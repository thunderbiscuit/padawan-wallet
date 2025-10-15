/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import Foundation
import SwiftUI

struct LessonsListView: View {
    @Environment(\.padawanColors) private var colors
    @EnvironmentObject private var languageManager: LanguageManager
    @StateObject private var viewModel: LessonsListViewModel
    
    init(
        path: Binding<NavigationPath>
    ) {
        _viewModel = StateObject(wrappedValue: LessonsListViewModel(path: path))
    }
    
    var body: some View {
        BackgroundView {
            ScrollView {
                VStack(alignment: .leading, spacing: 12.0) {
                    Group {
                        Text(languageManager.localizedString(forKey: "padawan_journey"))
                            .font(Fonts.title)
                        Text(languageManager.localizedString(forKey: "continue_on_your_journey"))
                            .font(Fonts.subtitle)
                    }
                    .foregroundStyle(colors.text)
                    Spacer().frame(height: 4)
                    
                    buildList()
                    
                    Spacer()
                }
                .frame(maxWidth: .maxWidthScreen, alignment: .leading)
                .padding()
            }
        }
        .onAppear {
            Task {
                await viewModel.updateList()
            }
        }
        .navigationDestination(for: LessonItemList.self) { lesson in
             LessonsDetailView(path: viewModel.$path, lesson: lesson)
                .toolbar(.hidden, for: .tabBar)
        }
    }
    
    @ViewBuilder
    private func buildList() -> some View {
        ForEach(viewModel.list) { item in
            Section {
                SectionTitleView(languageManager.localizedString(forKey: item.titleKey))
                
                ForEach(item.items) { lesson in
                    buildListItem(
                        lesson: lesson
                    )
                }
            }
            Spacer().frame(height: 10)
        }
    }
    
    @ViewBuilder
    private func buildListItem(lesson: LessonItemList) -> some View {
        let lessonTitle = languageManager.localizedString(forKey: lesson.titleKey)
        
        PadawanToggleButton(
            title: "\(lesson.sort). \(lessonTitle)",
            isOn: lesson.isDone) {
                viewModel.path.append(lesson)
            }
    }
}

#if DEBUG
#Preview {
    NavigationStack {
        LessonsListView(path: .constant(.init()))
            .environment(\.padawanColors, .tatooineDesert)
            .environmentObject(LanguageManager.shared)
    }
}
#endif
