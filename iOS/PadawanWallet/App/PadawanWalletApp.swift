/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import SwiftUI
import UIKit

let screenHeight = UIScreen.main.bounds.height
let isIpad = UIDevice.current.userInterfaceIdiom == .pad

@main
struct PadawanWalletApp: App {
    @EnvironmentObject var languageManager: LanguageManager
    @State private var navigationPath = NavigationPath()
    @ObservedObject var session = Session.shared
    
    init() {
        let themeChoice = session.themeChoice
        UINavigationBarAppearance.setupNavigationBar(themeChoice.colors)
        
#if DEBUG
        PadawanTestsConfiguration.setup()
#endif
        
    }
    
    var body: some Scene {
        WindowGroup {
            Group {
                if session.isOnboarding && !session.walletExists() {
                    NavigationStack(path: $navigationPath) {
                        WelcomeView(path: $navigationPath)
                    }
                } else {
                    CoreView(bdkClient: PadawanTestsConfiguration.bdkClient)
                }
            }
            .environment(\.padawanColors, session.themeChoice.colors)
            .environmentObject(LanguageManager.shared)
        }
    }
}
