/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import SwiftUI

struct ActionButton: View {
    var title: String
    var icon: String
    @Environment(\.padawanColors) private var colors

    var body: some View {
        ZStack {
            // Shadow background
            RoundedRectangle(cornerRadius: 12)
                .fill(.black)
                .offset(x: 4, y: 4)
            
            // Button content
            HStack {
                Text(title)
                Image(systemName: icon)
            }
            .font(.headline)
            .foregroundColor(colors.text)
            .padding()
            .frame(maxWidth: .infinity)
            .background(colors.accent2)
            .cornerRadius(12)
            .overlay(
                RoundedRectangle(cornerRadius: 12)
                    .stroke(.black, lineWidth: 2)
            )
        }
        .contentShape(Rectangle())
    }
}
