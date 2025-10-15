/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import SwiftUI
import BitcoinUI

private struct SendCoinsBackScreenAssets {
    static func title(_ lm: LanguageManager) -> String { lm.localizedString(forKey: "send_signet_coins_back") }
    static func sendCoinsBackAddress(_ lm: LanguageManager) -> String { lm.localizedString(forKey: "send_coins_back_address") }
    static func text(_ lm: LanguageManager) -> String { lm.localizedString(forKey: "send_coins_back") }
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
                    
                    Text(SendCoinsBackScreenAssets.text(languageManager))
                        .font(Fonts.font(.regular, 16))
                        .foregroundStyle(colors.text)
                        .padding(.horizontal)
                }
                .padding()
            }
        }
        .navigationTitle(SendCoinsBackScreenAssets.title(languageManager))
    }
    
    @ViewBuilder
    private func buildCopyButton() -> some View {
        Button {
            copyAddress()
        } label: {
            HStack(spacing: 12.0) {
                AddressFormattedView(
                    address: SendCoinsBackScreenAssets.sendCoinsBackAddress(languageManager),
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
    }
    
    @ViewBuilder
    private func buildQRCode() -> some View {
        ZStack {
            RoundedRectangle(cornerRadius: 12)
                .fill(colors.background2)
            Button {
                copyAddress()
            } label: {
                QRCodeView(qrCodeType: .bitcoin(SendCoinsBackScreenAssets.sendCoinsBackAddress(languageManager)))
                    .padding()
            }
        }
        .frame(width: 320, height: 320)
    }
    
    private func copyAddress() {
        UIPasteboard.general.string = SendCoinsBackScreenAssets.sendCoinsBackAddress(languageManager)
        copiedAddress = true
        DispatchQueue.main.asyncAfter(deadline: .now() + 3) {
            copiedAddress = false
        }
    }
}
