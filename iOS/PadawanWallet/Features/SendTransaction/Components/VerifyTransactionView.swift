//
//  VerifyTransactionView.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 05/09/25.
//

import SwiftUI

private struct ViewAssets {
    static var screenTitle: String { LanguageManager.shared.localizedString(forKey: "confirm_transaction") }
    static var amountLabel: String { LanguageManager.shared.localizedString(forKey: "amount") }
    static var addressLabel: String { LanguageManager.shared.localizedString(forKey: "address") }
    static var taxLabel: String { LanguageManager.shared.localizedString(forKey: "total_fee") }
    static var confirmaButton: String { LanguageManager.shared.localizedString(forKey: "confirm_and_broadcast") }
    static var accBroadcastHint: String { LanguageManager.shared.localizedString(forKey: "accessibility_broadcast_hint") }
}

struct VerifyTransactionView: View {
    @Environment(\.dismiss) private var dismiss
    @Environment(\.padawanColors) private var colors
    
    private let amount: String
    private let address: String
    private let tax: String
    private let primaryAction: () -> Void
    
    init(amount: String, address: String, tax: String, primaryAction: @escaping () -> Void) {
        self.amount = amount
        self.address = address
        self.tax = tax
        self.primaryAction = primaryAction
    }
    
    var body: some View {
        BackgroundView {
            VStack(spacing: 20) {
                Text(ViewAssets.screenTitle)
                    .font(Fonts.font(.medium, 24))
                    .foregroundStyle(colors.text)
                    .padding(.top, 24)
                    .accessibilityAddTraits(.isHeader)
                
                VStack(alignment: .leading, spacing: 16) {
                    buildTitle(
                        ViewAssets.amountLabel,
                        subtitle: amount
                    )
                    
                    buildTitle(
                        ViewAssets.addressLabel,
                        subtitle: address
                    )
                    
                    buildTitle(
                        ViewAssets.taxLabel,
                        subtitle: tax
                    )
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                
                Spacer()
                
                PadawanButton(title: ViewAssets.confirmaButton) {
                    dismiss()
                    primaryAction()
                }
                .fixedSize(horizontal: false, vertical: true)
                .accessibilityHint(ViewAssets.accBroadcastHint)
            }
            .padding()
        }
        .presentationDragIndicator(.visible)
        .presentationDetents([.height(400.0)])
    }
    
    @ViewBuilder
    private func buildTitle(_ title: String, subtitle: String) -> some View {
        VStack(alignment: .leading) {
            Text(title)
                .font(Fonts.font(.medium, 20))
            Text(subtitle)
                .font(Fonts.body)
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .foregroundStyle(colors.text)
        .accessibilityElement(children: .ignore)
        .accessibilityLabel("\(title), \(subtitle)")
    }
}

#if DEBUG
#Preview {
    VerifyTransactionView(
        amount: "200000 sats",
        address: "tb1pd8jmenqpe7rz2mavfdx7uc8pj7vskxv4rl6avxlqsw2u8u7d4gfs97durt",
        tax: "400 sats",
        primaryAction: { })
}
#endif
