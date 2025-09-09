//
//  WelcomeViewModel.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 28/07/25.
//

import SwiftUI

final class WelcomeViewModel: ObservableObject {
    
    struct OnboardingPage: Identifiable {
        let id = UUID()
        let image: Image
        let title: String
        let text: String
    }
    
    struct OnboardingStrings {
        static let screenTitle = Strings.onboardTitle1
        static let welcomeText = Strings.welcomeStatement
        static let createWalletButtonTitle = Strings.createAWallet
        static let importWalletButtonTitle = Strings.alreadyHaveAWallet
        static let screenTitlePage2 = Strings.onboardTitle2
        static let screenTextPage2 = Strings.onboard2
        static let screenTitlePage3 = Strings.onboardTitle3
        static let screenTextPage3 = Strings.onboard3
    }
    
    @Binding var path: NavigationPath
    @Published var sheetScreen: WelcomeScreenNavigation?
    @Published var fullScreenCover: ImportWalletNavigation?
    @Published var onboardingPages: [OnboardingPage] = []
    
    var createWalletButtonTitle: String { OnboardingStrings.createWalletButtonTitle }
    var importWalletButtonTitle: String { OnboardingStrings.importWalletButtonTitle }
    
    private let bdkClient: BDKClient
    
    init(path: Binding<NavigationPath>, bdkClient: BDKClient) {
        _path = path
        self.bdkClient = bdkClient
        self.onboardingPages = Self.makeOnboardingPages()
    }
    
    private static func makeOnboardingPages() -> [OnboardingPage] {
        [
            OnboardingPage(
                image: Asset.Images.padawantransparent.toImage,
                title: OnboardingStrings.screenTitle,
                text: OnboardingStrings.welcomeText
            ),
            OnboardingPage(
                image: Asset.Images.bitcoinIcon.toImage,
                title: OnboardingStrings.screenTitlePage2,
                text: OnboardingStrings.screenTextPage2
            ),
            OnboardingPage(
                image: Image(systemName: "graduationcap"),
                title: OnboardingStrings.screenTitlePage3,
                text: OnboardingStrings.screenTextPage3
            )
        ]
    }
    
    // MARK: - Navigation
    func createWallet() {
        do {
            try bdkClient.createNewWallet()
            Session.shared.setNeedOnboarding(false)
        } catch let error as BDKServiceError {
            fullScreenCover = .alertError(data: .init(error: error))
        } catch {
            fullScreenCover = .alertError(data: .init(error: .generic))
        }
    }
    
    func importWallet() {
        if isIpad {
            sheetScreen = .importWallet
        } else {
            path.append(WelcomeScreenNavigation.importWallet)
        }
    }
}
