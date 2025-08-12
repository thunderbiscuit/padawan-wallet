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
            descriptor: descriptors.descriptor.description,
            changeDescriptor: descriptors.changeDescriptor.description
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
    
    func syncWithInspector(inspector: SyncScriptInspector) async throws {
        do {
            guard let wallet = self.wallet else { throw BDKServiceError.walletNotFound }
            let syncRequest = try wallet.startSyncWithRevealedSpks()
                .inspectSpks(inspector: inspector)
                .build()
            guard let update: Update = try electrumClient?.sync(
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
    
    // MARK: - Private
    
    private func loadWallet(
        descriptor: Descriptor,
        changeDescriptor: Descriptor,
        network: Network
    ) throws {
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
        transactions: { [] }
    )
}
#endif
