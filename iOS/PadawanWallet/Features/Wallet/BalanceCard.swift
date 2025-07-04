/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import SwiftUI

struct BalanceCard: View {
    @Environment(\.padawanColors) private var colors
    
    var body: some View {
        ZStack(alignment: .topLeading) {
            RoundedRectangle(cornerRadius: 20)
                .fill(colors.background2)
                .shadow(color: colors.darkBackground.opacity(0.2), radius: 5, x: 5, y: 5)

            VStack(alignment: .leading, spacing: 16) {
                HStack {
                    Text("bitcoin signet")
                        .foregroundColor(colors.accent2)
                        .font(.subheadline)

                    Spacer()

                    HStack {
                        Text("btc")
                            .foregroundColor(colors.textLight)
                        Text("sats")
                            .bold()
                            .foregroundColor(colors.text)
                    }
                    .padding(.horizontal, 12)
                    .padding(.vertical, 6)
                    .background(colors.background)
                    .cornerRadius(12)
                }

                HStack {
                    Text("0")
                        .font(.system(size: 48, weight: .bold))
                        .foregroundColor(colors.text)
                    Text("sats")
                        .font(.title3)
                        .foregroundColor(colors.text)
                }

                Button("sync") {}
                    .font(.headline)
                    .foregroundColor(colors.background)
                    .padding(.vertical, 8)
                    .padding(.horizontal, 40)
                    .background(colors.accent1)
                    .clipShape(Capsule())
            }
            .padding()
        }
        .frame(maxWidth: .infinity)
    }
}
