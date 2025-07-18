/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import SwiftUI

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
