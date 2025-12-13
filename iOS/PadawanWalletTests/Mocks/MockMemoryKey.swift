//
//  MockMemoryKey.swift
//  PadawanWalletTests
//
//  Created by Vinicius Silva Moreira on 13/12/25.
//

@testable import PadawanWallet

class InMemoryKeyStore {
    var stored: BackupInfo?
    func save(_ info: BackupInfo) throws { self.stored = info }
    func get() throws -> BackupInfo {
        guard let info = stored else { throw KeyServiceError.backupInfoError }
        return info
    }
    func delete() { self.stored = nil }
}
