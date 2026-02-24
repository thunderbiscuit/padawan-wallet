/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import SwiftUI

struct CoreView: View {
    @Environment(\.padawanColors) private var colors
    @EnvironmentObject private var languageManager: LanguageManager
    @ObservedObject private var session = Session.shared
    @State private var walletPath: NavigationPath = .init()
    @State private var lessonPath: NavigationPath = .init()
    @State private var morePath: NavigationPath = .init()
    private let bdkClient: BDKClient
    
    init(
        bdkClient: BDKClient = .live
    ) {
        self.bdkClient = bdkClient
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
        .onAppear {
            updateAppearance(with: session.themeChoice.colors)
        }
        .onChange(of: session.themeChoice) { newValue in
            updateAppearance(with: newValue.colors)
        }
        .accentColor(colors.accent3)
    }
    
    private func updateAppearance(with colors: PadawanColors) {
        // Update the global appearance
        UITabBarAppearance.setupTabBarAppearance(colors: colors)
        UINavigationBarAppearance.setupNavigationBar(colors)
        
        // Force update all existing windows
        DispatchQueue.main.async {
            for window in UIApplication.shared.connectedScenes
                .compactMap({ $0 as? UIWindowScene })
                .flatMap({ $0.windows }) {
                
                // Update TabBar instances
                if let tabBar = window.rootViewController?.findTabBar() {
                    let standardAppearance = UITabBarAppearance()
                    standardAppearance.configureWithDefaultBackground()
                    standardAppearance.backgroundEffect = UIBlurEffect(style: colors == PadawanColors.vaderDark ? .dark : .light)
                    standardAppearance.backgroundColor = .clear
                    standardAppearance.shadowColor = .clear
                    
                    let font = Fonts.uiKitFont(.medium, 16)
                    let textColor = UIColor(colors.text)
                    let attributes: [NSAttributedString.Key: Any] = [
                        .font: font,
                        .foregroundColor: textColor
                    ]
                    standardAppearance.stackedLayoutAppearance.normal.titleTextAttributes = attributes
                    standardAppearance.stackedLayoutAppearance.normal.iconColor = textColor
                    
                    let scrollEdgeAppearance = UITabBarAppearance()
                    scrollEdgeAppearance.configureWithTransparentBackground()
                    scrollEdgeAppearance.backgroundColor = .clear
                    scrollEdgeAppearance.shadowColor = .clear
                    scrollEdgeAppearance.stackedLayoutAppearance.normal.titleTextAttributes = attributes
                    scrollEdgeAppearance.stackedLayoutAppearance.normal.iconColor = textColor
                    
                    tabBar.standardAppearance = standardAppearance
                    tabBar.scrollEdgeAppearance = scrollEdgeAppearance
                }
                
                // Update NavigationBar instances
                window.rootViewController?.findNavigationBars().forEach { navBar in
                    let font = Fonts.uiKitFont(.medium, 18)
                    let textColor = UIColor(colors.text)
                    
                    let standardAppearance = UINavigationBarAppearance()
                    standardAppearance.configureWithDefaultBackground()
                    standardAppearance.backgroundEffect = UIBlurEffect(style: colors == .vaderDark ? .dark : .light)
                    standardAppearance.backgroundColor = UIColor.clear
                    standardAppearance.shadowColor = .clear
                    standardAppearance.titleTextAttributes = [
                        .foregroundColor: textColor,
                        .font: font
                    ]
                    
                    standardAppearance.backButtonAppearance.normal.titleTextAttributes = [
                        .foregroundColor: textColor
                    ]
                    
                    let backButtonImage = UIImage(systemName: "chevron.backward")?.withTintColor(textColor, renderingMode: .alwaysOriginal)
                    standardAppearance.setBackIndicatorImage(backButtonImage, transitionMaskImage: backButtonImage)
                    
                    let scrollEdgeAppearance = UINavigationBarAppearance()
                    scrollEdgeAppearance.configureWithTransparentBackground()
                    scrollEdgeAppearance.backgroundColor = .clear
                    scrollEdgeAppearance.shadowColor = .clear
                    scrollEdgeAppearance.titleTextAttributes = [
                        .foregroundColor: textColor,
                        .font: font
                    ]
                    
                    scrollEdgeAppearance.backButtonAppearance.normal.titleTextAttributes = [
                        .foregroundColor: textColor
                    ]
                    scrollEdgeAppearance.setBackIndicatorImage(backButtonImage, transitionMaskImage: backButtonImage)
                    
                    navBar.standardAppearance = standardAppearance
                    navBar.scrollEdgeAppearance = scrollEdgeAppearance
                    navBar.compactAppearance = standardAppearance
                    navBar.tintColor = textColor
                    navBar.barStyle = colors == .vaderDark ? .black : .default
                }
            }
        }
    }
}

extension UITabBarAppearance {
    static func setupTabBarAppearance(colors: PadawanColors) {
        let font = Fonts.uiKitFont(.medium, 16)
        let textColor = UIColor(colors.text)
        let attributes: [NSAttributedString.Key: Any] = [
            .font: font,
            .foregroundColor: textColor
        ]
        
        let scrollEdgeAppearance = UITabBarAppearance()
        scrollEdgeAppearance.configureWithTransparentBackground()
        scrollEdgeAppearance.backgroundColor = .clear
        scrollEdgeAppearance.shadowColor = .clear
        scrollEdgeAppearance.stackedLayoutAppearance.normal.titleTextAttributes = attributes
        scrollEdgeAppearance.stackedLayoutAppearance.normal.iconColor = textColor
        
        let standardAppearance = UITabBarAppearance()
        standardAppearance.configureWithDefaultBackground()
        standardAppearance.backgroundEffect = UIBlurEffect(style: colors == PadawanColors.vaderDark ? .dark : .light)
        standardAppearance.backgroundColor = .clear
        standardAppearance.shadowColor = .clear
        standardAppearance.stackedLayoutAppearance.normal.titleTextAttributes = attributes
        standardAppearance.stackedLayoutAppearance.normal.iconColor = textColor
        
        UITabBar.appearance().standardAppearance = standardAppearance
        UITabBar.appearance().scrollEdgeAppearance = scrollEdgeAppearance
    }
}

extension UINavigationBarAppearance {
    static func setupNavigationBar(_ color: PadawanColors) {
        let font = Fonts.uiKitFont(.medium, 18)
        let textColor = UIColor(color.text)
        
        let scrollEdgeAppearance = UINavigationBarAppearance()
        scrollEdgeAppearance.configureWithTransparentBackground()
        scrollEdgeAppearance.backgroundColor = .clear
        scrollEdgeAppearance.shadowColor = .clear
        scrollEdgeAppearance.titleTextAttributes = [
            .foregroundColor: textColor,
            .font: font
        ]
        
        // Configurar cor do back button no scroll edge
        scrollEdgeAppearance.backButtonAppearance.normal.titleTextAttributes = [
            .foregroundColor: textColor
        ]
        let backButtonImage = UIImage(systemName: "chevron.backward")?.withTintColor(textColor, renderingMode: .alwaysOriginal)
        scrollEdgeAppearance.setBackIndicatorImage(backButtonImage, transitionMaskImage: backButtonImage)
        
        let standardAppearance = UINavigationBarAppearance()
        standardAppearance.configureWithDefaultBackground()
        standardAppearance.backgroundEffect = UIBlurEffect(style: color == .vaderDark ? .dark : .light)
        standardAppearance.backgroundColor = UIColor.clear
        standardAppearance.shadowColor = .clear
        standardAppearance.titleTextAttributes = [
            .foregroundColor: textColor,
            .font: font
        ]
        
        // Configurar cor do back button no standard
        standardAppearance.backButtonAppearance.normal.titleTextAttributes = [
            .foregroundColor: textColor
        ]
        standardAppearance.setBackIndicatorImage(backButtonImage, transitionMaskImage: backButtonImage)
        
        UINavigationBar.appearance().standardAppearance = standardAppearance
        UINavigationBar.appearance().scrollEdgeAppearance = scrollEdgeAppearance
        UINavigationBar.appearance().compactAppearance = standardAppearance
        UINavigationBar.appearance().tintColor = textColor
        UIBarButtonItem.appearance().tintColor = textColor
    }
}

// Helper extensions to find UIKit components in the view hierarchy
extension UIViewController {
    func findTabBar() -> UITabBar? {
        if let tabBarController = self as? UITabBarController {
            return tabBarController.tabBar
        }
        
        for child in children {
            if let tabBar = child.findTabBar() {
                return tabBar
            }
        }
        
        return nil
    }
    
    func findNavigationBars() -> [UINavigationBar] {
        var navigationBars: [UINavigationBar] = []
        
        if let navController = self as? UINavigationController {
            navigationBars.append(navController.navigationBar)
        }
        
        for child in children {
            navigationBars.append(contentsOf: child.findNavigationBars())
        }
        
        return navigationBars
    }
}


