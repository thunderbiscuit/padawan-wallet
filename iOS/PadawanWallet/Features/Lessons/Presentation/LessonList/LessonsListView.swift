/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import Foundation
import SwiftUI

struct LessonsListView: View {
    @Environment(\.padawanColors) private var colors
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
                        Text(Strings.padawanJourney)
                            .font(Fonts.title)
                        Text(Strings.continueOnYourJourney)
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
        .navigationDestination(for: LessonScreenNavigation.self) { item in
            Group {
                switch item {
                case .lessonDetails(let lesson):
                    LessonsDetailView(path: viewModel.$path, lesson: lesson)
                    
                case .alert(let data):
                    AlertModalView(data: data)
                }
            }
            .toolbar(.hidden, for: .tabBar)
        }
    }
    
    @ViewBuilder
    private func buildList() -> some View {
        ForEach(viewModel.list) { item in
            Section {
                SectionTitleView(item.title)
                
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
        PadawanToggleButton(
            title: "\(lesson.sort). \(lesson.title)",
            isOn: lesson.isDone) {
                viewModel.showLesson(for: lesson)
            }
    }
}

#if DEBUG
#Preview {
    LessonsListView(path: .constant(.init()))
        .environment(\.padawanColors, .tatooineDesert)
}
#endif
