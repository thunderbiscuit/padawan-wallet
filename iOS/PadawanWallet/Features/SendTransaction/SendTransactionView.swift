//
//  SendTransactionView.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 05/09/25.
//

import SwiftUI

private struct ViewAssets {
    static func navigationTitle(_ lm: LanguageManager) -> String { lm.localizedString(forKey: "send_bitcoin") }
    static func labelInputAmount(_ lm: LanguageManager) -> String { lm.localizedString(forKey: "amount") }
    static func placeHolderInputAmount(_ lm: LanguageManager) -> String { lm.localizedString(forKey: "enter_amount_sats") }
    static func labelInputAddress(_ lm: LanguageManager) -> String { lm.localizedString(forKey: "address") }
    static func placeHolderInputAddress(_ lm: LanguageManager) -> String { lm.localizedString(forKey: "enter_signet_address") }
    static func labelBalance(_ lm: LanguageManager) -> String { lm.localizedString(forKey: "balance") }
    static func labelTax(_ lm: LanguageManager) -> String { lm.localizedString(forKey: "fees_sats_vbytes") }
    static func buttonVerifyTransaction(_ lm: LanguageManager) -> String { lm.localizedString(forKey: "verify_transaction") }
    
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
                    Text("\(ViewAssets.labelBalance(languageManager)) \(viewModel.getBalance()) sats")
                        .font(Fonts.subtitle)
                        .multilineTextAlignment(.trailing)
                        .foregroundStyle(colors.textFaded)
                }
                
                buildInputTexts()
                
                buildTax()
                
                Spacer().frame(minHeight: 50)
                
                PadawanButton(
                title: ViewAssets.buttonVerifyTransaction(languageManager),
                    action: {
                        dismissKeyBoard()
                        viewModel.verifyTransaction()
                    }
                )

                .fixedSize(horizontal: false, vertical: true)
                .padding(.horizontal)
                
                Spacer()
            }
            .frame(maxWidth: .maxWidthScreen, alignment: .leading)
            .padding()
        }
        .toolbar(.hidden, for: .tabBar)
        .navigationTitle(ViewAssets.navigationTitle(languageManager))
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
                    label: ViewAssets.labelInputAmount(languageManager),
                    placeholder: ViewAssets.placeHolderInputAmount(languageManager),
                    text: $viewModel.amountValue,
                    trailingIcon: nil,
                    keyboardType: .numberPad
                )
                
                buildInputText(
                    label: ViewAssets.labelInputAddress(languageManager),
                    placeholder: ViewAssets.placeHolderInputAddress(languageManager),
                    text: $viewModel.address,
                    trailingIcon: ViewAssets.cameraIcon,
                    trailingAction: {
                        dismissKeyBoard()
                        viewModel.openCamera()
                    }
                )
            }
        }
    
    @ViewBuilder
    private func buildTax() -> some View {
        VStack(alignment: .leading, spacing: .zero) {
            buildHeader(title: ViewAssets.labelTax(languageManager))
            Spacer().frame(height: 8.0)
            Slider(
                value: $viewModel.feeRate,
                in: viewModel.feeRateRange,
                step: viewModel.feeRateStep
            )
            .accentColor(colors.accent1)
            
            Text(String(format: "%.0f", viewModel.feeRate))
                .font(Fonts.body)
                .foregroundColor(colors.textLight)
        }
        .frame(maxWidth: .infinity, alignment: .leading)
    }
    
    @ViewBuilder
    private func buildInputText(
        label: String,
        placeholder: String,
        text: Binding<String>,
        trailingIcon: Image? = nil,
        trailingAction: (() -> Void)? = nil,
        keyboardType: UIKeyboardType = .default
    ) -> some View {
        VStack(spacing: 6) {
            buildHeader(title: label)
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
                        
                        if let trailingIcon {
                            Button {
                                trailingAction?()
                            } label: {
                                trailingIcon
                                    .padding(.horizontal)
                            }
                            
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
