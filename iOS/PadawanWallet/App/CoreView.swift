/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import SwiftUI

struct CoreView: View {
    @Environment(\.padawanColors) private var colors
    
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
                    Text(Strings.bottomNavWallet)
                } icon: {
                    Image(systemName: "bitcoinsign.square")
                }
            }

            NavigationStack(path: $lessonPath) {
                LessonsRootView()
            }
            .tabItem {
                Label {
                    Text(Strings.bottomNavChapters)
                } icon: {
                    Image(systemName: "graduationcap")
                }
            }

            NavigationStack(path: $morePath) {
                MoreRootView()
            }
            .tabItem {
                Label {
                    Text(Strings.bottomNavSettings)
                } icon: {
                    Image(systemName: "ellipsis")
                }
            }
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
