//
//  ImportWalletView.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 28/07/25.
//

import SwiftUI

private extension ImportWalletView {
    struct ImportWalletViewtrings {
        static var screenTitle: String = Strings.recoverAWallet
        static var subtitle: String = Strings.enterYour12Words
        
        static var buttonTitle: String = Strings.recoverWallet
        
        static func wordNumber(_ number: Int) -> String {
            "Word \(number)"
        }
    }
}

struct ImportWalletView: View {
    @Environment(\.padawanColors) private var colors
    
    private let viewModel: ImporViewModel
    
    init(
        path: Binding<NavigationPath>,
        bdkClient: BDKClient = .live
    ) {
        self.viewModel = .init(path: path, bdkClient: bdkClient)
    }
    
    var body: some View {
        BackgroundView {
            ScrollView {
                VStack(alignment: .leading, spacing: 20) {
                    Text(ImportWalletViewtrings.screenTitle)
                        .font(Fonts.title)
                        .multilineTextAlignment(.leading)
                    
                    Text(ImportWalletViewtrings.subtitle)
                        .font(Fonts.subtitle)
                        .multilineTextAlignment(.leading)
                    
                    buildForm()
                    
                    Spacer().frame(height: 20)
                    
                    PadawanButton(title: ImportWalletViewtrings.buttonTitle) {
                        viewModel.importWallet()
                    }
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.horizontal, 30)
                .padding(.bottom, 30)
            }
        }
    }
    
    @ViewBuilder
    private func buildForm() -> some View {
        ForEach(0..<12) { i in
            buildTextField(
                label: ImportWalletViewtrings.wordNumber(i+1),
                placeholder: ImportWalletViewtrings.wordNumber(i+1),
                text: .init(get: {
                    viewModel.words[i]
                }, set: { value in
                    viewModel.words[i] = value
                })
            )
        }
    }
    
    @ViewBuilder
    private func buildTextField(
        label: String,
        placeholder: String,
        text: Binding<String>
    ) -> some View {
        InputTextView(
            text: text,
            label: label,
            placeholder: placeholder,
            autocorrectionDisabled: true,
            autocapitalization: .never
        )
    }
}

#if DEBUG
#Preview {
    ImportWalletView(
        path: .constant(.init()),
        bdkClient: .mock
    )
    .environment(\.padawanColors, .tatooineDesert)
}
#endif
