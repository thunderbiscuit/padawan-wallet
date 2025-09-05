/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import Foundation
import SwiftUI

struct OnboardingPage: Identifiable {
    let id = UUID()
    let image: Image
    let title: String
    let text: String
}

private extension WelcomeView {
    struct WelcomeViewStrings {
        static var screenTitle: String = Strings.padawanWallet
        static var welcomeText: String = Strings.welcomeStatement
        static var createWalletButtonTitle: String = Strings.createAWallet
        static var importWalletButtonTitle: String = Strings.alreadyHaveAWallet
        static var screenTitlePage2: String = Strings.onboardTitle2
        static var screenTextPage2: String = Strings.onboard2
        static var screenTitlePage3: String = Strings.onboardTitle3
        static var screenTextPage3: String = Strings.onboard3
    }
}

struct WelcomeView: View {
    @Environment(\.padawanColors) private var colors
    @StateObject private var viewModel: WelcomeViewModel
    
    private let onboardingPages: [OnboardingPage] = [
        OnboardingPage(
            image: Asset.Images.padawanLogoLight.toImage,
            title: WelcomeViewStrings.screenTitle,
            text: WelcomeViewStrings.welcomeText
        ),
        OnboardingPage(
            image: Asset.Images.bitcoinIcon.toImage,
            title: WelcomeViewStrings.screenTitlePage2,
            text: WelcomeViewStrings.screenTextPage2
        ),
        OnboardingPage(
            image: Asset.Images.padawanLogoLight.toImage,
            title: WelcomeViewStrings.screenTitlePage3,
            text: WelcomeViewStrings.screenTextPage3
        ),
    ]
    
    init(
        path: Binding<NavigationPath>,
        bdkClient: BDKClient = .live
    ) {
        _viewModel = StateObject(wrappedValue: WelcomeViewModel(path: path, bdkClient: bdkClient))
    }
    
    var body: some View {
        BackgroundView {
            TabView {
                ForEach(onboardingPages) { page in
                    buildTabViewItem(page: page)
                }
                
                VStack(spacing: 60) {
                    PadawanCardIconView(image: Asset.Images.padawanLogoLight.toImage, size: 150)

                    
                    buildButtons()
                }
                .frame(maxWidth: .maxWidthScreen)
                .padding(.horizontal, 30)
                .padding(.bottom, 30)
            }
            .tabViewStyle(PageTabViewStyle(indexDisplayMode: .automatic))

        }
        .fullScreenCover(item: $viewModel.fullScreenCover) { item in
            switch item {
            case .alertError(let data):
                AlertModalView(data: data)
                    .background(BackgroundClearView())
                
            default:
                EmptyView()
            }
        }
        .sheet(item: $viewModel.sheetScreen) { item in
            switch item {
            case .importWallet:
                ImportWalletView(path: viewModel.$path)
            default:
                EmptyView()
            }
        }
        .navigationDestination(for: WelcomeScreenNavigation.self) { item in
            switch item {
            case .importWallet:
                ImportWalletView(path: viewModel.$path)
            default:
                EmptyView()
            }
        }
    }
    
    @ViewBuilder
    private func buildTabViewItem(page: OnboardingPage) -> some View {
        VStack(spacing: 8) {
            PadawanCardIconView(image: page.image, size: 120)
            
            Text(page.title)
                .font(Fonts.title)
                .foregroundStyle(colors.text)
            
            Text(page.text)
                .font(Fonts.body)
                .foregroundStyle(colors.textFaded)
                .padding()
        }
        .padding(.top, -130)
    }

    
    @ViewBuilder
    private func buildButtons() -> some View {
        VStack(spacing: 30) {
            Group {
                PadawanButton(title: WelcomeViewStrings.createWalletButtonTitle) {
                    viewModel.createWallet()
                }
                .frame(height: 105)
                
                PadawanButton(title: WelcomeViewStrings.importWalletButtonTitle) {
                    viewModel.importWallet()
                }
                .frame(height: 105)
            }
        }
    }
}

#if DEBUG
#Preview("TatooineDesert") {
    WelcomeView(path: .constant(.init()))
        .environment(\.padawanColors, .tatooineDesert)
}

#Preview("VaderDark") {
    WelcomeView(path: .constant(.init()))
        .environment(\.padawanColors, .vaderDark)
}
#endif
