//
//  WalletViewModel.swift
//  iosApp
//
//  Created by geigerzaehler on 2023-12-04.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import BitcoinDevKit

extension TransactionDetails: Comparable {
    public static func == (lhs: BitcoinDevKit.TransactionDetails, rhs: BitcoinDevKit.TransactionDetails) -> Bool {
        
        let lhs_timestamp: UInt64 = lhs.confirmationTime?.timestamp ?? UInt64.max;
        let rhs_timestamp: UInt64 = rhs.confirmationTime?.timestamp ?? UInt64.max;
       
        return lhs_timestamp == rhs_timestamp
    }
    
    public static func < (lhs: TransactionDetails, rhs: TransactionDetails) -> Bool {
        
        let lhs_timestamp: UInt64 = lhs.confirmationTime?.timestamp ?? UInt64.max;
        let rhs_timestamp: UInt64 = rhs.confirmationTime?.timestamp ?? UInt64.max;
        
        return lhs_timestamp < rhs_timestamp
    }
}

enum HexConvertError: Error {
    case wrongInputStringLength
    case wrongInputStringCharacters
}

extension StringProtocol {
    
    func asHexArrayFromNonValidatedSource() -> [UInt8] {
        var startIndex = self.startIndex
        return stride(from: 0, to: count, by: 2).compactMap { _ in
            let endIndex = index(startIndex, offsetBy: 2, limitedBy: self.endIndex) ?? self.endIndex
            defer { startIndex = endIndex }
            return UInt8(self[startIndex..<endIndex], radix: 16)
        }
    }

    func asHexArray() throws -> [UInt8] {
        if count % 2 != 0 { throw HexConvertError.wrongInputStringLength }
        let characterSet = "0123456789ABCDEFabcdef"
        let wrongCharacter = first { return !characterSet.contains($0) }
        if wrongCharacter != nil { throw HexConvertError.wrongInputStringCharacters }
        return asHexArrayFromNonValidatedSource()
    }
}

struct WalletModel: Identifiable {
    
    let id: String = UUID().uuidString //creates unique user id per model item
    let balance: UInt64
    let balanceText: String
    let transactions: [BitcoinDevKit.TransactionDetails] = []
    let blockHeight: UInt32 = 0
}

class WalletViewModel: ObservableObject {
    
    enum State {
        case empty
        case loading
        case failed(Error)
        case loaded(Wallet, Blockchain)
    }
    
    enum SyncState {
        case empty
        case syncing
        case synced
        case failed(Error)
    }
    
    private(set) var key = "private_key"
    @Published private(set) var state = State.empty
    @Published private(set) var syncState = SyncState.empty
    @Published private(set) var balance: UInt64 = 0
    @Published private(set) var balanceText = "sync plz"
    @Published private(set) var transactions: [BitcoinDevKit.TransactionDetails] = []
    @Published private(set) var blockHeight: UInt32 = 0
    @Published private(set) var firstTimeRunning: Bool = true
    
    
    func  firstTimeRunningToggle() {
        
        firstTimeRunning.toggle()
        print(firstTimeRunning)
    }
    
    func onSend(recipient: String, amount: UInt64, fee: Float) -> Result<Int, Error> {
        
        switch state {
            
        case .loaded(let wallet, let blockchain):
            
                do {
                    let address = try Address(address: recipient)
                    let script = address.scriptPubkey()
                    let txBuilder = TxBuilder().addRecipient(script: script, amount: amount).feeRate(satPerVbyte: fee)
                    let details = try txBuilder.finish(wallet: wallet)
                    let _ = try wallet.sign(psbt: details.psbt, signOptions: nil)
                    let tx = details.psbt.extractTx()
                    try blockchain.broadcast(transaction: tx)
                    let txid = details.psbt.txid()
                    print(txid)
                    return(Result.success(1))
                } 
                catch let error {
                    print(error)
                    return(Result.failure(error))
                }
         
        default:
            print("error onSend")
            return(Result.success(1))
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
    
    func load() {
        state = .loading
        
        DispatchQueue.global().async { [weak self] in
            guard let self = self else { return }
            
            let db = DatabaseConfig.memory
            
            do {
                
                let network = Network.testnet
                //let network = Network.bitcoin
                //let network = Network.signet
                //let network = Network.regtest
                
                let blockChainUrl = "ssl://electrum.blockstream.info:50002"
                //let blockChainUrlTestnet = "ssl://electrum.blockstream.info:60002"
                
                let electrum = ElectrumConfig(url: blockChainUrl, socks5: nil, retry: 5, timeout: nil, stopGap: 10, validateDomain: true)
                
                let blockchainConfig = BlockchainConfig.electrum(config: electrum)
                let blockchain = try Blockchain(config: blockchainConfig)
                
                let descriptor = try Descriptor.init(descriptor: "wpkh(tprv8ZgxMBicQKsPeSitUfdxhsVaf4BXAASVAbHypn2jnPcjmQZvqZYkeqx7EHQTWvdubTSDa5ben7zHC7sUsx4d8tbTvWdUtHzR8uhHg2CW7MT/*)", network: network)
                
                //                Pay-to-pubkey scripts (P2PK), through the pk function.
                //                Pay-to-pubkey-hash scripts (P2PKH), through the pkh function.
                //                Pay-to-witness-pubkey-hash scripts (P2WPKH), through the wpkh function.
                //                Pay-to-script-hash scripts (P2SH), through the sh function.
                //                Pay-to-witness-script-hash scripts (P2WSH), through the wsh function.
                //                Pay-to-taproot outputs (P2TR), through the tr function.
                
                //from documentation
                //                let descriptor = try Descriptor.init(descriptor: "wpkh(tprv8ZgxMBicQKsPdy6LMhUtFHAgpocR8GC6QmwMSFpZs7h6Eziw3SpThFfczTDh5rW2krkqffa11UpX3XkeTTB2FvzZKWXqPY54Y6Rq4AQ5R8L/84'/0'/0'/0/*)",network: network)
                //                let changeDescriptor = try Descriptor.init(descriptor: "wpkh(tprv8ZgxMBicQKsPdy6LMhUtFHAgpocR8GC6QmwMSFpZs7h6Eziw3SpThFfczTDh5rW2krkqffa11UpX3XkeTTB2FvzZKWXqPY54Y6Rq4AQ5R8L/84'/0'/0'/1/*)",network: network)
                
                let wallet = try Wallet(descriptor: descriptor, changeDescriptor: nil, network: network, databaseConfig: db)
                
                DispatchQueue.main.async {
                    self.state = State.loaded(wallet, blockchain)
                    self.getBlockHeight()
                }
            } catch let error {
                DispatchQueue.main.async {
                    self.state = State.failed(error)
                }
            }
        }
    }
    
    func toggleBTCDisplay(displayOption: String) {
        
        if displayOption == "BTC" {
            self.balanceText = String(format: "%.8f", Double(self.balance) / Double(100000000))
        }
        else {
            self.balanceText = String(self.balance)
        }
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
