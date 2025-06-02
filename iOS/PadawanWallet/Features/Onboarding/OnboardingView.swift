//
//  OnboardingView.swift
//  PadawanWallet
//
//  Created by thunderbiscuit on 2025-06-02.
//

import Foundation
import SwiftUI

struct OnboardingView: View {
    @AppStorage("isOnboarding") private var isOnboarding: Bool = true

    var body: some View {
        VStack(spacing: 20) {
            Text("Onboarding Screen")
                .font(.largeTitle)

            Button("Continue to Wallet") {
                isOnboarding = false
            }
            .padding()
            .background(Color.blue)
            .foregroundColor(.white)
            .clipShape(Capsule())
        }
        .padding()
    }
}
