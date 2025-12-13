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

// MARK: - factories

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

// MARK: - mock

class InMemoryKeyStore {
    var stored: BackupInfo?
    func save(_ info: BackupInfo) throws { self.stored = info }
    func get() throws -> BackupInfo {
        guard let info = stored else { throw KeyServiceError.backupInfoError }
        return info
    }
    func delete() { self.stored = nil }
}
