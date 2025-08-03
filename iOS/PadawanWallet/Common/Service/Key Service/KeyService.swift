//
//  KeyService.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 03/08/25.
//

import BitcoinDevKit
import Foundation
import KeychainAccess

private extension String {
    static let backupInfoKey = "BackupInfo"
}

// MARK: - KeyService

private struct KeyService {
    static let shared = KeyService()
    
    private let keychain: Keychain
    private let keychainLabel = Bundle.main.infoDictionary?["CFBundleName"] as? String ?? Bundle.main.bundleIdentifier ?? "Unknown Bundle"
    
    init() {
        let bundleIdentifier = Bundle.main.bundleIdentifier ?? "Unknown Bundle"
        let keychain = Keychain(service: bundleIdentifier)
            .label(keychainLabel)
            .synchronizable(false)
            .accessibility(.whenUnlocked)
        self.keychain = keychain
    }
    
    func deleteBackupInfo() throws {
        try keychain.remove(.backupInfoKey)
    }
    
    func getBackupInfo() throws -> BackupInfo {
        guard let encryptedJsonData = try keychain.getData(.backupInfoKey) else {
            throw KeyServiceError.backupInfoError
        }
        let decoder = JSONDecoder()
        let backupInfo = try decoder.decode(BackupInfo.self, from: encryptedJsonData)
        return backupInfo
    }
    
    func saveBackupInfo(backupInfo: BackupInfo) throws {
        let encoder = JSONEncoder()
        let data = try encoder.encode(backupInfo)
        keychain[data: .backupInfoKey] = data
    }
}


// MARK: - KeyClient
struct KeyClient {
    let deleteBackupInfo: () throws -> Void
    let getBackupInfo: () throws -> BackupInfo
    let saveBackupInfo: (BackupInfo) throws -> Void
}

extension KeyClient {
    static let live: KeyClient = .init(
        deleteBackupInfo: {
            try KeyService.shared.deleteBackupInfo()
        },
        getBackupInfo: {
            try KeyService.shared.getBackupInfo()
        },
        saveBackupInfo: { backupInfo in
            try KeyService.shared.saveBackupInfo(backupInfo: backupInfo)
        }
    )
}
