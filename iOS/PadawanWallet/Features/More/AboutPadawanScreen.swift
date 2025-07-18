/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import SwiftUI

struct AboutPadawanScreen: View {
    @Environment(\.padawanColors) private var colors
    
    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 24) {
                // About text
                Text(String(localized: "about_text"))
                    .font(.body)
                    .foregroundColor(colors.textLight)
                    .multilineTextAlignment(.leading)
                
                // Privacy text
                Text(String(localized: "privacy_text"))
                    .font(.body)
                    .foregroundColor(colors.textLight)
                    .multilineTextAlignment(.leading)
                
                // Privacy policy link
                Button(action: {
                    if let url = URL(string: String(localized: "privacy_link")) {
                        UIApplication.shared.open(url)
                    }
                }) {
                    Text("Read our privacy policy here.")
                        .font(.body)
                        .foregroundColor(colors.accent2)
                        .underline()
                }
                
                Spacer()
            }
            .padding()
        }
        .background(colors.background)
        .navigationTitle(String(localized: "about_padawan"))
        .navigationBarTitleDisplayMode(.inline)
    }
}
