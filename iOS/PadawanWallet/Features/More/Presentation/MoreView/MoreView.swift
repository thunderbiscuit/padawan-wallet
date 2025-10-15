/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import SwiftUI

private struct MoreRootViewAssets {
    static func title(_ lm: LanguageManager) -> String { lm.localizedString(forKey: "settings") }
    static func subtitle(_ lm: LanguageManager) -> String { lm.localizedString(forKey: "everything_else") }
    
    static func buttonRecoverPhase(_ lm: LanguageManager) -> String { lm.localizedString(forKey: "recovery_phrase") }
    static func buttonSendCoinBack(_ lm: LanguageManager) -> String { lm.localizedString(forKey: "send_signet_coins_back") }
    static func buttonSelectLanguage(_ lm: LanguageManager) -> String { lm.localizedString(forKey: "change_language") }
    static func buttonAboutPadawan(_ lm: LanguageManager) -> String { lm.localizedString(forKey: "about_padawan") }
    static func buttonReset(_ lm: LanguageManager) -> String { lm.localizedString(forKey: "reset_completed_chapters") }
    static func buttonResetWallet(_ lm: LanguageManager) -> String { lm.localizedString(forKey: "reset_wallet") }
    
    static func separatorWithVersion(_ lm: LanguageManager, _ version: String) -> String {
        "\(lm.localizedString(forKey: "padawan_wallet")) \(version)"
    }
    
    static var chevronRight = Image(systemName: "chevron.right.2")
}


struct MoreRootView: View {
    @Environment(\.padawanColors) private var colors
    @EnvironmentObject private var languageManager: LanguageManager
    
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
                        Text(MoreRootViewAssets.title(languageManager))
                            .font(Fonts.title)
                        Text(MoreRootViewAssets.subtitle(languageManager))
                            .font(Fonts.subtitle)
                    }
                    .foregroundStyle(colors.text)
                    
                    buildButtons()
                    
                    buildSeparator()
                    
                    FilledButton(
                        title: MoreRootViewAssets.buttonReset(languageManager),
                        titleColor: colors.text,
                        backgroundColor: colors.errorRed
                    ) {
                        viewModel.resetLessons()
                    }
                    .padding(.top, 12)
                    
                    FilledButton(
                        title: MoreRootViewAssets.buttonResetWallet(languageManager),
                        titleColor: colors.text,
                        backgroundColor: colors.accent3
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
                title: MoreRootViewAssets.buttonRecoverPhase(languageManager),
                icon: MoreRootViewAssets.chevronRight
            ) {
                viewModel.showRecoveryPhrase()
            }
            
            FilledButton(
                title: MoreRootViewAssets.buttonSendCoinBack(languageManager),
                icon: MoreRootViewAssets.chevronRight
            ) {
                viewModel.showSendCoinsBack()
            }
            
            FilledButton(
                title: MoreRootViewAssets.buttonSelectLanguage(languageManager),
                icon: MoreRootViewAssets.chevronRight
            ) {
                viewModel.showLanguage()
            }
            
            FilledButton(
                title: MoreRootViewAssets.buttonAboutPadawan(languageManager),
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
                
                case .alert(let data):
                    AlertModalView(data: data)
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
            Text(MoreRootViewAssets.separatorWithVersion(languageManager, viewModel.version))
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
    NavigationStack {
        MoreRootView(path: .constant(.init()))
            .environment(\.padawanColors, .tatooineDesert)
            .environmentObject(LanguageManager.shared)
    }
}
#endif
