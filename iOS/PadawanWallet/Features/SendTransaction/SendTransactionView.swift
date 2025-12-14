//
//  SendTransactionView.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 05/09/25.
//

import SwiftUI

private struct ViewAssets {
    static var navigationTitle: String { LanguageManager.shared.localizedString(forKey: "send_bitcoin") }
    static var labelInputAmount: String { LanguageManager.shared.localizedString(forKey: "amount") }
    static var placeHolderInputAmount: String { LanguageManager.shared.localizedString(forKey: "enter_amount_sats") }
    static var labelInputAddress: String { LanguageManager.shared.localizedString(forKey: "address") }
    static var placeHolderInputAddress: String { LanguageManager.shared.localizedString(forKey: "enter_signet_address") }
    static var labelBalance: String { LanguageManager.shared.localizedString(forKey: "balance") }
    static var labelTax: String { LanguageManager.shared.localizedString(forKey: "fees_sats_vbytes") }
    static var buttonVerifyTransaction: String { LanguageManager.shared.localizedString(forKey: "verify_transaction") }
    
    static var accScanQRCode: String { LanguageManager.shared.localizedString(forKey: "accessibility_scan_qr_code") }
    static var accSlideTaxs: String { LanguageManager.shared.localizedString(forKey: "accessibility_slide_taxs") }
    static var accVerifyTransaction: String { LanguageManager.shared.localizedString(forKey: "accessibility_verify_transaction") }
    
    static var cameraIcon: Image = Image(systemName: "camera")
}

struct SendTransactionView: View {
    @Environment(\.padawanColors) private var colors
    @EnvironmentObject private var languageManager: LanguageManager
    @StateObject private var viewModel: SendTransactionViewModel
    
    init(
        path: Binding<NavigationPath>,
        bdkClient: BDKClient = .live
    ) {
        _viewModel = StateObject(wrappedValue: SendTransactionViewModel(path: path, bdkClient: bdkClient))
    }
    
    var body: some View {
        BackgroundView {
            VStack(spacing: 20) {
                HStack {
                    Spacer()
                    Text("\(ViewAssets.labelBalance) \(viewModel.getBalance()) sats")
                        .font(Fonts.subtitle)
                        .multilineTextAlignment(.trailing)
                        .foregroundStyle(colors.textFaded)
                        .accessibilityLabel("\(ViewAssets.labelBalance), \(viewModel.getBalance()) sats")
                        .accessibilityAddTraits(.isStaticText)
                }
                
                buildInputTexts()
                
                buildTax()
                
                Spacer().frame(minHeight: 50)
                
                PadawanButton(
                title: ViewAssets.buttonVerifyTransaction,
                    action: {
                        dismissKeyBoard()
                        viewModel.verifyTransaction()
                    }
                )
                .accessibilityIdentifier("sendVerifyButton")
                .fixedSize(horizontal: false, vertical: true)
                .padding(.horizontal)
                .accessibilityHint(Text(ViewAssets.accVerifyTransaction))
                
                Spacer()
            }
            .frame(maxWidth: .maxWidthScreen, alignment: .leading)
            .padding()
        }
        .toolbar(.hidden, for: .tabBar)
        .navigationTitle(ViewAssets.navigationTitle)
        .onTapGesture {
            dismissKeyBoard()
        }
        .fullScreenCover(item: $viewModel.fullScreenCover) { item in
            switch item {                
            case .alert(let data):
                AlertModalView(data: data)
                    .background(BackgroundClearView())
            
            case .openCamera:
                CameraScannView { address in
                    viewModel.address = address
                }
                
            default:
                EmptyView()
            }
        }
        .sheet(item: $viewModel.sheetScreen) { item in
            switch item {
            case .verifyTransaction(let amount, let address, let tax):
                VerifyTransactionView(
                    amount: amount,
                    address: address,
                    tax: tax,
                    primaryAction: {
                        viewModel.sendTransaction()
                    }
                )
                
            default:
                EmptyView()
            }
        }
    }
    
    @ViewBuilder
        private func buildInputTexts() -> some View {
            VStack(spacing: 20) {
                buildInputText(
                    label: ViewAssets.labelInputAmount,
                    placeholder: ViewAssets.placeHolderInputAmount,
                    identifier: "sendAmountInput",
                    text: $viewModel.amountValue,
                    trailingIcon: nil,
                    keyboardType: .numberPad
                )
                
                buildInputText(
                    label: ViewAssets.labelInputAddress,
                    placeholder: ViewAssets.placeHolderInputAddress,
                    identifier: "sendAddressInput",
                    text: $viewModel.address,
                    trailingIcon: ViewAssets.cameraIcon,
                    trailingAction: {
                        dismissKeyBoard()
                        viewModel.openCamera()
                    },
                    trailingAccessibilityLabel: ViewAssets.accScanQRCode
                )
            }
        }
    
    @ViewBuilder
    private func buildTax() -> some View {
        VStack(alignment: .leading, spacing: .zero) {
            buildHeader(title: ViewAssets.labelTax)
                .accessibilityHidden(true)
            Spacer().frame(height: 8.0)
            Slider(
                value: $viewModel.feeRate,
                in: viewModel.feeRateRange,
                step: viewModel.feeRateStep
            )
            .accentColor(colors.accent1)
            .accessibilityLabel(ViewAssets.labelTax)
            .accessibilityValue("\(Int(viewModel.feeRate)) \(ViewAssets.accSlideTaxs)")
            
            Text(String(format: "%.0f", viewModel.feeRate))
                .font(Fonts.body)
                .foregroundColor(colors.textLight)
                .accessibilityHidden(true)
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .accessibilityElement(children: .contain)
    }
    
    @ViewBuilder
    private func buildInputText(
        label: String,
        placeholder: String,
        identifier: String,
        text: Binding<String>,
        trailingIcon: Image? = nil,
        trailingAction: (() -> Void)? = nil,
        trailingAccessibilityLabel: String? = nil,
        keyboardType: UIKeyboardType = .default
    ) -> some View {
        VStack(spacing: 6) {
            buildHeader(title: label)
            .accessibilityAddTraits(.isStaticText)
            PadawanCardView(
                backgroundColor: colors.background2) {
                    HStack {
                        InputTextView(
                            text: text,
                            placeholder: placeholder,
                            autocorrectionDisabled: true,
                            autocapitalization: .never,
                            standardAppearance: true,
                            keyboardType: keyboardType
                        )
                        .accessibilityIdentifier(identifier)
                        .accessibilityLabel(label)
                        
                        if let trailingIcon {
                            Button {
                                trailingAction?()
                            } label: {
                                trailingIcon
                                    .padding(.horizontal)
                            }
                            .accessibilityLabel(trailingAccessibilityLabel ?? "")
                            .accessibilityRemoveTraits(.isImage)
                            
                        } else {
                            EmptyView()
                        }
                    }
                }
                .fixedSize(horizontal: false, vertical: true)
        }
    }
    
    @ViewBuilder
    private func buildHeader(title: String) -> some View {
        Text(title)
            .font(Fonts.font(.medium, 22.0))
            .foregroundStyle(colors.text)
            .frame(maxWidth: .infinity, alignment: .leading)
    }
}

#if DEBUG
#Preview {
    NavigationStack {
        SendTransactionView(
            path: .constant(.init()),
            bdkClient: .mock
        )
        .environment(\.padawanColors, PadawanColorTheme.tatooine.colors)
        .environmentObject(LanguageManager.shared)
    }
}
#endif
