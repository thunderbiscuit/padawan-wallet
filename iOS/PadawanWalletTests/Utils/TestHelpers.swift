//
//  HelperTests.swift
//  PadawanWalletTests
//
//  Created by Vinicius Silva Moreira on 13/12/25.
//

import BitcoinDevKit
@testable import PadawanWallet

// MARK: - helpers

struct TestSeeds {
    static let valid = "space echo position wrist orient erupt relief museum myself grain wisdom tumble"
    static let badCount = "omit arm chunk harbor path unfair coil radar stairs unfair raise"
    static let unknownWord = "space echo position wrist orient erupt relief museum myself grain wisdom saladfruits"
    static let badChecksum = "tumble echo position wrist orient erupt relief museum myself grain wisdom space"
}

struct TestAddresses {
    static let valid = "tb1qw508d6qejxtdg4y5r3zarvary0c5xw7kxpjzsx"
    static let mainnet = "bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq"
}

// MARK: - factories & stubs

struct KeyClientFactory {
    static func createFake(store: InMemoryKeyStore) -> KeyClient {
        return KeyClient(
            deleteBackupInfo: { store.delete() },
            getBackupInfo: { try store.get() },
            saveBackupInfo: { try store.save($0) }
        )
    }
}

struct BDKClientFactory {
    static func create(service: BDKService) -> BDKClient {
        return BDKClient(
            createNewWallet: { try service.createWallet(nil) },
            importWallet: { seed in try service.createWallet(seed) },
            loadWallet: { try service.loadWalletFromBackup() },
            getBalance: { try service.getBalance() },
            syncWithInspector: { _ in },
            fullScanWithInspector: { _ in },
            needsFullScan: { service.needsFullScan },
            transactions: { try service.transactions() },
            getAddress: { try service.getAddress() },
            createTransaction: { addr, amt, fee in
                try service.buildTransaction(address: addr, amount: amt, feeRate: fee)
            },
            send: { _, _, _ in },
            getRecoveryPhase: { try service.getRecoveryPhase() }
        )
    }
}

func makeBalance(confirmedSats: UInt64) -> Balance {
    return Balance(
        immature: Amount.fromSat(satoshi: 0),
        trustedPending: Amount.fromSat(satoshi: 0),
        untrustedPending: Amount.fromSat(satoshi: 0),
        confirmed: Amount.fromSat(satoshi: confirmedSats),
        trustedSpendable: Amount.fromSat(satoshi: confirmedSats),
        total: Amount.fromSat(satoshi: confirmedSats)
    )
}

func makeStubClient(
    getBalance: @escaping () throws -> Balance = { makeBalance(confirmedSats: 0) },
    transactions: @escaping () throws -> [TxDetails] = { [] },
    needsFullScan: @escaping () -> Bool = { false },
    syncWithInspector: @escaping (SyncScriptInspector) async throws -> Void = { _ in },
    getAddress: @escaping () throws -> String = { "tb1_mock_address" },
    getRecoveryPhase: @escaping () throws -> String = { "mock word list recovery" },
    createNewWallet: @escaping () throws -> Void = { },
    importWallet: @escaping (String) throws -> Void = { _ in },
    createTransaction: @escaping (String, UInt64, UInt64) throws -> Psbt = { _, _, _ in throw BDKServiceError.generic },
    send: @escaping (String, UInt64, UInt64) async throws -> Void = { _, _, _ in }
) -> BDKClient {
    return BDKClient(
        createNewWallet: createNewWallet,
        importWallet: importWallet,
        loadWallet: { },
        getBalance: getBalance,
        syncWithInspector: syncWithInspector,
        fullScanWithInspector: { _ in },
        needsFullScan: needsFullScan,
        transactions: transactions,
        getAddress: getAddress,
        createTransaction: createTransaction,
        send: send,
        getRecoveryPhase: getRecoveryPhase
    )
}

// MARK: - constants

let validDummyPsbtBase64 = BDKService.pb64
