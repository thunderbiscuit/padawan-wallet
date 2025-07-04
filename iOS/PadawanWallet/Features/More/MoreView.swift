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
                    Text("More")
                        .font(.title)
                        .bold()
                        .foregroundColor(colors.text)
                    Text("A collection of everything else you need in the app.")
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

struct MoreItem: View {
    let title: String
    @Environment(\.padawanColors) private var colors

    var body: some View {
        HStack {
            Text(title)
                .foregroundColor(colors.text)
            Spacer()
            Image(systemName: "chevron.right")
                .foregroundColor(colors.accent2)
        }
        .padding()
        .background(
            ZStack {
                // Shadow background
                RoundedRectangle(cornerRadius: 12)
                    .fill(.black)
                    .offset(x: 4, y: 4)
                
                // Item background
                RoundedRectangle(cornerRadius: 12)
                    .fill(colors.background2)
            }
        )
        .overlay(
            RoundedRectangle(cornerRadius: 12)
                .stroke(.black, lineWidth: 2)
        )
    }
}



struct RecoveryPhraseScreen: View {
    @Environment(\.padawanColors) private var colors
    
    var body: some View {
        Text("Recovery Phrase Screen")
            .font(.largeTitle)
            .foregroundColor(colors.text)
            .frame(maxWidth: .infinity, maxHeight: .infinity)
            .background(colors.background)
            .navigationTitle("Recovery phrase")
            .navigationBarTitleDisplayMode(.inline)
    }
}

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
