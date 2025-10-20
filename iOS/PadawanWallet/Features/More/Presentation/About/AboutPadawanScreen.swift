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
                
                Text(LanguageManager.shared.localizedString(forKey: "about_text"))
                    .font(Fonts.font(.regular, 16))
                    .foregroundColor(colors.textLight)
                    .multilineTextAlignment(.leading)
                
                Text(LanguageManager.shared.localizedString(forKey: "privacy_text"))
                    .font(Fonts.font(.regular, 16))
                    .foregroundColor(colors.textLight)
                    .multilineTextAlignment(.leading)
                
                Button(action: {
                    if let url = URL(string: LanguageManager.shared.localizedString(forKey: "privacy_link")) {
                        UIApplication.shared.open(url)
                    }
                }) {
                    Text(LanguageManager.shared.localizedString(forKey: "button_link_privacy"))
                        .font(Fonts.font(.regular, 16))
                        .foregroundColor(colors.accent2)
                        .underline()
                }
                
                Spacer()
            }
            .padding()
        }
        .background(colors.background)
        .navigationTitle(LanguageManager.shared.localizedString(forKey: "about_padawan"))
        .navigationBarTitleDisplayMode(.inline)
    }
}
