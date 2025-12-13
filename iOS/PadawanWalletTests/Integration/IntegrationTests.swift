//
//  PadawanWalletTests.swift
//  PadawanWalletTests
//
//  Created by thunderbiscuit on 2025-04-16.
//

import Testing
import BitcoinDevKit
@testable import PadawanWallet

@Suite(.serialized)
struct IntegrationTests {
    
    private let client: BDKClient
    init() async throws {
        try? Persister.deleteConnection()
        let inMemoryKeys = InMemoryKeyStore()
        let keyClient = KeyClientFactory.createFake(store: inMemoryKeys)
        let service = BDKService(keyClient: keyClient, electrumClient: nil)
        self.client = BDKClientFactory.create(service: service)
    }
    
    @Test("Should create a new wallet by generating real keys")
    func testCreateNewWallet() throws {
        try client.createNewWallet()
        let address = try client.getAddress()
        
        #expect(address.starts(with: "tb1"))
    }
    
    @Test("Should load a previously saved wallet")
    func testLoadWallet() throws {
        try client.createNewWallet()
        try client.loadWallet()
        let balance = try client.getBalance()
        
        #expect(balance.total.toSat() == 0)
    }
    
    @Test("Should generate valid and rotating receiving addresses")
    func testGenerateReceiveAddress() throws {
        try client.createNewWallet()
        
        let address1 = try client.getAddress()
        
        #expect(address1.starts(with: "tb1") && address1.count > 20)
        
        let address2 = try client.getAddress()
        
        #expect(!address2.isEmpty)
    }
    
    @Test("Should import a wallet with a valid 12-word recovery phrase")
    func testImportWallet() throws {
        let validSeed = TestSeeds.valid
        try client.importWallet(validSeed)
        let recovery = try client.getRecoveryPhase()
        
        #expect(recovery == validSeed)
    }
    
    @Test("Should ignore extra whitespace when importing the seed")
    func testImportWalletWithWhitespace() throws {
        let messySeed = "  \(TestSeeds.valid)  "
        try client.importWallet(messySeed)
        let recovery = try client.getRecoveryPhase()
        
        #expect(recovery == TestSeeds.valid)
    }
    
    @Test("Should fail when importing invalid seeds")
    func testImportFailures() {
        // bad word count
        expectImportFailure(seed: TestSeeds.badCount, errorContains: ["WordCount", "Invalid", "Generic"])
        // unknown word
        expectImportFailure(seed: TestSeeds.unknownWord, errorContains: ["Invalid", "Unknown"])
        // bad checksum
        expectImportFailure(seed: TestSeeds.badChecksum, errorContains: ["Checksum", "Invalid"])
    }
    
    @Test("Should return an empty list of transactions for a new wallet")
    func testEmptyTransactionHistory() throws {
        try client.createNewWallet()
        let history = try client.transactions()
        
        #expect(history.isEmpty)
    }
    
    @Test("Should fail to create transaction without funds")
    func testCreateTransactionInsufficientFunds() throws {
        try client.createNewWallet()
        let targetAddress = TestAddresses.valid
        
        #expect(performing: {
            _ = try client.createTransaction(targetAddress, 1000, 1)
        }, throws: { error in
            guard let txError = error as? CreateTxError else { return false }
            if case .CoinSelection = txError { return true }
            return false
        })
    }
    
    @Test("Should fail when sending to invalid address format")
    func testCreateTransactionInvalidAddressFormat() throws {
        try client.createNewWallet()
        let garbageAddress = "ImNotBitcoinAdress123"
        
        #expect(performing: {
            _ = try client.createTransaction(garbageAddress, 1000, 1)
        }, throws: { error in
            guard let parseError = error as? AddressParseError else { return false }
            if case .Base58 = parseError { return true }
            return false
        })
    }
    
    @Test("Should fail when sending to Mainnet address on Testnet")
    func testCreateTransactionNetworkMismatch() throws {
        try client.createNewWallet()
        let mainnetAddress = TestAddresses.mainnet
        
        #expect(performing: {
            _ = try client.createTransaction(mainnetAddress, 1000, 1)
        }, throws: { error in
            return error.localizedDescription.contains("Network") ||
            error.localizedDescription.contains("Invalid")
        })
    }
    
    @Test("Satoshi-to-BTC string formatting")
    func testSatsFormatting() {
        let cases: [(sats: UInt64, expected: String)] = [
            // 1 btc
            (100_000_000, "1.00 000 000"),
            // 0 btc
            (0, "0.00 000 000"),
            // dust
            (546, "0.00 000 546"),
            // broke value
            (21_500_400, "0.21 500 400")
        ]
        for testCase in cases {
            #expect(testCase.sats.formattedBTC() == testCase.expected)
        }
    }
}

extension IntegrationTests {
    private func expectImportFailure(seed: String, errorContains keywords: [String]) {
        #expect(performing: {
            try client.importWallet(seed)
        }, throws: { error in
            keywords.contains { error.localizedDescription.contains($0) }
        })
    }
}
