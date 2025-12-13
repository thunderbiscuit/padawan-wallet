//
//  MockStorageProtocol.swift
//  PadawanWalletTests
//
//  Created by Vinicius Silva Moreira on 13/12/25.
//

@testable import PadawanWallet

class MockStorage: StorageProtocol {
    var store: [String: Any] = [:]
    
    func get<T>(_ key: String) -> T? { return store[key] as? T }
    func set<T>(_ value: T, key: String) { store[key] = value }
    func remove(_ key: String) { store.removeValue(forKey: key) }
}
