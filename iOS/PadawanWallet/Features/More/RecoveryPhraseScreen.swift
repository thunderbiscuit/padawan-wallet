/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import SwiftUI

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
