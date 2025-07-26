/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import Foundation
import SwiftUI

private extension OnboardingView {
    struct OnboardingViewStrings {
        static var screenTitle: String = Strings.padawanWallet
        static var welcomeText: String = Strings.welcomeStatement
        
        static var createWalletButtonTitle: String = Strings.createAWallet
        static var importWalletButtonTitle: String = Strings.alreadyHaveAWallet
    }
}

struct OnboardingView: View {
    @Environment(\.padawanColors) private var colors
    @AppStorage("isOnboarding") private var isOnboarding: Bool = true

    var body: some View {
        BackgroundView {
            ScrollView {
                VStack(spacing: 20) {
                    Asset.Images.padawanLogoLight.toImage
                        .resizable()
                        .frame(width: 90, height: 90)
                    
                    Text(OnboardingViewStrings.screenTitle)
                        .font(Fonts.title)
                        .foregroundStyle(colors.text)
                    
                    Text(OnboardingViewStrings.welcomeText)
                        .font(Fonts.body)
                        .foregroundStyle(colors.textFaded)
                    
                    Spacer()
                    
                    buildButtons()
                }
                .frame(minHeight: screenHeight - 90)
                .padding(.horizontal, 30)
                .padding(.bottom, 30)
            }
        }
    }
    
    @ViewBuilder
    private func buildButtons() -> some View {
        VStack(spacing: 20) {
            Group {
                PadawanButton(
                    title: OnboardingViewStrings.createWalletButtonTitle) {
                        isOnboarding = false
                    }
                    
                
                PadawanButton(
                    title: OnboardingViewStrings.importWalletButtonTitle) {
                        isOnboarding = false
                    }
            }
            .frame(width: 300)
        }
    }
}

#if DEBUG
#Preview("TatooineDesert") {
    OnboardingView()
        .environment(\.padawanColors, .tatooineDesert)
}

#Preview("VaderDark") {
    OnboardingView()
        .environment(\.padawanColors, .vaderDark)
}
#endif
