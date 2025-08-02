//
//  Persister+Util.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 02/08/25.
//

import BitcoinDevKit
import Foundation

extension Persister {
    static func createConnection() throws -> Persister {
        try deleteConnection()
        try FileManager.default.ensureDirectoryExists(at: URL.walletDataDirectoryURL)
        try FileManager.default.removeOldFlatFileIfNeeded(at: URL.defaultWalletDirectory)
        let persister = try Persister.newSqlite(path: URL.persistenceBackendPath)
        return persister
    }

    static func loadConnection() throws -> Persister {
        let persistenceBackendPath = URL.persistenceBackendPath
        let persister = try Persister.newSqlite(path: persistenceBackendPath)
        return persister
    }

    static func deleteConnection() throws {
        let walletDataDirectoryURL = URL.walletDataDirectoryURL
        if FileManager.default.fileExists(atPath: walletDataDirectoryURL.path) {
            try FileManager.default.removeItem(at: walletDataDirectoryURL)
        }
    }
}
