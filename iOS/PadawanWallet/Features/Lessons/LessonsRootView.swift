//
//  LessonsRootView.swift
//  PadawanWallet
//
//  Created by thunderbiscuit on 2025-06-02.
//

import Foundation
import SwiftUI

struct LessonElement {
    let type: ElementType
    let resourceKey: String
}

struct LessonContent {
    let elements: [LessonElement]
}

struct Lesson: Identifiable {
    let id = UUID()
    let title: String
    let isHighlighted: Bool
    let content: LessonContent?
}

struct LessonSection {
    let title: String
    let lessons: [Lesson]
}

enum ElementType {
    case title
    case subtitle
    case paragraph
    case resource
}

struct LessonsRootView: View {
    let lesson1Content = LessonContent(
        elements: [
            LessonElement(type: .title, resourceKey: "l1_title"),
            LessonElement(type: .paragraph, resourceKey: "l1_p1"),
            LessonElement(type: .paragraph, resourceKey: "l1_p2"),
            LessonElement(type: .subtitle, resourceKey: "l1_h2"),
            LessonElement(type: .paragraph, resourceKey: "l1_p3"),
            LessonElement(type: .paragraph, resourceKey: "l1_p4"),
            LessonElement(type: .subtitle, resourceKey: "l1_h3"),
            LessonElement(type: .paragraph, resourceKey: "l1_p5"),
            LessonElement(type: .paragraph, resourceKey: "l1_p6"),
            LessonElement(type: .subtitle, resourceKey: "l1_h4"),
            LessonElement(type: .paragraph, resourceKey: "l1_p7"),
            LessonElement(type: .paragraph, resourceKey: "l1_p8"),
            LessonElement(type: .paragraph, resourceKey: "l1_p9")
        ]
    )

    var body: some View {
        NavigationStack {
            ScrollView {
                VStack(alignment: .leading, spacing: 24) {
                    VStack(alignment: .leading, spacing: 4) {
                        Text("Padawan journey")
                            .font(.title)
                            .bold()
                        Text("Continue on your journey of learning bitcoin.")
                            .foregroundColor(.gray)
                    }

                    VStack(alignment: .leading, spacing: 12) {
                        Text("Getting started")
                            .font(.headline)
                            .bold()
                        
                        NavigationLink(destination: LessonDetailScreen(lesson: Lesson(title: "1. What is the Bitcoin Signet?", isHighlighted: false, content: lesson1Content))) {
                            LessonRow(lesson: Lesson(title: "1. What is the Bitcoin Signet?", isHighlighted: false, content: lesson1Content))
                        }
                        
                        NavigationLink(destination: LessonDetailScreen(lesson: Lesson(title: "2. Receiving bitcoin", isHighlighted: true, content: nil))) {
                            LessonRow(lesson: Lesson(title: "2. Receiving bitcoin", isHighlighted: true, content: nil))
                        }
                        
                        NavigationLink(destination: LessonDetailScreen(lesson: Lesson(title: "3. Sending bitcoin", isHighlighted: false, content: nil))) {
                            LessonRow(lesson: Lesson(title: "3. Sending bitcoin", isHighlighted: false, content: nil))
                        }
                    }
                    
                    VStack(alignment: .leading, spacing: 12) {
                        Text("Transactions")
                            .font(.headline)
                            .bold()
                        
                        NavigationLink(destination: LessonDetailScreen(lesson: Lesson(title: "4. What is the mempool?", isHighlighted: true, content: nil))) {
                            LessonRow(lesson: Lesson(title: "4. What is the mempool?", isHighlighted: true, content: nil))
                        }
                        
                        NavigationLink(destination: LessonDetailScreen(lesson: Lesson(title: "5. What are transaction fees?", isHighlighted: false, content: nil))) {
                            LessonRow(lesson: Lesson(title: "5. What are transaction fees?", isHighlighted: false, content: nil))
                        }
                        
                        NavigationLink(destination: LessonDetailScreen(lesson: Lesson(title: "6. Bitcoin units", isHighlighted: false, content: nil))) {
                            LessonRow(lesson: Lesson(title: "6. Bitcoin units", isHighlighted: false, content: nil))
                        }
                    }
                    
                    VStack(alignment: .leading, spacing: 12) {
                        Text("Advanced")
                            .font(.headline)
                            .bold()
                        
                        NavigationLink(destination: LessonDetailScreen(lesson: Lesson(title: "7. Seed phrases", isHighlighted: false, content: nil))) {
                            LessonRow(lesson: Lesson(title: "7. Seed phrases", isHighlighted: false, content: nil))
                        }
                        
                        NavigationLink(destination: LessonDetailScreen(lesson: Lesson(title: "8. Private keys", isHighlighted: false, content: nil))) {
                            LessonRow(lesson: Lesson(title: "8. Private keys", isHighlighted: false, content: nil))
                        }
                        
                        NavigationLink(destination: LessonDetailScreen(lesson: Lesson(title: "9. UTXOs", isHighlighted: false, content: nil))) {
                            LessonRow(lesson: Lesson(title: "9. UTXOs", isHighlighted: false, content: nil))
                        }
                    }
                }
                .padding()
            }
            .navigationTitle("")
            .navigationBarHidden(true)
        }
    }
}

struct LessonRow: View {
    let lesson: Lesson

    var body: some View {
        HStack {
            Text(lesson.title)
                .foregroundColor(.black)
                .padding()
            Spacer()
            if lesson.isHighlighted {
                Image(systemName: "star.fill")
                    .foregroundColor(.yellow)
                    .padding(.trailing, 12)
            } else {
                Circle()
                    .stroke(lineWidth: 2)
                    .frame(width: 24, height: 24)
                    .padding(.trailing, 12)
            }
        }
        .background(lesson.isHighlighted ? Color.red.opacity(0.8) : Color(.systemGray5))
        .cornerRadius(12)
        .shadow(color: .black.opacity(0.25), radius: 2, x: 4, y: 4)
    }
}

struct LessonDetailScreen: View {
    let lesson: Lesson

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 16) {
                if let content = lesson.content {
                    buildLessonContent(content: content)
                } else {
                    Text("Lesson content coming soon!")
                        .font(.title2)
                        .foregroundColor(.gray)
                        .padding()
                }
            }
            .padding()
        }
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
