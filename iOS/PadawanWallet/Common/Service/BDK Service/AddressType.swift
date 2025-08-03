//
//  AddressType.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 02/08/25.
//

import Foundation

enum AddressType: String, CaseIterable {
    case bip86 = "bip86"
    case bip84 = "bip84"

    var description: String {
        switch self {
        case .bip86: return "bip86"
        case .bip84: return "bip84"
        }
    }

    var displayName: String {
        switch self {
        case .bip86: return "BIP86 (Taproot)"
        case .bip84: return "BIP84 (SegWit)"
        }
    }

    init?(stringValue: String) {
        switch stringValue {
        case "bip86": self = .bip86
        case "bip84": self = .bip84
        default: return nil
        }
    }
}
