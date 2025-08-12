//
//  BalanceView.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 13/08/25.
//

import SwiftUI

enum BalanceFormat: String, CaseIterable {
    case btc = "btc", sats = "sats"
    
    func formatted(_ value: UInt64) -> String {
        switch self {
        case .sats:
            return value.formattedSats()
            
        case .btc:
            return value.formattedBTC()
            
        }
    }
}

struct BalanceView: View {
    @Environment(\.padawanColors) private var colors
    
    @Binding private var balance: UInt64
    @Binding private var balanceFormatOption: BalanceFormat
    
    init(
        balance: Binding<UInt64> = .constant(.zero),
        balanceFormatOption: Binding<BalanceFormat> = .constant(.btc)
    ) {
        _balance = balance
        _balanceFormatOption = balanceFormatOption
    }
    
    var body: some View {
        HStack {
            Text("\(format($balance.wrappedValue))")
                .font(Fonts.font(.bold, 40))
                .foregroundStyle(colors.text)
            Text(balanceFormatOption.rawValue)
                .font(Fonts.subtitle)
                .foregroundStyle(colors.text)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .leading)
    }
    
    private func format(_ value: UInt64) -> String {
        return balanceFormatOption.formatted(value)
    }
}

#if DEBUG
#Preview("btc") {
    BalanceView(
        balance: .constant(450000)
    )
    .padding()
}

#Preview("sats") {
    BalanceView(
        balance: .constant(450000),
        balanceFormatOption: .constant(.sats)
    )
    .padding()
}
#endif
