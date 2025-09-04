/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import Foundation
import SwiftUI

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
    
    init(
        path: Binding<NavigationPath>,
        bdkClient: BDKClient = .live
    ) {
        _viewModel = StateObject(wrappedValue: WelcomeViewModel(path: path, bdkClient: bdkClient))
    }
    
    var body: some View {
        BackgroundView {
            TabView() {
                VStack(spacing: 8) {
                    Asset.Images.padawanLogoLight.toImage
                        .resizable()
                        .frame(width: 120, height: 120)
                    
                    Text(WelcomeViewStrings.screenTitle)
                        .font(Fonts.title)
                        .foregroundStyle(colors.text)
                    
                    Text(WelcomeViewStrings.welcomeText)
                        .font(Fonts.body)
                        .foregroundStyle(colors.textFaded)
                        .padding()
                }
                .tag(0)
                .padding(.top, -130)
                
                VStack(spacing: 5) {
                    
                    Asset.Images.bitcoinIcon.toImage
                        .resizable()
                        .frame(width: 150, height: 150)
                    
                    Text(WelcomeViewStrings.screenTitlePage2)
                        .font(Fonts.title)
                        .foregroundStyle(colors.text)
                    
                    Text(WelcomeViewStrings.screenTextPage2)
                        .font(Fonts.body)
                        .foregroundStyle(colors.textFaded)
                        .padding()
                }
                .tag(1)
                .padding(.top, -130)
                
                VStack(spacing: 5) {
                    
                    Asset.Images.bitcoinIcon.toImage
                        .resizable()
                        .frame(width: 150, height: 150)
                    
                    Text(WelcomeViewStrings.screenTitlePage3)
                        .font(Fonts.title)
                        .foregroundStyle(colors.text)
                    
                    Text(WelcomeViewStrings.screenTextPage3)
                        .font(Fonts.body)
                        .foregroundStyle(colors.textFaded)
                        .padding()
                }
                .tag(2)
                .padding(.top, -130)
                
                VStack(spacing: 60) {
                    
                    Asset.Images.padawanLogoLight.toImage
                        .resizable()
                        .frame(width: 150, height: 150)
                    
                    buildButtons()
                }
                .tag(3)
                
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
