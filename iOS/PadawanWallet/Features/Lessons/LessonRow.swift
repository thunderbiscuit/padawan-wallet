/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import Foundation
import SwiftUI

struct LessonRow: View {
    let lesson: Lesson
    @Environment(\.padawanColors) private var colors

    var body: some View {
        ZStack {
            // Shadow background
            RoundedRectangle(cornerRadius: 12)
                .fill(.black)
                .offset(x: 4, y: 4)
            
            // Row content
            HStack {
                Text(lesson.title)
                    .foregroundColor(colors.text)
                    .padding()
                Spacer()
                if lesson.isHighlighted {
                    Image(systemName: "star.fill")
                        .foregroundColor(colors.accent2)
                        .padding(.trailing, 12)
                } else {
                    Circle()
                        .stroke(colors.textLight, lineWidth: 2)
                        .frame(width: 24, height: 24)
                        .padding(.trailing, 12)
                }
            }
            .background(lesson.isHighlighted ? colors.accent1Light : colors.background2)
            .cornerRadius(12)
            .overlay(
                RoundedRectangle(cornerRadius: 12)
                    .stroke(.black, lineWidth: 2)
            )
        }
    }
}
