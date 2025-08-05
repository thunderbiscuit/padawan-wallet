//
//  BDKServiceTests.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 05/08/25.
//

import Testing
@testable import PadawanWallet

struct BDKClientTests {

    private let client = BDKClient.mock
    
    @Test("Should not throw when creating a new wallet")
    func testCreateNewWallet() throws {
        try client.createNewWallet()
    }
    
    @Test("Should not throw when importing a wallet")
    func testImportWallet() throws {
        try client.importWallet("omit arm chunk harbor path unfair coil radar stairs unfair raise memory")
    }
    
    @Test("Should throw BadWalletWordCount when importing a wallet with less than 12 words")
    func testBadImportWallet() throws {
        #expect(performing: {
            try client.importWallet("omit arm chunk harbor path unfair coil radar stairs unfair raise")
        }, throws: { error in
            error.localizedDescription.contains("BadWordCount")
        })
    }
    
    @Test("Should not throw when loading a wallet")
    func testLoadWallet() throws {
        try client.loadWallet()
    }
    
    @Test("Should return mock balance")
    func testGetBalance() throws {
        let balance = try client.getBalance()
        #expect(balance == .mock)
    }
}
