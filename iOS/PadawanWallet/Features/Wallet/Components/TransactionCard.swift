/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import SwiftUI

struct TransactionsCard: View {
    @Environment(\.padawanColors) private var colors
    
    var body: some View {
        VStack(alignment: .leading, spacing: 16) {
            Text("Transactions")
                .font(.title3)
                .bold()
                .foregroundColor(colors.text)

            VStack(alignment: .leading, spacing: 12) {
                Text("Hey! It looks like your transaction list is empty. Take a look around, and come back to get some coins so you can start playing with the wallet!")
                    .foregroundColor(colors.textLight)

                Button("Get Signet coins") {}
                    .font(.headline)
                    .foregroundColor(colors.text)
                    .padding()
                    .background(
                        ZStack {
                            // Shadow background
                            RoundedRectangle(cornerRadius: 12)
                                .fill(.black)
                                .offset(x: 4, y: 4)
                            
                            // Button background
                            RoundedRectangle(cornerRadius: 12)
                                .fill(colors.accent2)
                        }
                    )
                    .overlay(
                        RoundedRectangle(cornerRadius: 12)
                            .stroke(.black, lineWidth: 2)
                    )
            }
            .padding()
            .background(colors.background2)
            .cornerRadius(16)
            .overlay(
                RoundedRectangle(cornerRadius: 16)
                    .stroke(colors.accent3, lineWidth: 1)
            )
        }
    }
}
