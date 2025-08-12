//
//  ChainPosition+Extensions.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 14/08/25.
//

import BitcoinDevKit
import Foundation

extension ChainPosition {
    func isBefore(_ other: ChainPosition) -> Bool {
        switch (self, other) {
        case (.unconfirmed, .confirmed):
            return true
            
        case (.confirmed, .unconfirmed):
            return false
            
        case (.unconfirmed(let timestamp1), .unconfirmed(let timestamp2)):
            return (timestamp1 ?? 0) < (timestamp2 ?? 0)
            
        case (
            .confirmed(let blockTime1, let transitively1),
            .confirmed(let blockTime2, let transitively2)
        ):
            return blockTime1.blockId.height != blockTime2.blockId.height
                ? blockTime1.blockId.height > blockTime2.blockId.height
                : (transitively1 != nil) && (transitively2 == nil)
        }
    }
}

extension ChainPosition {
    static let mock = ChainPosition.unconfirmed(timestamp: nil)
}
