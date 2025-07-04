/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import SwiftUI

struct SendScreen: View {
    @State private var amount: String = ""
    @State private var address: String = ""
    @State private var feeRate: Double = 1.0
    @Environment(\.padawanColors) private var colors

    let balance: Int = 150_000

    var body: some View {
        VStack(alignment: .leading, spacing: 24) {
            // Title
            Text("Send bitcoin")
                .font(.title2)
                .bold()
                .foregroundColor(colors.text)
                .frame(maxWidth: .infinity, alignment: .center)

            // Amount field
            VStack(alignment: .leading, spacing: 8) {
                HStack {
                    Text("Amount")
                        .bold()
                        .foregroundColor(colors.text)
                    Spacer()
                    Text("Balance: \(balance) sat")
                        .font(.subheadline)
                        .foregroundColor(colors.textLight)
                }

                TextField("Enter amount (sats)", text: $amount)
                    .keyboardType(.numberPad)
                    .padding()
                    .background(colors.background2)
                    .foregroundColor(colors.text)
                    .cornerRadius(12)
                    .overlay(
                        RoundedRectangle(cornerRadius: 12)
                            .stroke(colors.accent3, lineWidth: 2)
                    )
            }

            // Address field
            VStack(alignment: .leading, spacing: 8) {
                Text("Address")
                    .bold()
                    .foregroundColor(colors.text)

                HStack {
                    TextField("Enter a bitcoin signet address", text: $address)
                        .autocapitalization(.none)
                        .disableAutocorrection(true)
                        .foregroundColor(colors.text)
                    Button(action: {
                        // Camera action
                    }) {
                        Image(systemName: "camera")
                            .foregroundColor(colors.accent1)
                    }
                }
                .padding()
                .background(colors.background2)
                .cornerRadius(12)
                .overlay(
                    RoundedRectangle(cornerRadius: 12)
                        .stroke(colors.accent3, lineWidth: 2)
                )
            }

            // Fee slider
            VStack(alignment: .leading, spacing: 8) {
                Text("Fees (sats/vbytes)")
                    .bold()
                    .foregroundColor(colors.text)

                Slider(value: $feeRate, in: 1...10, step: 1)
                    .accentColor(colors.accent1)
                Text(String(format: "%.1f", feeRate))
                    .font(.subheadline)
                    .foregroundColor(colors.textLight)
            }

            // Submit button
            Button(action: {
                // Trigger verify transaction flow
            }) {
                Text("Verify transaction")
                    .foregroundColor(colors.text)
                    .frame(maxWidth: .infinity)
                    .padding()
                    .background(colors.accent2)
                    .cornerRadius(12)
                    .shadow(color: colors.darkBackground.opacity(0.25), radius: 2, x: 4, y: 4)
            }
            .padding(.top)

            Spacer()
        }
        .padding()
        .background(colors.background)
        .navigationTitle("")
        .navigationBarTitleDisplayMode(.inline)
    }
}
