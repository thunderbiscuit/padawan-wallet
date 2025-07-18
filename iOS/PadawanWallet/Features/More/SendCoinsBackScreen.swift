/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import SwiftUI

struct SendCoinsBackScreen: View {
    @State private var showCopiedMessage = false
    @Environment(\.padawanColors) private var colors
    
    var body: some View {
        ScrollView {
            VStack(spacing: 20) {
                // QR Code placeholder (since we don't have the vector asset)
                VStack {
                    Image(systemName: "qrcode")
                        .font(.system(size: 200))
                        .foregroundColor(colors.text)
                        .background(Color.white)
                        .cornerRadius(12)
                        .padding()
                }
                .background(colors.background2)
                .cornerRadius(12)
                
                // Bitcoin address with copy functionality
                VStack(alignment: .leading, spacing: 8) {
                    Button(action: {
                        let address = String(localized: "send_coins_back_address")
                        UIPasteboard.general.string = address
                        showCopiedMessage = true
                        DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
                            showCopiedMessage = false
                        }
                    }) {
                        HStack {
                            Text(String(localized: "send_coins_back_address"))
                                .font(.system(size: 14))
                                .foregroundColor(colors.text)
                                .multilineTextAlignment(.leading)
                            
                            Spacer()
                            
                            Image(systemName: showCopiedMessage ? "checkmark.circle.fill" : "doc.on.clipboard")
                                .foregroundColor(showCopiedMessage ? colors.goGreen : colors.textLight)
                        }
                        .padding()
                        .background(colors.background2)
                        .cornerRadius(8)
                    }
                    
                    if showCopiedMessage {
                        Text("Address copied to clipboard!")
                            .font(.caption)
                            .foregroundColor(colors.goGreen)
                            .padding(.horizontal)
                    }
                }
                
                // Explanatory text
                Text(String(localized: "send_coins_back"))
                    .font(.body)
                    .foregroundColor(colors.textLight)
                    .multilineTextAlignment(.leading)
                    .padding(.horizontal)
                
                Spacer()
            }
            .padding()
        }
        .background(colors.background)
        .navigationTitle(String(localized: "send_signet_coins_back"))
        .navigationBarTitleDisplayMode(.inline)
    }
}
