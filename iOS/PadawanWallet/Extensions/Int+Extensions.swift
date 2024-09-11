//
//  Int+Extensions.swift
//  PadawanWallet
//
//  Copyright 2024 thunderbiscuit, geigerzaehler, and contributors.
//  Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE.txt file.
//

import Foundation

extension UInt32 {
    private static var numberFormatter: NumberFormatter = {
        let numberFormatter = NumberFormatter()
        numberFormatter.numberStyle = .decimal

        return numberFormatter
    }()

    var delimiter: String {
        return UInt32.numberFormatter.string(from: NSNumber(value: self)) ?? ""
    }
}

extension UInt64 {
    private static var numberFormatter: NumberFormatter = {
        let numberFormatter = NumberFormatter()
        numberFormatter.numberStyle = .decimal

        return numberFormatter
    }()

    var delimiter: String {
        return UInt64.numberFormatter.string(from: NSNumber(value: self)) ?? ""
    }
}

extension UInt64 {
    func formattedSatoshis() -> String {
        if self == 0 {
            return "0.00 000 000"
        } else {
            let balanceString = String(format: "%010d", self)

            let zero = balanceString.prefix(2)
            let first = balanceString.dropFirst(2).prefix(2)
            let second = balanceString.dropFirst(4).prefix(3)
            let third = balanceString.dropFirst(7).prefix(3)

            var formattedZero = zero

            if zero == "00" {
                formattedZero = zero.dropFirst()
            } else if zero.hasPrefix("0") {
                formattedZero = zero.suffix(1)
            }

            let formattedBalance = "\(formattedZero).\(first) \(second) \(third)"

            return formattedBalance
        }
    }
}

extension Int {
    func newDateAgo() -> String {
        let date = Date(timeIntervalSince1970: TimeInterval(self))
        let formatter = RelativeDateTimeFormatter()
        formatter.unitsStyle = .full
        let relativeDate = formatter.localizedString(for: date, relativeTo: Date.now)

        return relativeDate
    }
}

extension UInt64 {
    func toDate() -> Date {
        return Date(timeIntervalSince1970: TimeInterval(self))
    }
}




