//
//  BDKService.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 02/08/25.
//

import BitcoinDevKit
import SwiftUI

// MARK: - BDKService
private class BDKService {
    
    // MARK: - Configs
    private let network: Network = .signet
    private static let batchSize = UInt64(10)
    private static let stopGap: UInt64 = 20 // using https://github.com/bitcoin/bips/blob/master/bip-0044.mediawiki#address-gap-limit
    private static let parallelRequests = UInt64(5)
    
    // MARK: -
    static let shared = BDKService()
    
    private var persister: Persister?
    private var wallet: Wallet?
    private(set) var needsFullScan: Bool {
        get {
            Session.shared.isFullScanRequired
        }
        set {
            Session.shared.isFullScanRequired = newValue
        }
    }
    private let keyClient: KeyClient
    private let electrumClient: ElectrumClient?
    
    init(
        keyClient: KeyClient = .live,
        electrumClient: ElectrumClient? = .live
    ) {
        self.keyClient = keyClient
        self.electrumClient = electrumClient
    }
    
    // Create a new wallet or import
    func createWallet(_ seed: String?) throws {
        self.persister = try Persister.createConnection()
        guard let persister = self.persister else {
            throw BDKServiceError.dbNotFound
        }
        
        var _seed: String
        if let seed = seed, !seed.isEmpty {
            needsFullScan = true
            _seed = seed
        } else {
            needsFullScan = false
            let mnemonicWords = Mnemonic(wordCount: .words12)
            _seed = mnemonicWords.description
        }
        
        let mnemonic = try Mnemonic.fromString(mnemonic: _seed)
        let secretKey = DescriptorSecretKey(
            network: network,
            mnemonic: mnemonic,
            password: nil
        )
        let descriptors = Descriptor.createDescriptors(
            secretKey: secretKey,
            network: network
        )
        
        let wallet = try Wallet(
            descriptor: descriptors.descriptor,
            changeDescriptor: descriptors.changeDescriptor,
            network: network,
            persister: persister
        )
        self.wallet = wallet
        
        let backupInfo = BackupInfo(
            mnemonic: mnemonic.description,
            descriptor: descriptors.descriptor.toStringWithSecret(),
            changeDescriptor: descriptors.changeDescriptor.toStringWithSecret()
        )
        try keyClient.saveBackupInfo(backupInfo)
    }
    
    func loadWalletFromBackup() throws {
        let backupInfo = try keyClient.getBackupInfo()
        let descriptor = try Descriptor(descriptor: backupInfo.descriptor, network: network)
        let changeDescriptor = try Descriptor(descriptor: backupInfo.changeDescriptor, network: network)
        try loadWallet(descriptor: descriptor, changeDescriptor: changeDescriptor, network: network)
    }
    
    func getBalance() throws -> Balance {
        guard let wallet = self.wallet else { throw BDKServiceError.walletNotFound }
        let balance = wallet.balance()
        return balance
    }
    
    func fullScanWithInspector(inspector: FullScanScriptInspector) async throws {
        do {
            guard let wallet = self.wallet else { throw BDKServiceError.walletNotFound }
            let fullScanRequest = try wallet.startFullScan()
                .inspectSpksForAllKeychains(inspector: inspector)
                .build()
            
            guard let update = try electrumClient?.fullScan(
                request: fullScanRequest,
                stopGap: BDKService.stopGap,
                batchSize: BDKService.parallelRequests,
                fetchPrevTxouts: true
            ) else {
                throw BDKServiceError.clientNotStarted
            }
            
            let _ = try wallet.applyUpdate(update: update)
            guard let persister = self.persister else {
                throw BDKServiceError.dbNotFound
            }
            let _ = try wallet.persist(persister: persister)
            
        } catch is CannotConnectError {
            throw BDKServiceError.needResync
            
        } catch is PersistenceError {
            throw BDKServiceError.dbNotFound
            
        } catch {
            throw BDKServiceError.errorWith(message: error.localizedDescription)
        }
    }
    
    func syncWithInspector(inspector: SyncScriptInspector) async throws {
        do {
            guard let wallet = self.wallet else { throw BDKServiceError.walletNotFound }
            let syncRequest = try wallet.startSyncWithRevealedSpks()
                .inspectSpks(inspector: inspector)
                .build()
            
            guard let update = try electrumClient?.sync(
                request: syncRequest,
                batchSize: BDKService.batchSize,
                fetchPrevTxouts: true
            ) else {
                throw BDKServiceError.clientNotStarted
            }
            
            let _ = try wallet.applyUpdate(update: update)
            guard let persister = self.persister else {
                throw BDKServiceError.dbNotFound
            }
            let _ = try wallet.persist(persister: persister)
            
        } catch is CannotConnectError {
            throw BDKServiceError.needResync
            
        } catch is PersistenceError {
            throw BDKServiceError.dbNotFound
            
        } catch {
            throw BDKServiceError.errorWith(message: error.localizedDescription)
        }
    }
    
    func transactions() throws -> [TxDetails] {
        guard let wallet = self.wallet else {
            throw BDKServiceError.walletNotFound
        }
        let transactions = wallet.transactions()
        let sortedTransactions = transactions.sorted { (tx1, tx2) in
            return tx1.chainPosition.isBefore(tx2.chainPosition)
        }
        
        let details: [TxDetails] = sortedTransactions.compactMap {
            wallet.txDetails(txid: $0.transaction.computeTxid())
        }
        
        return details
    }
    
    func getAddress() throws -> String {
        guard let wallet else {
            throw BDKServiceError.walletNotFound
        }
        guard let persister else {
            throw BDKServiceError.dbNotFound
        }
        let nextAddress = wallet.revealNextAddress(keychain: .external)
        _ = try wallet.persist(persister: persister)
        return nextAddress.address.description
    }
    
    func buildTransaction(
        address: String,
        amount: UInt64,
        feeRate: UInt64
    ) throws -> Psbt {
        guard let wallet = self.wallet else { throw BDKServiceError.walletNotFound }
        let script = try Address(address: address, network: self.network)
            .scriptPubkey()
        let txBuilder = try TxBuilder()
            .addRecipient(
                script: script,
                amount: Amount.fromSat(satoshi: amount)
            )
            .feeRate(feeRate: FeeRate.fromSatPerVb(satVb: feeRate))
            .finish(wallet: wallet)
        return txBuilder
    }
    
    func send(
        address: String,
        amount: UInt64,
        feeRate: UInt64
    ) async throws {
        let psbt = try buildTransaction(
            address: address,
            amount: amount,
            feeRate: feeRate
        )
        try await signAndBroadcast(psbt: psbt)
    }
    
    func getRecoveryPhase() throws -> String {
        let backupInfo = try keyClient.getBackupInfo()
        
        return backupInfo.mnemonic
    }
    
    // MARK: - Private
    
    private func signAndBroadcast(psbt: Psbt) async throws {
        guard let wallet = self.wallet else { throw BDKServiceError.walletNotFound }
        let isSigned = try wallet.sign(psbt: psbt)
        if isSigned {
            let transaction = try psbt.extractTx()
            _ = try electrumClient?.transactionBroadcast(tx: transaction)
        } else {
            throw BDKServiceError.notSigned
        }
    }
    
    private func loadWallet(
        descriptor: Descriptor,
        changeDescriptor: Descriptor,
        network: Network
    ) throws {
        if wallet != nil { return }
        guard FileManager.default.fileExists(atPath: URL.persistenceBackendPath) else {
            let persister = try Persister.createConnection()
            self.persister = persister
            self.wallet = try Wallet(
                descriptor: descriptor,
                changeDescriptor: changeDescriptor,
                network: network,
                persister: persister
            )
            return
        }
        
        do {
            let persister = try Persister.loadConnection()
            self.persister = persister
            self.wallet = try Wallet.load(
                descriptor: descriptor,
                changeDescriptor: changeDescriptor,
                persister: persister
            )
            
        } catch is LoadWithPersistError {
            try Persister.deleteConnection()
            let persister = try Persister.createConnection()
            self.wallet = try Wallet(
                descriptor: descriptor,
                changeDescriptor: changeDescriptor,
                network: network,
                persister: persister
            )
        }
    }
}

// MAR: - BDKClient
struct BDKClient {
    let createNewWallet: () throws -> Void
    let importWallet: ( _ seed: String) throws -> Void
    let loadWallet: () throws -> Void
    let getBalance: () throws -> Balance
    let syncWithInspector: (SyncScriptInspector) async throws -> Void
    let fullScanWithInspector: (FullScanScriptInspector) async throws -> Void
    let needsFullScan: () -> Bool
    let transactions: () throws -> [TxDetails]
    let getAddress: () throws -> String
    let createTransaction: (String, UInt64, UInt64) throws -> Psbt
    let send: (String, UInt64, UInt64) async throws -> Void
    let getRecoveryPhase: () throws -> String
}

extension BDKClient {
    static let live: BDKClient = .init(
        createNewWallet: {
            try BDKService.shared.createWallet(nil)
        },
        importWallet: { seed in
            try BDKService.shared.createWallet(seed)
        },
        loadWallet: {
            try BDKService.shared.loadWalletFromBackup()
        },
        getBalance: {
            try BDKService.shared.getBalance()
        },
        syncWithInspector: { inspector in
            try await BDKService.shared.syncWithInspector(inspector: inspector)
        },
        fullScanWithInspector: { inspector in
            try await BDKService.shared.fullScanWithInspector(inspector: inspector)
        },
        needsFullScan: {
            BDKService.shared.needsFullScan
        },
        transactions: {
            try BDKService.shared.transactions()
        },
        getAddress: {
            try BDKService.shared.getAddress()
        },
        createTransaction: { (address, amount, feeRate) in
            try BDKService.shared.buildTransaction(
                address: address,
                amount: amount,
                feeRate: feeRate
            )
        },
        send: { (address, amount, feeRate) in
            try await BDKService.shared.send(address: address, amount: amount, feeRate: feeRate)
        },
        getRecoveryPhase: {
            try BDKService.shared.getRecoveryPhase()
        }
    )
}

extension ElectrumClient {
    private static let url = "ssl://mempool.space:60602"
    
    static var live: ElectrumClient? {
        do {
            return try ElectrumClient(url: ElectrumClient.url)
        } catch {
            return nil
        }
    }
}

#if DEBUG
extension BDKService {
    static let mock: BDKService = .init(keyClient: .mock)
}

extension BDKClient {
    static let mock: BDKClient = .init(
        createNewWallet: {
            try BDKService.mock.createWallet(nil)
        },
        importWallet: { seed in
            try BDKService.mock.createWallet(seed)
        },
        loadWallet: {
            try BDKService.mock.loadWalletFromBackup()
        },
        getBalance: { .mock },
        syncWithInspector: { inspect in
            try await BDKService.mock.syncWithInspector(inspector: inspect)
        },
        fullScanWithInspector: { inspect in
            try await BDKService.mock.fullScanWithInspector(inspector: inspect)
        },
        needsFullScan: { true },
        transactions: { [] },
        getAddress: { "tb1pd8jmenqpe7rz2mavfdx7uc8pj7vskxv4rl6avxlqsw2u8u7d4gfs97durt" },
        createTransaction: { _, _, _ in
            let pb64 = """
                cHNidP8BAIkBAAAAAeaWcxp4/+xSRJ2rhkpUJ+jQclqocoyuJ/ulSZEgEkaoAQAAAAD+////Ak/cDgAAAAAAIlEgqxShDO8ifAouGyRHTFxWnTjpY69Cssr3IoNQvMYOKG/OVgAAAAAAACJRIGnlvMwBz4Ylb6xLTe5g4ZeZCxmVH/XWG+CDlcPzzaoT8qoGAAABAStAQg8AAAAAACJRIFGGvSoLWt3hRAIwYa8KEyawiFTXoOCVWFxYtSofZuAsIRZ2b8YiEpzexWYGt8B5EqLM8BE4qxJY3pkiGw/8zOZGYxkAvh7sj1YAAIABAACAAAAAgAAAAAAEAAAAARcgdm/GIhKc3sVmBrfAeRKizPAROKsSWN6ZIhsP/MzmRmMAAQUge7cvJMsJmR56NzObGOGkm8vNqaAIJdnBXLZD2PvrinIhB3u3LyTLCZkeejczmxjhpJvLzamgCCXZwVy2Q9j764pyGQC+HuyPVgAAgAEAAIAAAACAAQAAAAYAAAAAAQUgtIFPrI2EW/+PJiAmYdmux88p0KgeAxDFLMoeQoS66hIhB7SBT6yNhFv/jyYgJmHZrsfPKdCoHgMQxSzKHkKEuuoSGQC+HuyPVgAAgAEAAIAAAACAAAAAAAIAAAAA
                """
            return try Psbt(psbtBase64: pb64)
        },
        send: { _, _, _ in },
        getRecoveryPhase: { "" }
    )
}
#endif
