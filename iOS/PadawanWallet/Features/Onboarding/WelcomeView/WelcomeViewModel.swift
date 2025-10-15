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
        let titleKey: String
        let textKey: String
    }
    
    struct OnboardingKeys {
        static let screenTitle = "onboard_title_1"
        static let welcomeText = "onboard_1"
        static let createWalletButtonTitle = "create_a_wallet"
        static let importWalletButtonTitle = "already_have_a_wallet"
        static let screenTitlePage2 = "onboard_title_2"
        static let screenTextPage2 = "onboard_2"
        static let screenTitlePage3 = "onboard_title_3"
        static let screenTextPage3 = "onboard_3"
    }
    
    @Binding var path: NavigationPath
    @Published var sheetScreen: WelcomeScreenNavigation?
    @Published var fullScreenCover: ImportWalletNavigation?
    @Published var onboardingPages: [OnboardingPage] = []
    
    var createWalletButtonTitleKey: String { OnboardingKeys.createWalletButtonTitle }
    var importWalletButtonTitleKey: String { OnboardingKeys.importWalletButtonTitle }
    
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
                titleKey: OnboardingKeys.screenTitle,
                textKey: OnboardingKeys.welcomeText
            ),
            OnboardingPage(
                image: Asset.Images.bitcoinicon1.toImage,
                titleKey: OnboardingKeys.screenTitlePage2,
                textKey: OnboardingKeys.screenTextPage2
            ),
            OnboardingPage(
                image: Image(systemName: "graduationcap"),
                titleKey: OnboardingKeys.screenTitlePage3,
                textKey: OnboardingKeys.screenTextPage3
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
