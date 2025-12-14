/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import SwiftUI

private struct MoreRootViewAssets {
    static var title: String { LanguageManager.shared.localizedString(forKey: "settings") }
    static var subtitle: String { LanguageManager.shared.localizedString(forKey: "everything_else") }
    static var buttonRecoverPhrase: String { LanguageManager.shared.localizedString(forKey: "recovery_phrase") }
    static var buttonSendCoinBack: String { LanguageManager.shared.localizedString(forKey: "send_signet_coins_back") }
    static var buttonSelectLanguage: String { LanguageManager.shared.localizedString(forKey: "change_language") }
    static var buttonAboutPadawan: String { LanguageManager.shared.localizedString(forKey: "about_padawan") }
    static var buttonReset: String { LanguageManager.shared.localizedString(forKey: "reset_completed_chapters") }
    static var buttonResetWallet: String { LanguageManager.shared.localizedString(forKey: "reset_wallet") }
    
    static var accNavigationHint: String { LanguageManager.shared.localizedString(forKey: "accessibility_opens_screen_hint") }
    static var accResetLessonsHint: String { LanguageManager.shared.localizedString(forKey: "accessibility_reset_lessons_hint") }
    
    static func accAppVersion(_ version: String) -> String {
        let label = LanguageManager.shared.localizedString(forKey: "accessibility_app_version")
        let value = separatorWithVersion(version)
        return "\(label), \(value)"
    }
    
    static func separatorWithVersion(_ version: String) -> String {
        "\(LanguageManager.shared.localizedString(forKey: "padawan_wallet")) \(version)"
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
                        Text(MoreRootViewAssets.title)
                            .font(Fonts.title)
                        Text(MoreRootViewAssets.subtitle)
                            .font(Fonts.subtitle)
                    }
                    .foregroundStyle(colors.text)
                    .accessibilityElement(children: .combine)
                    .accessibilityAddTraits(.isHeader)
                    
                    buildButtons()
                    
                    buildSeparator()
                    
                    FilledButton(
                        title: MoreRootViewAssets.buttonReset,
                        titleColor: colors.text,
                        backgroundColor: colors.errorRed
                    ) {
                        viewModel.resetLessons()
                    }
                    .accessibilityIdentifier("settingsResetLessons")
                    .padding(.top, 12)
                    .accessibilityHint(MoreRootViewAssets.accResetLessonsHint)
                    
                    FilledButton(
                        title: MoreRootViewAssets.buttonResetWallet,
                        titleColor: colors.text,
                        backgroundColor: colors.accent3
                    ) {
                        viewModel.resetWallet()
                    }
                    .accessibilityIdentifier("settingsResetWallet")
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
                title: MoreRootViewAssets.buttonRecoverPhrase,
                icon: MoreRootViewAssets.chevronRight
            ) {
                viewModel.showRecoveryPhrase()
            }
            .accessibilityIdentifier("settingsRecoveryPhrase")
            .accessibilityHint(MoreRootViewAssets.accNavigationHint)
            
            FilledButton(
                title: MoreRootViewAssets.buttonSendCoinBack,
                icon: MoreRootViewAssets.chevronRight
            ) {
                viewModel.showSendCoinsBack()
            }
            .accessibilityIdentifier("settingsSendCoinsBack")
            .accessibilityHint(MoreRootViewAssets.accNavigationHint)
            
            FilledButton(
                title: MoreRootViewAssets.buttonSelectLanguage,
                icon: MoreRootViewAssets.chevronRight
            ) {
                viewModel.showLanguage()
            }
            .accessibilityIdentifier("settingsLanguage")
            .accessibilityHint(MoreRootViewAssets.accNavigationHint)
            
            FilledButton(
                title: MoreRootViewAssets.buttonAboutPadawan,
                icon: MoreRootViewAssets.chevronRight
            ) {
                viewModel.showAbout()
            }
            .accessibilityIdentifier("settingsAbout")
            .accessibilityHint(MoreRootViewAssets.accNavigationHint)
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
                .accessibilityHidden(true)
            Text(MoreRootViewAssets.separatorWithVersion(viewModel.version))
                .font(.footnote)
                .foregroundColor(colors.textLight)
                .accessibilityAddTraits(.isStaticText)
            Divider()
                .background(colors.textFaded)
                .accessibilityHidden(true)
        }
        .padding(.top, 16)
        .accessibilityElement(children: .ignore)
        .accessibilityLabel(MoreRootViewAssets.accAppVersion(viewModel.version))
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
