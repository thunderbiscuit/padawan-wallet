/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import SwiftUI

struct ScaleButtonStyle: ButtonStyle {
    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .scaleEffect(configuration.isPressed ? 0.85 : 1.0)
            .animation(.spring(response: 0.2, dampingFraction: 0.6), value: configuration.isPressed)
    }
}

struct WelcomeView: View {
    @Environment(\.padawanColors) private var colors
    @EnvironmentObject private var languageManager: LanguageManager
    @StateObject private var viewModel: WelcomeViewModel
    @State private var currentPage = 0
    
    init(path: Binding<NavigationPath>, bdkClient: BDKClient = .live) {
        _viewModel = StateObject(wrappedValue: WelcomeViewModel(path: path, bdkClient: bdkClient))
    }
    
    var body: some View {
        BackgroundView {
            VStack {
                TabView(selection: $currentPage) {
                    ForEach(Array(viewModel.onboardingPages.enumerated()), id: \.1.id) { index, page in
                        buildTabViewItem(page: page)
                            .tag(index)
                    }
                    
                    VStack(spacing: 60) {
                        PadawanCardIconView(image: Asset.Images.padawantransparent.toImage, size: 150, hasBorder: false)
                        buildButtons()
                    }
                    .frame(maxWidth: .maxWidthScreen)
                    .padding(.horizontal, 30)
                    .padding(.bottom, 30)
                    .tag(viewModel.onboardingPages.count)
                }
                .tabViewStyle(PageTabViewStyle(indexDisplayMode: .never))
                
                buildNavigationControls()
                    .padding(.vertical)
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
    private func buildTabViewItem(page: WelcomeViewModel.OnboardingPage) -> some View {
        VStack(spacing: 20) {
            Spacer()
            
            PadawanCardIconView(image: page.image, size: 100)

            Text(languageManager.localizedString(forKey: page.titleKey))
                .font(Fonts.title)
                .foregroundStyle(colors.text)
                .padding(.top, 20)

            Text(languageManager.localizedString(forKey: page.textKey))
                .font(Fonts.body)
                .foregroundStyle(colors.textFaded)
                .multilineTextAlignment(.center)
                .padding(.horizontal)

            Spacer()
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)

    }
    
    @ViewBuilder
    private func buildButtons() -> some View {
        VStack(spacing: 30) {
            PadawanButton(title: languageManager.localizedString(forKey: viewModel.createWalletButtonTitleKey)) {
                            viewModel.createWallet()
                        }
            .frame(height: 105)
            
            PadawanButton(title: languageManager.localizedString(forKey: viewModel.importWalletButtonTitleKey)) {
                            viewModel.importWallet()
                        }
            .frame(height: 105)
        }
    }
    
    @ViewBuilder
    private func buildNavigationControls() -> some View {
        HStack {
            if currentPage > 0 {
                Button {
                    withAnimation(.spring(response: 0.2, dampingFraction: 0.6)) {
                        currentPage -= 1
                    }
                } label: {
                    Image(systemName: "chevron.left")
                        .font(.system(size: 20, weight: .medium))
                        .foregroundStyle(.black)
                        .frame(width: 50, height: 50)
                        
                }
                .buttonStyle(ScaleButtonStyle())
            } else {
                Spacer().frame(width: 50, height: 50)
            }

            Spacer()

            HStack(spacing: 8) {
                ForEach(0...viewModel.onboardingPages.count, id: \.self) { index in
                    Circle()
                        .fill(index == currentPage ? Color.black : Color.black.opacity(0.4))
                        .frame(width: 10, height: 10)
                }
            }

            Spacer()

            if currentPage < viewModel.onboardingPages.count {
                Button {
                    withAnimation(.spring(response: 0.2, dampingFraction: 0.6)) {
                        if currentPage == viewModel.onboardingPages.count - 1 {
                            currentPage = viewModel.onboardingPages.count
                        } else {
                            currentPage += 1
                        }
                    }
                } label: {
                    Image(systemName: currentPage == viewModel.onboardingPages.count - 1 ? "hand.thumbsup" : "chevron.right")
                        .font(.system(size: 20, weight: .medium))
                        .foregroundStyle(.black)
                        .frame(width: 50, height: 50)
                        
                }
                .buttonStyle(ScaleButtonStyle())
            } else {
                Spacer().frame(width: 50, height: 50)
            }
        }
        .padding(.horizontal, 20)
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
