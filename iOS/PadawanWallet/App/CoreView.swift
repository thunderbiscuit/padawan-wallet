/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import SwiftUI

struct CoreView: View {
    @Environment(\.padawanColors) private var colors
    @EnvironmentObject private var languageManager: LanguageManager
    @State private var walletPath: NavigationPath = .init()
    @State private var lessonPath: NavigationPath = .init()
    @State private var morePath: NavigationPath = .init()
    private let bdkClient: BDKClient
    
    init(
        bdkClient: BDKClient = .live
    ) {
        self.bdkClient = bdkClient
        UITabBarAppearance.setupTabBarAppearance(colors: colors)
    }
    
    var body: some View {
        TabView {
            NavigationStack(path: $walletPath) {
                WalletView(
                    path: $walletPath,
                    bdkClient: bdkClient
                )
            }
            .tabItem {
                Label {
                    Text(languageManager.localizedString(forKey: "bottom_nav_wallet"))
                } icon: {
                    Image(systemName: "bitcoinsign.square")
                }
            }

            .accessibilityIdentifier("tabBarWallet")
            NavigationStack(path: $lessonPath) {
                LessonsListView(
                    path: $lessonPath
                )
            }
            .tabItem {
                Label {
                    Text(languageManager.localizedString(forKey: "bottom_nav_chapters"))
                } icon: {
                    Image(systemName: "graduationcap")
                }
            }

            .accessibilityIdentifier("tabBarLessons")

            NavigationStack(path: $morePath) {
                MoreRootView(
                    path: $morePath,
                    bdkClient: bdkClient
                )
            }
            .tabItem {
                Label {
                    Text(languageManager.localizedString(forKey: "bottom_nav_settings"))
                } icon: {
                    Image(systemName: "ellipsis")
                }
            }
            .accessibilityIdentifier("tabBarSettings")
        }
        .accentColor(colors.accent3)
    }
}

private extension UITabBarAppearance {
    static func setupTabBarAppearance(colors: PadawanColors) {
        let font = Fonts.uiKitFont(.medium, 16)
        let attributes: [NSAttributedString.Key: Any] = [
            .font: font
        ]
        UITabBarItem.appearance().setTitleTextAttributes(attributes, for: .normal)
    }
}
