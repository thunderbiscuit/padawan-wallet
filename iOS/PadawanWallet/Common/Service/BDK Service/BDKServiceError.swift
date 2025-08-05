//
//  WalletError.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 02/08/25.
//

import Foundation

enum BDKServiceError: Error {
    case dbNotFound
    case walletNotFound
    
    var errorDescription: String? {
        switch self {
        case .dbNotFound:
            return "Database not found"
        
        case .walletNotFound:
            return "Wallet not found"
        }
    }
}
