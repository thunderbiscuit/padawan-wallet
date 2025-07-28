/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import SwiftUI
import UIKit

let screenHeight = UIScreen.main.bounds.height

@main
struct PadawanWalletApp: App {
    @AppStorage("isOnboarding") var isOnboarding: Bool = true
    @State private var navigationPath = NavigationPath()

    var body: some Scene {
        WindowGroup {
            NavigationStack(path: $navigationPath) {
                if isOnboarding {
                    WelcomeView(viewModel: .init(path: $navigationPath))
                        .environment(\.padawanColors, .tatooineDesert)
                } else {
                    CoreView()
                        .environment(\.padawanColors, .tatooineDesert)
                }
            }
        }
    }
}
