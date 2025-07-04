/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import SwiftUI

struct CoreView: View {
    @Environment(\.padawanColors) private var colors
    
    var body: some View {
        TabView {
            WalletRootView()
                .tabItem {
                    Label(String(localized: "bottom_nav_wallet"), systemImage: "bitcoinsign.square.fill")
                }

            LessonsRootView()
                .tabItem {
                    Label(String(localized: "bottom_nav_chapters"), systemImage: "graduationcap.fill")
                }

            MoreRootView()
                .tabItem {
                    Label(String(localized: "bottom_nav_settings"), systemImage: "plus.square.fill")
                }
        }
        .accentColor(colors.accent2)
        .background(colors.background)
    }
}
