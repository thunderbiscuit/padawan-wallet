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
            ScrollView {
                VStack(spacing: 20) {
                    Asset.Images.padawanLogoLight.toImage
                        .resizable()
                        .frame(width: 90, height: 90)
                    
                    Text(WelcomeViewStrings.screenTitle)
                        .font(Fonts.title)
                        .foregroundStyle(colors.text)
                    
                    Text(WelcomeViewStrings.welcomeText)
                        .font(Fonts.body)
                        .foregroundStyle(colors.textFaded)
                    
                    Spacer().frame(minHeight: 90)
                    
                    buildButtons()
                }
                .frame(maxWidth: .maxWidthScreen)
                .padding(.horizontal, 30)
                .padding(.bottom, 30)
            }
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
        VStack(spacing: 20) {
            Group {
                PadawanButton(title: WelcomeViewStrings.createWalletButtonTitle) {
                    viewModel.createWallet()
                }
                
                PadawanButton(title: WelcomeViewStrings.importWalletButtonTitle) {
                    viewModel.importWallet()
                }
            }
            .frame(minWidth: 300, maxWidth: .maxWidthScreen)
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
