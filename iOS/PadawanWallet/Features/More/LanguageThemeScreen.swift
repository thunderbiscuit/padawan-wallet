/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import SwiftUI

struct LanguageThemeScreen: View {
    enum Language: String, CaseIterable, Identifiable {
        case english = "English"
        case spanish = "Spanish"
        case portuguese = "Portuguese"

        var id: String { self.rawValue }
    }

    enum ColorTheme: String, CaseIterable, Identifiable {
        case tatooine = "Tatooine Desert"
        case vader = "Vader Dark (Coming Soon!)"

        var id: String { self.rawValue }
    }

    @State private var selectedLanguage: Language = .english
    @State private var selectedTheme: ColorTheme = .tatooine
    @Environment(\.padawanColors) private var colors

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 32) {
                // Title + Description
                VStack(alignment: .leading, spacing: 8) {
                    Text("Language + Color Theme")
                        .font(.title2)
                        .bold()
                        .foregroundColor(colors.text)

                    Text("Select your preferred language and color theme for Padawan Wallet.")
                        .foregroundColor(colors.textLight)
                        .font(.body)
                }

                // Language picker
                VStack(alignment: .leading, spacing: 16) {
                    Text("App-Level Language")
                        .bold()
                        .font(.headline)
                        .foregroundColor(colors.text)
                        .padding(.bottom, 4)

                    Divider()
                        .background(colors.textFaded)

                    ForEach(Language.allCases) { language in
                        HStack {
                            Image(systemName: selectedLanguage == language ? "circle.inset.filled" : "circle")
                                .foregroundColor(selectedLanguage == language ? colors.accent1 : colors.textLight)
                            Text(language.rawValue)
                                .foregroundColor(colors.text)
                            Spacer()
                        }
                        .contentShape(Rectangle())
                        .onTapGesture {
                            selectedLanguage = language
                        }
                    }
                }

                // Color theme picker
                VStack(alignment: .leading, spacing: 16) {
                    Text("Color Theme")
                        .bold()
                        .font(.headline)
                        .foregroundColor(colors.text)
                        .padding(.bottom, 4)

                    Divider()
                        .background(colors.textFaded)

                    ForEach(ColorTheme.allCases) { theme in
                        HStack {
                            Image(systemName: selectedTheme == theme ? "circle.inset.filled" : "circle")
                                .foregroundColor(theme == .vader ? colors.textFaded : colors.accent1)
                            Text(theme.rawValue)
                                .foregroundColor(theme == .vader ? colors.textFaded : colors.text)
                            Spacer()
                        }
                        .contentShape(Rectangle())
                        .onTapGesture {
                            if theme != .vader {
                                selectedTheme = theme
                            }
                        }
                    }
                }

                Spacer()
            }
            .padding()
        }
        .background(colors.background)
        .navigationTitle("")
        .navigationBarTitleDisplayMode(.inline)
    }
}
