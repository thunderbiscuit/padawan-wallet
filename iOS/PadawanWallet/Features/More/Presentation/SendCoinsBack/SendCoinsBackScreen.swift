/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import SwiftUI
import BitcoinUI

private struct SendCoinsBackScreenAssets {
    static var title: String { LanguageManager.shared.localizedString(forKey: "send_signet_coins_back") }
    static var sendCoinsBackAddress: String { LanguageManager.shared.localizedString(forKey: "send_coins_back_address") }
    static var text: String { LanguageManager.shared.localizedString(forKey: "send_coins_back") }
    
    static var accQRCodeLabel: String { LanguageManager.shared.localizedString(forKey: "accessibility_qr_code_return_label") }
    static var accCopyHint: String { LanguageManager.shared.localizedString(forKey: "accessibility_copy_address_hint") }
    static var accAddressCopied: String { LanguageManager.shared.localizedString(forKey: "accessibility_return_address_copied") }
    static var accAddressLabel: String { LanguageManager.shared.localizedString(forKey: "accessibility_return_address") }
    
    static var copyIcon = Image(systemName: "document.on.document")
    static var copiedIcon = Image(systemName: "checkmark.circle.fill")
}

struct SendCoinsBackScreen: View {
    @State private var copiedAddress = false
    @Environment(\.padawanColors) private var colors
    @EnvironmentObject private var languageManager: LanguageManager
    
    var body: some View {
        BackgroundView {
            ScrollView {
                VStack(spacing: 20) {
                    buildQRCode()
                    
                    buildCopyButton()
                    
                    Text(SendCoinsBackScreenAssets.text)
                        .font(Fonts.font(.regular, 16))
                        .foregroundStyle(colors.text)
                        .padding(.horizontal)
                }
                .padding()
            }
        }
        .navigationTitle(SendCoinsBackScreenAssets.title)
    }
    
    @ViewBuilder
    private func buildCopyButton() -> some View {
        Button {
            copyAddress()
        } label: {
            HStack(spacing: 12.0) {
                AddressFormattedView(
                    address: SendCoinsBackScreenAssets.sendCoinsBackAddress,
                    columns: 4,
                    spacing: 2.0,
                    gridItemSize: 60
                )
                .font(Fonts.font(.regular, 18))

                if copiedAddress {
                    SendCoinsBackScreenAssets.copiedIcon
                        .foregroundStyle(.green)
                } else {
                    SendCoinsBackScreenAssets.copyIcon
                }
            }
        }
        .accessibilityIdentifier("sendCoinsBackCopyBtn")
        .accessibilityLabel(SendCoinsBackScreenAssets.accAddressLabel)
        .accessibilityHint(SendCoinsBackScreenAssets.accCopyHint)
    }
    
    @ViewBuilder
    private func buildQRCode() -> some View {
        ZStack {
            RoundedRectangle(cornerRadius: 12)
                .fill(colors.background2)
            Button {
                copyAddress()
            } label: {
                QRCodeView(qrCodeType: .bitcoin(SendCoinsBackScreenAssets.sendCoinsBackAddress))
                    .padding()
            }
            .accessibilityLabel(SendCoinsBackScreenAssets.accQRCodeLabel)
            .accessibilityHint(SendCoinsBackScreenAssets.accCopyHint)
            .accessibilityRemoveTraits(.isImage)
            .accessibilityAddTraits(.isButton)
        }
        .frame(width: 320, height: 320)
        .accessibilityElement(children: .contain)
    }
    
    private func copyAddress() {
        UIPasteboard.general.string = SendCoinsBackScreenAssets.sendCoinsBackAddress
        copiedAddress = true
        UIAccessibility.post(notification: .announcement, argument: SendCoinsBackScreenAssets.accAddressCopied)
        DispatchQueue.main.asyncAfter(deadline: .now() + 3) {
            copiedAddress = false
        }
    }
}
