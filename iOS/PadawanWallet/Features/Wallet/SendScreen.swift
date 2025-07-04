/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import SwiftUI

struct SendScreen: View {
    @State private var amount: String = ""
    @State private var address: String = ""
    @State private var feeRate: Double = 1.0

    let balance: Int = 150_000

    var body: some View {
        VStack(alignment: .leading, spacing: 24) {
            // Title
            Text("Send bitcoin")
                .font(.title2)
                .bold()
                .frame(maxWidth: .infinity, alignment: .center)

            // Amount field
            VStack(alignment: .leading, spacing: 8) {
                HStack {
                    Text("Amount").bold()
                    Spacer()
                    Text("Balance: \(balance) sat")
                        .font(.subheadline)
                }

                TextField("Enter amount (sats)", text: $amount)
                    .keyboardType(.numberPad)
                    .padding()
                    .background(Color(.systemGray6))
                    .cornerRadius(12)
                    .overlay(
                        RoundedRectangle(cornerRadius: 12)
                            .stroke(.black, lineWidth: 2)
                    )
            }

            // Address field
            VStack(alignment: .leading, spacing: 8) {
                Text("Address").bold()

                HStack {
                    TextField("Enter a bitcoin signet address", text: $address)
                        .autocapitalization(.none)
                        .disableAutocorrection(true)
                    Button(action: {
                        // Camera action
                    }) {
                        Image(systemName: "camera")
                            .foregroundColor(.black)
                    }
                }
                .padding()
                .background(Color(.systemGray6))
                .cornerRadius(12)
                .overlay(
                    RoundedRectangle(cornerRadius: 12)
                        .stroke(.black, lineWidth: 2)
                )
            }

            // Fee slider
            VStack(alignment: .leading, spacing: 8) {
                Text("Fees (sats/vbytes)").bold()

                Slider(value: $feeRate, in: 1...10, step: 1)
                    .accentColor(.red)
                Text(String(format: "%.1f", feeRate))
                    .font(.subheadline)
            }

            // Submit button
            Button(action: {
                // Trigger verify transaction flow
            }) {
                Text("Verify transaction")
                    .foregroundColor(.black)
                    .frame(maxWidth: .infinity)
                    .padding()
                    .background(Color.orange)
                    .cornerRadius(12)
                    .shadow(color: .black.opacity(0.25), radius: 2, x: 4, y: 4)
            }
            .padding(.top)

            Spacer()
        }
        .padding()
        .navigationTitle("")
        .navigationBarTitleDisplayMode(.inline)
    }
}
