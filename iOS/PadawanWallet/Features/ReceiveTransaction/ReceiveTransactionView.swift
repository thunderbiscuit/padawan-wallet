//
//  ReceiveTransactionView.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 05/09/25.
//

import BitcoinUI
import SwiftUI

private struct ViewAssets {
    static var navigationTitle: String { LanguageManager.shared.localizedString(forKey: "receive_bitcoin") }
    static var button: String { LanguageManager.shared.localizedString(forKey: "generate_a_new_address") }
    
    static var copyIcon = Image(systemName: "document.on.document")
    
    static var accQrCodeLabel: String { LanguageManager.shared.localizedString(forKey: "accessibility_qr_code_label") }
    static var accQrCodeHint: String { LanguageManager.shared.localizedString(forKey: "accessibility_qr_code_hint") }
    static var accAddressLabel: String { LanguageManager.shared.localizedString(forKey: "accessibility_address_label") }
    static var accAddressCopied: String { LanguageManager.shared.localizedString(forKey: "accessibility_address_copied") }
    static var accStartsWith: String { LanguageManager.shared.localizedString(forKey: "accessibility_starts_with") }
    static var accEndsWith: String { LanguageManager.shared.localizedString(forKey: "accessibility_ends_with") }
}

struct ReceiveTransactionView: View {
    @Environment(\.padawanColors) private var colors
    @EnvironmentObject private var languageManager: LanguageManager
    @StateObject private var viewModel: ReceiveTransactionViewModel
    
    init(
        path: Binding<NavigationPath>,
        bdkClient: BDKClient = .live
    ) {
        _viewModel = StateObject(wrappedValue: ReceiveTransactionViewModel(path: path, bdkClient: bdkClient))
    }
    
    var body: some View {
        BackgroundView {
            VStack(spacing: 12.0) {
                Spacer()
                buildQRCode()
                
                if let address = viewModel.address, !address.isEmpty {
                    Button {
                        copyToClipboard(address)
                    } label: {
                        HStack(spacing: 12.0) {
                            
                            AddressFormattedView(
                                address: address,
                                columns: 4,
                                spacing: 2.0,
                                gridItemSize: 60
                            )
                            .font(Fonts.font(.regular, 18))

                            ViewAssets.copyIcon
                        }
                    }
                    .accessibilityLabel(ViewAssets.accAddressLabel)
                    .accessibilityValue(getAccessibleAddressSummary(address))
                    .accessibilityHint(ViewAssets.accQrCodeHint)
                    .accessibilityAddTraits(.isButton)
                }
                
                Spacer()
                PadawanButton(title: ViewAssets.button) {
                                    viewModel.generateAddress()
                }
                .fixedSize(horizontal: false, vertical: true)
            }
            .frame(maxWidth: .maxWidthScreen)
            .padding()
        }
        .toolbar(.hidden, for: .tabBar)
        .navigationTitle(ViewAssets.navigationTitle)
    }
    
    @ViewBuilder
    private func buildQRCode() -> some View {
        if let address = viewModel.address, !address.isEmpty {
            ZStack {
                RoundedRectangle(cornerRadius: 12)
                    .fill(colors.background2)
                Button {
                    copyToClipboard(address)
                } label: {
                    QRCodeView(qrCodeType: .bitcoin(address))
                        .padding()
                }
                .accessibilityElement(children: .combine)
                .accessibilityLabel(ViewAssets.accQrCodeLabel)
                .accessibilityHint(ViewAssets.accQrCodeHint)
                .accessibilityAddTraits(.isImage)
                .accessibilityAddTraits(.isButton)
            }
            .frame(width: 320, height: 320)
        } else {
            EmptyView()
        }
    }
    private func copyToClipboard(_ address: String) {
        viewModel.copyAddress()
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
            UIAccessibility.post(notification: .announcement, argument: ViewAssets.accAddressCopied)
        }
    }
    
    private func getAccessibleAddressSummary(_ address: String) -> String {
        guard address.count > 12 else { return address }
        let prefix = address.prefix(6)
        let suffix = address.suffix(6)
        let startText = ViewAssets.accStartsWith
        let endText = ViewAssets.accEndsWith
        return "\(startText) \(prefix), \(endText) \(suffix)"
    }
}
