/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import Foundation
import SwiftUI

struct LessonDetailScreen: View {
    let lesson: Lesson
    @Environment(\.padawanColors) private var colors

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 16) {
                if let content = lesson.content {
                    buildLessonContent(content: content)
                } else {
                    Text("Lesson content coming soon!")
                        .font(.title2)
                        .foregroundColor(colors.textLight)
                        .padding()
                }
            }
            .padding()
        }
        .background(colors.background)
        .navigationTitle(lesson.title)
        .navigationBarTitleDisplayMode(.inline)
    }
    
    @ViewBuilder
    private func buildLessonContent(content: LessonContent) -> some View {
        ForEach(0..<content.elements.count, id: \.self) { index in
            let element = content.elements[index]
            
            VStack(alignment: .leading, spacing: 12) {
                Text(NSLocalizedString(element.resourceKey, comment: ""))
                    .font(fontForElementType(element.type))
                    .fontWeight(element.type == .title || element.type == .subtitle ? .bold : .regular)
                    .lineSpacing(element.type == .paragraph ? 4 : 0)
                    .padding(.top, element.type == .subtitle ? 8 : 0)
                    .foregroundColor(element.type == .title ? colors.text : (element.type == .subtitle ? colors.text : colors.textLight))
            }
        }
    }
    
    private func fontForElementType(_ type: ElementType) -> Font {
        switch type {
        case .title: return .largeTitle
        case .subtitle: return .title2
        case .paragraph: return .body
        case .resource: return .body
        }
    }
}
