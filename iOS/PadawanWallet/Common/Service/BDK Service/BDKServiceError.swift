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
    case invalidSeed
    case generic
    case errorWith(message: String)
    case needResync
    case clientNotStarted
    
    var localizedDescription: String {
        switch self {
        case .dbNotFound:
            return "Database not found"
        
        case .walletNotFound:
            return "Wallet not found"
            
        case .invalidSeed:
            return "Invalid seed"
            
        case .generic:
            return "Generic error"
            
        case .errorWith(let message):
            return message
            
        case .needResync:
            return "Need resync"
            
        case .clientNotStarted:
            return "Client not started"
        }
    }
}
