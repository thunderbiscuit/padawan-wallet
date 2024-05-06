//
//  WalletViewModel.swift
//  PadawanWallet
//
//  Copyright 2024 thunderbiscuit, geigerzaehler, and contributors.
//  Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE.txt file.
//

import Foundation
import BitcoinDevKit
import SwiftUI

struct WalletModel: Identifiable {
    
    let id: String = UUID().uuidString //creates unique user id per model item
    let balance: UInt64
    let balanceText: String
    let transactions: [BitcoinDevKit.TransactionDetails] = []
    let blockHeight: UInt32 = 0
}

struct EsploraServerURLNetwork {
//    struct Bitcoin {
//        private static let blockstream = "https://blockstream.info/api"
//        private static let kuutamo = "https://esplora.kuutamo.cloud"
//        private static let mempoolspace = "https://mempool.space/api"
//        private static let electrum = "ssl://electrum.blockstream.info:50002"
//        static let allValues = [
//            blockstream,
//            kuutamo,
//            mempoolspace,
//            electrum
//        ]
//    }
    struct Regtest {
        private static let local = "http://127.0.0.1:3002"
        static let allValues = [
            local
        ]
    }
    struct Signet {
        static let bdk = "https://signet.bitcoindevkit.net:3003/"
        static let mutiny = "https://mutinynet.com/api"
        static let allValues = [
            bdk,
            mutiny
        ]
    }
    struct Testnet {
        static let blockstream = "https://blockstream.info/testnet/api/"
        static let kuutamo = "https://esplora.testnet.kuutamo.cloud"
        static let mempoolspace = "https://mempool.space/testnet/api/"
        static let electrum = "ssl://electrum.blockstream.info:60002" //"ssl://electrum.blockstream.info:50002"
        static let allValues = [
            blockstream,
            kuutamo,
            mempoolspace,
            electrum
        ]
    }
}

@Observable class WalletViewModel {
    
    @ObservationIgnored @AppStorage("isOnboarding") var isOnboarding: Bool?
    @ObservationIgnored @AppStorage("secretMnemonic") var secretMnemonic: String? //TODO this will need to be stored in the keychain!!
    @ObservationIgnored @AppStorage("secretDescriptor") var secretDescriptor: String? //TODO this will need to be stored in the keychain!!
    @ObservationIgnored @AppStorage("secretChangeDescriptor") var secretChangeDescriptor: String? //TODO this will need to be stored in the keychain!!
    
    enum State {
        case empty
        case loading
        case failed(Error)
        case loaded(Wallet, Blockchain)
    }
    
    enum SyncState : Equatable {
        
        static func == (lhs: WalletViewModel.SyncState, rhs: WalletViewModel.SyncState) -> Bool {
            switch (lhs, rhs) {
                case (.empty, .empty),(.syncing, .syncing), (.synced, .synced), (.failed, .failed): return true
                default: return false
            }
        }
        
        case empty
        case syncing
        case synced
        case failed(Error)
    }
    
    private(set) var key = "private_key"
    private(set) var state = State.empty
    private(set) var syncState = SyncState.empty
    private(set) var balance: UInt64 = 0
    private(set) var balanceText = "sync plz"
    private(set) var transactions: [BitcoinDevKit.TransactionDetails] = []
    private(set) var blockHeight: UInt32 = 0
    private(set) var firstTimeRunning: Bool = true
    var network: Network = Network.testnet
//    private var blockchainConfig: BlockchainConfig?
//    private var wallet: Wallet?
    
    var sendViewError: BdkError?
    var showingSendViewErrorAlert = false
    
    func toggleBTCDisplay(displayOption: String) {
        
        if displayOption == "BTC" {
            self.balanceText = String(format: "%.8f", Double(self.balance) / Double(100000000))
        }
        else {
            self.balanceText = String(self.balance)
        }
    }
    
    func send(recipient: String, amount: UInt64, fee: Float) {
        
        switch state {
            
        case .loaded(let wallet, let blockchain):
            
            do {
                let address = try Address(address: recipient, network: wallet.network())
                let script = address.scriptPubkey()
                let txBuilder = TxBuilder().addRecipient(script: script, amount: amount).feeRate(satPerVbyte: fee)
                let details = try txBuilder.finish(wallet: wallet)
                let _ = try wallet.sign(psbt: details.psbt, signOptions: nil)
                let tx = details.psbt.extractTx()
                try blockchain.broadcast(transaction: tx)
                //let txid = details.psbt.txid()
                //print(txid)
            }
            catch let error as BdkError {
                self.sendViewError = .Generic(message: error.description)
                self.showingSendViewErrorAlert = true
            } catch {
                self.sendViewError = .Generic(message: "Error Sending")
                self.showingSendViewErrorAlert = true
            }
         
        default:
            self.sendViewError = .Generic(message:"no wallet loaded!")
            self.showingSendViewErrorAlert = true
            
        }
    }
    
    func  getBlockHeight() {
        
        DispatchQueue.global().async { [weak self] in
            guard let self = self else { return }
            
            switch self.state {
            case .loaded(_, let blockchain):
                
                do {
                    let tempBlockHeight = try blockchain.getHeight()
                    
                    DispatchQueue.main.async {
                        self.blockHeight = tempBlockHeight
                    }
                }
                catch let error {
                    print(error)
                }
                
            default:
                print("not loaded")
                do { }
            }
        }
    }
    
    func createWallet(words: String?) throws {

        do {
            let baseUrl =
            //            try! keyService.getEsploraURL()
            //            ?? Constants.Config.EsploraServerURLNetwork.Testnet.mempoolspace
            EsploraServerURLNetwork.Testnet.electrum
            
            let esploraConfig = EsploraConfig(
                baseUrl: baseUrl,
                proxy: nil,
                concurrency: nil,
                stopGap: UInt64(20),
                timeout: nil
            )
            let blockchainConfig = BlockchainConfig.esplora(config: esploraConfig)
            //        self.blockchainConfig = blockchainConfig
            let blockchain = try Blockchain(config: blockchainConfig)
            
            var words12: String
            if let words = words, !words.isEmpty {
                words12 = words
            } else {
                let mnemonic = Mnemonic(wordCount: WordCount.words12)
                words12 = mnemonic.asString()
            }
            
            let mnemonic = try Mnemonic.fromString(mnemonic: words12)
            
            let secretKey = DescriptorSecretKey(
                network: network,
                mnemonic: mnemonic,
                password: nil
            )
            
            let descriptor = Descriptor.newBip86(
                secretKey: secretKey,
                keychain: .external,
                network: network
            )

//use for testing
//let descriptor = try Descriptor.init(descriptor: "wpkh(tprv8ZgxMBicQKsPeSitUfdxhsVaf4BXAASVAbHypn2jnPcjmQZvqZYkeqx7EHQTWvdubTSDa5ben7zHC7sUsx4d8tbTvWdUtHzR8uhHg2CW7MT/*)", network: network)
            
            let changeDescriptor = Descriptor.newBip86(
                secretKey: secretKey,
                keychain: .internal,
                network: network
            )

//use for testing
//let changeDescriptor = try Descriptor.init(descriptor: "wpkh(tprv8ZgxMBicQKsPdy6LMhUtFHAgpocR8GC6QmwMSFpZs7h6Eziw3SpThFfczTDh5rW2krkqffa11UpX3XkeTTB2FvzZKWXqPY54Y6Rq4AQ5R8L/84'/0'/0'/1/*)",network: network)
            
            secretMnemonic = mnemonic.asString() //TODO this will need to be stored in the keychain!!
            secretDescriptor = descriptor.asString() //TODO this will need to be stored in the keychain!!
            secretChangeDescriptor = changeDescriptor.asStringPrivate() //TODO this will need to be stored in the keychain!!
            
            let wallet = try Wallet(
                descriptor: descriptor,
                changeDescriptor: changeDescriptor,
                network: network,
                databaseConfig: .memory
            )
            //       self.wallet = wallet
            DispatchQueue.main.async {
                self.state = State.loaded(wallet, blockchain)
                self.isOnboarding = false
            }
            
        } catch let error {
            DispatchQueue.main.async {
                self.state = State.failed(error)
            }
        }
    }
    
    func load() {
            
        switch state {
            
        case .empty:
            state = .loading
            do {
                
                //TODO read secret data from storage. this will need to be stored in the keychain!!
                if let mnemonic = secretMnemonic, secretMnemonic != "",
                   let descriptor = secretDescriptor, secretDescriptor != "",
                   let changeDescriptor = secretChangeDescriptor, secretChangeDescriptor != "" {
                    
                    let blockChainUrl = EsploraServerURLNetwork.Testnet.electrum //"ssl://electrum.blockstream.info:50002"
                                    
                    let electrum = ElectrumConfig(url: blockChainUrl, socks5: nil, retry: 5, timeout: nil, stopGap: 10, validateDomain: true)
                                    
                    let blockchainConfig = BlockchainConfig.electrum(config: electrum)
                    let blockchain = try Blockchain(config: blockchainConfig)
                        
                    let wallet = try Wallet(descriptor: Descriptor(descriptor: descriptor, network: self.network),
                                            changeDescriptor: Descriptor(descriptor: changeDescriptor, network: self.network),
                                            network: self.network,
                                            databaseConfig: .memory)
                    
                    DispatchQueue.main.async {
                        self.state = State.loaded(wallet, blockchain)
                        self.getBlockHeight()
                        self.sync()
                        return
                    }
                }
                else {
                    try createWallet(words: nil) //continue to create new wallet if one does not exist
                }
            }
            catch {
                print("error creating wallet")
            }
        case .loading:
            return
        default:
            return //wallet already created
        }
        
//        DispatchQueue.global().async { [weak self] in
//            guard let self = self else { return }
//
//            do {
//                Pay-to-pubkey scripts (P2PK), through the pk function.
//                Pay-to-pubkey-hash scripts (P2PKH), through the pkh function.
//                Pay-to-witness-pubkey-hash scripts (P2WPKH), through the wpkh function.
//                Pay-to-script-hash scripts (P2SH), through the sh function.
//                Pay-to-witness-script-hash scripts (P2WSH), through the wsh function.
//                Pay-to-taproot outputs (P2TR), through the tr function.
//            }
//        }
    }
    
    func sync() {
        self.balanceText = "syncing"
        
        DispatchQueue.global().async { [weak self] in
            guard let self = self else { return }
            
            switch self.state {
            case .loaded(let wallet, let blockchain):
                DispatchQueue.main.async {
                    self.syncState = .syncing
                }
                do {
                    // TODO use this progress update to show "syncing"
                    try wallet.sync(blockchain: blockchain, progress: nil)
                    let balance = try wallet.getBalance().confirmed
                    let wallet_transactions: [TransactionDetails] = try wallet.listTransactions(includeRaw: false)
                    
                    DispatchQueue.main.async {
                        self.syncState = .synced
                        self.balance = balance
                        self.balanceText = String(format: "%.8f", Double(self.balance) / Double(100000000))
                        self.transactions = wallet_transactions.sorted().reversed()
                    }
                } catch let error {
                    print(error)
                    DispatchQueue.main.async {
                        self.syncState = .failed(error)
                    }
                }
            default: do { }
                print("default")
            }
        }
    }
}
