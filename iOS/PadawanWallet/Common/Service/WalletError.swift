//
//  WalletError.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 02/08/25.
//

import Foundation

enum WalletError: Error {
    case dbNotFound
    
    var errorDescription: String? {
        switch self {
        case .dbNotFound:
            return "Database not found"
        }
    }
}
