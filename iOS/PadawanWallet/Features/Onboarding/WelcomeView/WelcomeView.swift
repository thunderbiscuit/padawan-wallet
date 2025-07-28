/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import Foundation
import SwiftUI

private extension WelcomeView {
    struct WelcomeViewStrings {
        static var screenTitle: String = Strings.padawanWallet
        static var welcomeText: String = Strings.welcomeStatement
        
        static var createWalletButtonTitle: String = Strings.createAWallet
        static var importWalletButtonTitle: String = Strings.alreadyHaveAWallet
    }
}

struct WelcomeView: View {
    @Environment(\.padawanColors) private var colors
    @AppStorage("isOnboarding") private var isOnboarding: Bool = true

    private let viewModel: WelcomeViewModel
    
    init(
        viewModel: WelcomeViewModel = .init(path: .constant(.init()))
    ) {
        self.viewModel = viewModel
    }
    
    var body: some View {
        BackgroundView {
            ScrollView {
                VStack(spacing: 20) {
                    Asset.Images.padawanLogoLight.toImage
                        .resizable()
                        .frame(width: 90, height: 90)
                    
                    Text(WelcomeViewStrings.screenTitle)
                        .font(Fonts.title)
                        .foregroundStyle(colors.text)
                    
                    Text(WelcomeViewStrings.welcomeText)
                        .font(Fonts.body)
                        .foregroundStyle(colors.textFaded)
                    
                    Spacer()
                    
                    buildButtons()
                }
                .frame(minHeight: screenHeight - 90)
                .padding(.horizontal, 30)
                .padding(.bottom, 30)
            }
        }
        .navigationDestination(for: WelcomeNavigation.self) { item in
            switch item {
            case .createWallet:
                Text("Create Wallet")
                
            case .importWallet:
                ImportWalletView(viewModel: .init(path: viewModel.$path))
            }
        }
    }
    
    @ViewBuilder
    private func buildButtons() -> some View {
        VStack(spacing: 20) {
            Group {
                PadawanButton(title: WelcomeViewStrings.createWalletButtonTitle) {
                    viewModel.createWallet()
                }
                
                PadawanButton(title: WelcomeViewStrings.importWalletButtonTitle) {
                    viewModel.importWallet()
                }
            }
            .frame(width: 300)
        }
    }
}

#if DEBUG
#Preview("TatooineDesert") {
    WelcomeView()
        .environment(\.padawanColors, .tatooineDesert)
}

#Preview("VaderDark") {
    WelcomeView()
        .environment(\.padawanColors, .vaderDark)
}
#endif
