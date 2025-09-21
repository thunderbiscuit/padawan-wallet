/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import SwiftUI

private struct MoreRootViewAssets {
    static var title = Strings.settings
    static var subtitle = Strings.everythingElse
    
    static var buttonRecoverPhase = Strings.recoveryPhrase
    static var buttonSendCoinBack = Strings.sendSignetCoinsBack
    static var buttonSelectLanguage = Strings.changeLanguage
    static var buttonAboutPadawan = Strings.aboutPadawan
    static var buttonReset = Strings.resetCompletedChapters
    
    static func separatorWithVersion(_ version: String) -> String {
        "\(Strings.padawanWallet) \(version)"
    }
    
    static var chevronRight = Image(systemName: "chevron.right.2")
}

struct MoreRootView: View {
    @Environment(\.padawanColors) private var colors
    @StateObject private var viewModel: MoreViewModel
    
    init(
        path: Binding<NavigationPath>,
        bdkClient: BDKClient = .live
    ) {
        _viewModel = StateObject(wrappedValue: MoreViewModel(path: path, bdkClient: bdkClient))
    }
    
    var body: some View {
        BackgroundView {
            ScrollView {
                VStack(alignment: .leading, spacing: 12.0) {
                    Group {
                        Text(MoreRootViewAssets.title)
                            .font(Fonts.title)
                        Text(MoreRootViewAssets.subtitle)
                            .font(Fonts.subtitle)
                    }
                    .foregroundStyle(colors.text)
                    
                    buildButtons()
                    
                    buildSeparator()
                    
                    FilledButton(
                        title: MoreRootViewAssets.buttonReset,
                        titleColor: colors.text,
                        backgroundColor: colors.errorRed
                    ) {
                        viewModel.resetWallet()
                    }
                    .padding(.top, 12)
                    
                    Spacer()
                }
                .frame(maxWidth: .maxWidthScreen)
                .padding()
            }
        }
    }
    
    @ViewBuilder
    private func buildButtons() -> some View {
        VStack(spacing: 20) {
            FilledButton(
                title: MoreRootViewAssets.buttonRecoverPhase,
                icon: MoreRootViewAssets.chevronRight
            ) {
                viewModel.showRecoveryPhrase()
            }
            
            FilledButton(
                title: MoreRootViewAssets.buttonSendCoinBack,
                icon: MoreRootViewAssets.chevronRight
            ) {
                viewModel.showSendCoinsBack()
            }
            
            FilledButton(
                title: MoreRootViewAssets.buttonSelectLanguage,
                icon: MoreRootViewAssets.chevronRight
            ) {
                viewModel.showLanguage()
            }
            
            FilledButton(
                title: MoreRootViewAssets.buttonAboutPadawan,
                icon: MoreRootViewAssets.chevronRight
            ) {
                viewModel.showAbout()
            }
        }
        .padding(.top)
        .navigationDestination(for: MoreScreenNavigation.self) { item in
            Group {
                switch item {
                case .recoveryPhase(let words):
                    RecoveryPhraseScreen(words: words)
                    
                case .sendCoinsBack:
                    SendCoinsBackScreen()
                    
                case .language:
                    LanguageThemeScreen()
                    
                case .about:
                    AboutPadawanScreen()
                }
            }
            .toolbar(.hidden, for: .tabBar)
        }
    }
    
    @ViewBuilder
    private func buildSeparator() -> some View {
        VStack(spacing: 4) {
            Divider()
                .background(colors.textFaded)
            Text(MoreRootViewAssets.separatorWithVersion(viewModel.version))
                .font(.footnote)
                .foregroundColor(colors.textLight)
            Divider()
                .background(colors.textFaded)
        }
        .padding(.top, 16)
    }
}

#if DEBUG
#Preview {
    MoreRootView(path: .constant(.init()))
        .environment(\.padawanColors, .tatooineDesert)
}
#endif
