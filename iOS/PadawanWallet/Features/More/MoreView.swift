/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import SwiftUI

//struct MoreRootView: View {
//    var body: some View {
//        ZStack {
//            Color.blue.ignoresSafeArea()
//            Text("More")
//                .font(.largeTitle)
//                .foregroundColor(.white)
//        }
//    }
//}

struct MoreRootView: View {
    var body: some View {
        NavigationStack {
            VStack(alignment: .leading, spacing: 24) {
                // Title + Subtitle
                VStack(alignment: .leading, spacing: 8) {
                    Text("More")
                        .font(.title)
                        .bold()
                    Text("A collection of everything else you need in the app.")
                        .foregroundColor(.gray)
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
                    Text("Padawan Wallet v0.16.0")
                        .font(.footnote)
                        .foregroundColor(.gray)
                    Divider()
                }
                .padding(.top, 16)

                // Reset button
                Button(action: {
                    // Reset action here
                }) {
                    Text("Reset completed chapters")
                        .frame(maxWidth: .infinity)
                        .padding()
                        .foregroundColor(.black)
                        .background(Color.red.opacity(0.8))
                        .cornerRadius(12)
                        .shadow(color: .black.opacity(0.25), radius: 2, x: 4, y: 4)
                }

                Spacer()
            }
            .padding()
            .navigationTitle("")
            .navigationBarHidden(true)
        }
    }
}

struct MoreItem: View {
    let title: String

    var body: some View {
        HStack {
            Text(title)
                .foregroundColor(.black)
            Spacer()
            Image(systemName: "chevron.right")
                .foregroundColor(.orange)
        }
        .padding()
        .background(Color(.systemGray5))
        .cornerRadius(12)
        .shadow(color: .black.opacity(0.25), radius: 1, x: 2, y: 2)
    }
}



struct RecoveryPhraseScreen: View {
    var body: some View {
        Text("Recovery Phrase Screen")
            .font(.largeTitle)
            .navigationTitle("Recovery phrase")
            .navigationBarTitleDisplayMode(.inline)
    }
}

struct SendCoinsBackScreen: View {
    var body: some View {
        Text("Send Signet Coins Back Screen")
            .font(.largeTitle)
            .navigationTitle("Send signet coins back")
            .navigationBarTitleDisplayMode(.inline)
    }
}

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

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 32) {
                // Title + Description
                VStack(alignment: .leading, spacing: 8) {
                    Text("Language + Color Theme")
                        .font(.title2)
                        .bold()

                    Text("Select your preferred language and color theme for Padawan Wallet.")
                        .foregroundColor(.gray)
                        .font(.body)
                }

                // Language picker
                VStack(alignment: .leading, spacing: 16) {
                    Text("App-Level Language")
                        .bold()
                        .font(.headline)
                        .padding(.bottom, 4)

                    Divider()

                    ForEach(Language.allCases) { language in
                        HStack {
                            Image(systemName: selectedLanguage == language ? "circle.inset.filled" : "circle")
                                .foregroundColor(selectedLanguage == language ? .red : .gray)
                            Text(language.rawValue)
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
                        .padding(.bottom, 4)

                    Divider()

                    ForEach(ColorTheme.allCases) { theme in
                        HStack {
                            Image(systemName: selectedTheme == theme ? "circle.inset.filled" : "circle")
                                .foregroundColor(theme == .vader ? .gray.opacity(0.3) : .red)
                            Text(theme.rawValue)
                                .foregroundColor(theme == .vader ? .gray.opacity(0.5) : .primary)
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
        .navigationTitle("")
        .navigationBarTitleDisplayMode(.inline)
    }
}

struct AboutPadawanScreen: View {
    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 24) {
                // About text
                Text(String(localized: "about_text"))
                    .font(.body)
                    .foregroundColor(.primary)
                    .multilineTextAlignment(.leading)
                
                // Privacy text
                Text(String(localized: "privacy_text"))
                    .font(.body)
                    .foregroundColor(.primary)
                    .multilineTextAlignment(.leading)
                
                // Privacy policy link
                Button(action: {
                    if let url = URL(string: String(localized: "privacy_link")) {
                        UIApplication.shared.open(url)
                    }
                }) {
                    Text("Read our privacy policy here.")
                        .font(.body)
                        .foregroundColor(.blue)
                        .underline()
                }
                
                Spacer()
            }
            .padding()
        }
        .navigationTitle(String(localized: "about_padawan"))
        .navigationBarTitleDisplayMode(.inline)
    }
}
