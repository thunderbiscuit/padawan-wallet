/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import SwiftUI
import UIKit

let screenHeight = UIScreen.main.bounds.height

@main
struct PadawanWalletApp: App {
    @State private var navigationPath = NavigationPath()
    @ObservedObject var session = Session.shared
    
    var body: some Scene {
        WindowGroup {
            NavigationStack(path: $navigationPath) {
                Group {
                    if session.isOnboarding && !session.walletExists() {
                        WelcomeView(
                            path: $navigationPath
                        )
                    } else {
                        CoreView()
                    }
                }
                .environment(\.padawanColors, .tatooineDesert)
            }
        }
    }
}
