//
//  BDKService.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 02/08/25.
//

import BitcoinDevKit
import SwiftUI

private class BDKService {
    
    private let network: Network = .signet
    private let currentAddressType: AddressType = .bip84
    
    static let shared = BDKService()
    
    private var persister: Persister?
    private var wallet: Wallet?
    private var needsFullScan: Bool = false
    
    // Create a new wallet or import
    func createWallet(_ seed: String?) throws {
        self.persister = try Persister.createConnection()
        guard let persister = self.persister else {
            throw WalletError.dbNotFound
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
    }
}

struct BDKClient {
    let createWallet: ( _ seed: String?) throws -> Void
}

extension BDKClient {
    static let live: BDKClient = .init(
        createWallet: { seed in
            try BDKService.shared.createWallet(seed)
        }
    )
}

#if DEBUG
extension BDKClient {
    static let mock: BDKClient = .init(
        createWallet: { _ in }
    )
}
#endif
