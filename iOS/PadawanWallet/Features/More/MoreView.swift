/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import SwiftUI

struct MoreRootView: View {
    @Environment(\.padawanColors) private var colors
    
    var body: some View {
        NavigationStack {
            VStack(alignment: .leading, spacing: 24) {
                // Title + Subtitle
                VStack(alignment: .leading, spacing: 8) {
                    Text(Strings.settings)
                        .font(Fonts.title)
                        .foregroundColor(colors.text)
                    Text(Strings.everythingElse)
                        .font(Fonts.subtitle)
                        .foregroundColor(colors.textLight)
                }

                // Navigation items
                VStack(spacing: 12) {
                    NavigationLink(destination: RecoveryPhraseScreen()) {
                        MoreItem(title: "Recovery phrase")
                    }
                    NavigationLink(destination: SendCoinsBackScreen()) {
                        MoreItem(title: "Send signet coins back")
                    }
                    NavigationLink(destination: LanguageThemeScreen()) {
                        MoreItem(title: "Language + Color Theme")
                    }
                    NavigationLink(destination: AboutPadawanScreen()) {
                        MoreItem(title: "About Padawan")
                    }
                }

                // App version
                VStack(spacing: 4) {
                    Divider()
                        .background(colors.textFaded)
                    Text("Padawan Wallet v0.16.0")
                        .font(.footnote)
                        .foregroundColor(colors.textLight)
                    Divider()
                        .background(colors.textFaded)
                }
                .padding(.top, 16)

                // Reset button
                Button(action: {
                    // Reset action here
                }) {
                    Text("Reset completed chapters")
                        .frame(maxWidth: .infinity)
                        .padding()
                        .foregroundColor(colors.text)
                        .background(
                            ZStack {
                                // Shadow background
                                RoundedRectangle(cornerRadius: 12)
                                    .fill(.black)
                                    .offset(x: 4, y: 4)
                                
                                // Button background
                                RoundedRectangle(cornerRadius: 12)
                                    .fill(colors.errorRed)
                            }
                        )
                        .overlay(
                            RoundedRectangle(cornerRadius: 12)
                                .stroke(.black, lineWidth: 2)
                        )
                }

                Spacer()
            }
            .padding()
            .background(colors.background)
            .navigationTitle("")
            .navigationBarHidden(true)
        }
    }
}
