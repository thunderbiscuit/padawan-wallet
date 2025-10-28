/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import SwiftUI

private struct BalanceCardStrings {
    static var cardTitle: String { LanguageManager.shared.localizedString(forKey: "bitcoin_signet") }
    static var buttonSync: String { LanguageManager.shared.localizedString(forKey: "sync") }
    static var formaOptions: [String] {
        BalanceFormat.allCases.map { option in
            LanguageManager.shared.localizedString(forKey: option.rawValue.lowercased())
        }
    }
}

struct BalanceCard: View {
    @Environment(\.padawanColors) private var colors
    @EnvironmentObject private var languageManager: LanguageManager
    @State private var balanceFormatOption: BalanceFormat = .sats
    @Binding var balance: UInt64
    @Binding private var isSyncing: Bool
    let actionSyncWallet: () -> Void
    
    init(
        balance: Binding<UInt64> = .constant(.zero),
        isSyncing: Binding<Bool> = .constant(false),
        actionSyncWallet: @escaping () -> Void
    ) {
        _balance = balance
        _isSyncing = isSyncing
        self.actionSyncWallet = actionSyncWallet
    }
    
    var body: some View {
        PadawanCardView(
            backgroundColor: colors.errorRed) {
                VStack(spacing: 8.0) {
                    HStack {
                        Text(BalanceCardStrings.cardTitle)
                            .font(Fonts.body)
                            .foregroundStyle(colors.textFaded)
                        Spacer()
                        SwitchButton(
                            options: BalanceCardStrings.formaOptions,
                            currentOption: .init(get: {
                                balanceFormatOption.rawValue
                            }, set: { newValue in
                                balanceFormatOption = .init(rawValue: newValue) ?? .sats
                            })
                        )
                    }
                    .frame(maxWidth: .infinity, alignment: .topLeading)
                    
                    buildBalanceView()
                    
                    buildSyncButton()
                }
                .padding([.horizontal, .top], 20)
                .padding(.bottom, .zero)
            }
    }

    @ViewBuilder
    private func buildBalanceView() -> some View {
        BalanceView(
            balance: $balance,
            balanceFormatOption: $balanceFormatOption
        )
    }
    
    @ViewBuilder
    private func buildSyncButton() -> some View {
        HStack {
            Button {
                syncWallet()
            } label: {
                if isSyncing {
                    ProgressView()
                        .tint(colors.accent1)
                } else {
                    Text(BalanceCardStrings.buttonSync)
                        .font(Fonts.caption)
                        .foregroundStyle(.white)
                }
                
            }
            .frame(width: 130, height: 40)
            .background(.black)
            .mask {
                RoundedRectangle(
                    cornerRadius: 12,
                    style: .continuous
                )
                .padding(.bottom, -12)
            }
        }
        .frame(maxWidth: .infinity, alignment: .center)
    }
    
    private func syncWallet() {
        isSyncing = true
        actionSyncWallet()
    }
}

#if DEBUG
#Preview {
    VStack {
        BalanceCard(balance: .constant(450000), actionSyncWallet: { })
            .frame(height: 200)
            .environment(\.padawanColors, PadawanColorTheme.tatooine.colors)
    }
    .padding()
}
#endif
