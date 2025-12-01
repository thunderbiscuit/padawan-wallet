/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import SwiftUI

struct AboutPadawanScreen: View {
    @Environment(\.padawanColors) private var colors
    @EnvironmentObject private var languageManager: LanguageManager
    
    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 24) {
                
                Text(languageManager.localizedString(forKey: "about_text"))
                    .font(Fonts.font(.regular, 16))
                    .foregroundColor(colors.textLight)
                    .multilineTextAlignment(.leading)
                    .accessibilityAddTraits(.isStaticText)
                
                Text(languageManager.localizedString(forKey: "privacy_text"))
                    .font(Fonts.font(.regular, 16))
                    .foregroundColor(colors.textLight)
                    .multilineTextAlignment(.leading)
                    .accessibilityAddTraits(.isStaticText)
                
                Button(action: {
                    if let url = URL(string: languageManager.localizedString(forKey: "privacy_link")) {
                        UIApplication.shared.open(url)
                    }
                }) {
                    Text(languageManager.localizedString(forKey: "button_link_privacy"))
                        .font(Fonts.font(.regular, 16))
                        .foregroundColor(colors.accent2)
                        .underline()
                }
                .accessibilityRemoveTraits(.isButton)
                .accessibilityAddTraits(.isLink)
                .accessibilityHint(languageManager.localizedString(forKey: "accessibility_privacy_link_hint"))
                
                Spacer()
            }
            .padding()
        }
        .background(colors.background)
        .navigationTitle(languageManager.localizedString(forKey: "about_padawan"))
        .navigationBarTitleDisplayMode(.inline)
    }
}
