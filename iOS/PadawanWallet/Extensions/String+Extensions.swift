//
//  String+Extensions.swift
//  PadawanWallet
//
//  Copyright 2024 thunderbiscuit, geigerzaehler, and contributors.
//  Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE.txt file.
//

import Foundation

enum HexConvertError: Error {
    case wrongInputStringLength
    case wrongInputStringCharacters
}

extension String {
    var formattedWithSeparator: String {
        guard let number = Int(self) else { return self }
        let formatter = NumberFormatter()
        formatter.numberStyle = .decimal
        formatter.groupingSeparator = ","
        formatter.groupingSize = 3
        return formatter.string(from: NSNumber(value: number)) ?? self
    }
}

extension StringProtocol {
    
    func asHexArrayFromNonValidatedSource() -> [UInt8] {
        var startIndex = self.startIndex
        return stride(from: 0, to: count, by: 2).compactMap { _ in
            let endIndex = index(startIndex, offsetBy: 2, limitedBy: self.endIndex) ?? self.endIndex
            defer { startIndex = endIndex }
            return UInt8(self[startIndex..<endIndex], radix: 16)
        }
    }

    func asHexArray() throws -> [UInt8] {
        if count % 2 != 0 { throw HexConvertError.wrongInputStringLength }
        let characterSet = "0123456789ABCDEFabcdef"
        let wrongCharacter = first { return !characterSet.contains($0) }
        if wrongCharacter != nil { throw HexConvertError.wrongInputStringCharacters }
        return asHexArrayFromNonValidatedSource()
    }
}
