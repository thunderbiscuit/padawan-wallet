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
    
    private let network: Network = .signet
    private let currentAddressType: AddressType = .bip84
    
    static let shared = BDKService()
    
    private var persister: Persister?
    private var wallet: Wallet?
    private var needsFullScan: Bool = false
    private let keyClient: KeyClient
    
    init(keyClient: KeyClient = .live) {
        self.keyClient = keyClient
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
            for: currentAddressType,
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
        }
    )
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
        loadWallet: { },
        getBalance: { .mock }
    )
}
#endif
