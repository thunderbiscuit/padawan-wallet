//
//  UInt64+extensions.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 13/08/25.
//

import Foundation

extension UInt64 {
    func formattedSats() -> String {
        formatted(.number)
    }
    
    func formattedBTC() -> String {
        if self == 0 {
            return "0.00 000 000"
        } else {
            let btcValue = Double(self) / 100_000_000.0
            let btcString = String(format: "%.8f", btcValue)
            let parts = btcString.split(separator: ".")
            guard parts.count == 2 else { return btcString }

            let wholePart = String(parts[0])
            let decimalPart = String(parts[1])

            let paddedDecimal = decimalPart.padding(toLength: 8, withPad: "0", startingAt: 0)
            let first = paddedDecimal.prefix(2)
            let second = paddedDecimal.dropFirst(2).prefix(3)
            let third = paddedDecimal.dropFirst(5).prefix(3)

            let formattedBalance = "\(wholePart).\(first) \(second) \(third)"

            return formattedBalance
        }
    }
    
    func toDate() -> Date {
        return Date(timeIntervalSince1970: TimeInterval(self))
    }
}
