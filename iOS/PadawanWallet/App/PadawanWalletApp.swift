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
    @State private var navigationPath = NavigationPath()
    @ObservedObject var session = Session.shared
    
    init() {
        UINavigationBarAppearance.setupNavigationBar(.tatooineDesert)
    }
    
    var body: some Scene {
        WindowGroup {
            Group {
                if session.isOnboarding && !session.walletExists() {
                    NavigationStack(path: $navigationPath) {
                        WelcomeView(
                            path: $navigationPath
                        )
                    }
                } else {
                    CoreView()
                }
            }
            .environment(\.padawanColors, session.themeChoice.colors)
        }
    }
}

extension UINavigationBarAppearance {
    static func setupNavigationBar(_ color: PadawanColors) {
        let font = Fonts.uiKitFont(.medium, 18)
        let textColor = UIColor(color.textFaded)

        let appearance = UINavigationBarAppearance()
        appearance.configureWithTransparentBackground()
        appearance.backgroundColor = .clear
        appearance.titleTextAttributes = [
            .foregroundColor: textColor,
            .font: font
        ]
        appearance.largeTitleTextAttributes = [
            .foregroundColor: textColor,
            .font: font
        ]
        
        let backButtonAppearance = UIBarButtonItemAppearance()
        backButtonAppearance.normal.titleTextAttributes = [
            .foregroundColor: textColor,
            .font: font
        ]
        appearance.backButtonAppearance = backButtonAppearance
        
        UINavigationBar.appearance().tintColor = textColor
        UIBarButtonItem.appearance().tintColor = textColor
        
        UINavigationBar.appearance().standardAppearance = appearance
        UINavigationBar.appearance().scrollEdgeAppearance = appearance
        UINavigationBar.appearance().compactAppearance = appearance
    }
}
