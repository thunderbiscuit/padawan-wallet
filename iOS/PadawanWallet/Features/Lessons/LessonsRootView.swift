//
//  LessonsRootView.swift
//  PadawanWallet
//
//  Created by thunderbiscuit on 2025-06-02.
//

import Foundation
import SwiftUI

struct Lesson: Identifiable {
    let id = UUID()
    let title: String
    let isHighlighted: Bool
}

struct LessonSection {
    let title: String
    let lessons: [Lesson]
}

struct LessonsRootView: View {
    let sections = [
        LessonSection(title: "Getting started", lessons: [
            Lesson(title: "1. What is the Bitcoin Signet?", isHighlighted: false),
            Lesson(title: "2. Receiving bitcoin", isHighlighted: true),
            Lesson(title: "3. Sending bitcoin", isHighlighted: false)
        ]),
        LessonSection(title: "Transactions", lessons: [
            Lesson(title: "4. What is the mempool?", isHighlighted: true),
            Lesson(title: "5. What are transaction fees?", isHighlighted: false),
            Lesson(title: "6. Bitcoin units", isHighlighted: false)
        ])
    ]

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

                    ForEach(sections, id: \.title) { section in
                        VStack(alignment: .leading, spacing: 12) {
                            Text(section.title)
                                .font(.headline)
                                .bold()
                            ForEach(section.lessons) { lesson in
                                NavigationLink(destination: LessonDetailScreen(title: lesson.title)) {
                                    LessonRow(lesson: lesson)
                                }
                            }
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
    let title: String

    var body: some View {
        Text(title)
            .font(.largeTitle)
            .navigationTitle(title)
            .navigationBarTitleDisplayMode(.inline)
    }
}
