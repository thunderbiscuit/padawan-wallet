//
//  Amount+Extensions.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 03/08/25.
//

import BitcoinDevKit
import Foundation

extension Amount: @retroactive Equatable {
    public static func == (lhs: Amount, rhs: Amount) -> Bool {
        return lhs.toSat() == rhs.toSat()
    }
}

#if DEBUG
extension Amount {
    static let mock = Amount.fromSat(satoshi: 100_000)
}
#endif
