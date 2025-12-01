/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import SwiftUI

private struct RecoveryPhraseScreenAssets {
    static var title: String { LanguageManager.shared.localizedString(forKey: "your_recovery_phrase") }
    
    static func accWordLabel(index: Int, word: String) -> String {
        let label = LanguageManager.shared.localizedString(forKey: "accessibility_word_number")
        return "\(label) \(index), \(word)"
    }
}

struct RecoveryPhraseScreen: View {
    @Environment(\.padawanColors) private var colors
    @EnvironmentObject private var languageManager: LanguageManager
    
    private let words: String
    
    private let columns = [
        GridItem(.flexible(), spacing: 20),
        GridItem(.flexible(), spacing: 20)
    ]
    
    init(words: String) {
        self.words = words
    }
    
    var body: some View {
        BackgroundView {
            ScrollView {
                LazyVGrid(columns: columns, spacing: 20) {
                    let words = words.components(separatedBy: .whitespaces)
                    
                    ForEach(Array(words.enumerated()), id: \.offset) { index, word in
                        PadawanCardView(
                            backgroundColor: colors.accent1) {
                                Text("\(index + 1). \(word)")
                                    .font(Fonts.font(.medium, 16))
                                    .foregroundColor(colors.text)
                            }
                            .frame(height: 50)
                            .accessibilityElement(children: .ignore)
                            .accessibilityLabel(RecoveryPhraseScreenAssets.accWordLabel(index: index + 1, word: word))
                        
                    }
                }
                .padding()
            }
        }
        .navigationTitle(RecoveryPhraseScreenAssets.title)
    }
}

#if DEBUG
#Preview {
    NavigationStack {
        RecoveryPhraseScreen(
            words: "omit arm chunk harbor path unfair coil radar stairs unfair raise memory"
        )
        .environment(\.padawanColors, PadawanColorTheme.tatooine.colors)
        .environmentObject(LanguageManager.shared)
    }
}
#endif
