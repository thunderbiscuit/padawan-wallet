/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import SwiftUI

struct BalanceCard: View {
    @Environment(\.padawanColors) private var colors
    
    var body: some View {
        ZStack(alignment: .topLeading) {
            // Shadow background
            RoundedRectangle(cornerRadius: 20)
                .fill(.black)
                .offset(x: 4, y: 4)
            
            // Card content
            RoundedRectangle(cornerRadius: 20)
                .fill(colors.background2)
                .overlay(
                    RoundedRectangle(cornerRadius: 20)
                        .stroke(.black, lineWidth: 2)
                )

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
                    .background(
                        ZStack {
                            // Shadow background
                            Capsule()
                                .fill(.black)
                                .offset(x: 4, y: 4)
                            
                            // Button background
                            Capsule()
                                .fill(colors.accent1)
                        }
                    )
                    .overlay(
                        Capsule()
                            .stroke(.black, lineWidth: 2)
                    )
            }
            .padding()
        }
        .frame(maxWidth: .infinity)
    }
}
