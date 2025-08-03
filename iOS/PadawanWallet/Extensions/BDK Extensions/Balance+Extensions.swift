//
//  Balance+Extensions.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 03/08/25.
//

import BitcoinDevKit
import Foundation

extension Balance: @retroactive Equatable {
    public static func == (lhs: Balance, rhs: Balance) -> Bool {
        return lhs.immature == rhs.immature && lhs.trustedPending == rhs.trustedPending
            && lhs.untrustedPending == rhs.untrustedPending && lhs.confirmed == rhs.confirmed
            && lhs.trustedSpendable == rhs.trustedSpendable && lhs.total == rhs.total
    }
    
    static var zero: Self {
        return .init(
            immature: Amount.fromSat(satoshi: .zero),
            trustedPending: Amount.fromSat(satoshi: .zero),
            untrustedPending: Amount.fromSat(satoshi: .zero),
            confirmed: Amount.fromSat(satoshi: .zero),
            trustedSpendable: Amount.fromSat(satoshi: .zero),
            total: Amount.fromSat(satoshi: .zero)
        )
    }
}

#if DEBUG
    extension Balance {
        static var mock = Self(
            immature: Amount.fromSat(satoshi: UInt64(100)),
            trustedPending: Amount.fromSat(satoshi: UInt64(200)),
            untrustedPending: Amount.fromSat(satoshi: UInt64(300)),
            confirmed: Amount.fromSat(satoshi: UInt64(21000)),
            trustedSpendable: Amount.fromSat(satoshi: UInt64(1_000_000)),
            total: Amount.fromSat(satoshi: UInt64(615_000_000))
        )
    }
#endif
