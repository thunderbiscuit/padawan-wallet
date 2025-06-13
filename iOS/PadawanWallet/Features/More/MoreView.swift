//
//  MoreView.swift
//  PadawanWallet
//
//  Created by thunderbiscuit on 2025-06-02.
//

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
    var body: some View {
        Text("Language + Color Theme Screen")
            .font(.largeTitle)
            .navigationTitle("Language + Color Theme")
            .navigationBarTitleDisplayMode(.inline)
    }
}

struct AboutPadawanScreen: View {
    var body: some View {
        Text("About Padawan Screen")
            .font(.largeTitle)
            .navigationTitle("About Padawan")
            .navigationBarTitleDisplayMode(.inline)
    }
}
