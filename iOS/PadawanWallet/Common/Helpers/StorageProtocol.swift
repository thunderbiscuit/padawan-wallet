//
//  StorageProtocol.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 05/10/25.
//

import Foundation

protocol StorageProtocol {
    func set(_ value: Any?, key: String)
    func get<T>(_ key: String) -> T?
    func remove(_ key: String)
}

final class UserDefaultsStorage: StorageProtocol {
    
    static let shared = UserDefaultsStorage()
    
    private let defaults: UserDefaults
    
    init(defaults: UserDefaults = .standard) {
        self.defaults = defaults
    }
    
    convenience init?(suiteName: String) {
        guard let defaults = UserDefaults(suiteName: suiteName) else {
            return nil
        }
        self.init(defaults: defaults)
    }
    
    func set(_ value: Any?, key: String) {
        defaults.set(value, forKey: key)
    }
    
    func get<T>(_ key: String) -> T? {
        defaults.object(forKey: key) as? T
    }
    
    func remove(_ key: String) {
        defaults.removeObject(forKey: key)
    }
}
