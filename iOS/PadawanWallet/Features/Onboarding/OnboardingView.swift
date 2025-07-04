/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

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
