//
//  LessonDetailViiew.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 05/10/25.
//

import Foundation
import SwiftUI
import BitcoinUI

private struct LessonsDetailAssets {
    static var accPrevPage: String { LanguageManager.shared.localizedString(forKey: "accessibility_prev_page") }
    static var accNextPage: String { LanguageManager.shared.localizedString(forKey: "accessibility_next_page") }
    static var accFinishLesson: String { LanguageManager.shared.localizedString(forKey: "accessibility_finish_lesson") }
    
    static func accPageProgress(current: Int, total: Int) -> String {
        return "\(LanguageManager.shared.localizedString(forKey: "accessibility_page")) \(current + 1) \(LanguageManager.shared.localizedString(forKey: "accessibility_of")) \(total)"
    }
}

struct LessonsDetailView: View {
    @Environment(\.padawanColors) private var colors
    @EnvironmentObject private var languageManager: LanguageManager
    @StateObject private var viewModel: LessonsDetailViewModel
    
    init(
        path: Binding<NavigationPath>,
        lesson: LessonItemList
    ) {
        _viewModel = StateObject(
            wrappedValue: LessonsDetailViewModel(
                path: path,
                lesson: lesson
            )
        )
    }
    
    var body: some View {
        VStack(spacing: .zero) {
            buildHeader()
            
            BackgroundView {
                VStack {
                    buildTabView(lesson: viewModel.lesson)
                    
                    Spacer()
                    
                    buildDock()
                }
                .frame(maxWidth: .maxWidthScreen, maxHeight: .infinity, alignment: .leading)
                .padding()
            }
            .navigationTitle(languageManager.localizedString(forKey: viewModel.lesson.navigationTitle))
            .navigationBarTitleDisplayMode(.inline)
        }
    }
    
    @ViewBuilder
    private func buildHeader() -> some View {
        ZStack {
            colors.background2
                .ignoresSafeArea()
            
            VStack {
                Spacer()
                PageIndicatorView(
                    currentIndex: viewModel.selectedIndex,
                    total: viewModel.totalPages
                )
                .accessibilityLabel(LessonsDetailAssets.accPageProgress(current: viewModel.selectedIndex, total: viewModel.totalPages))
                .accessibilityAddTraits(.updatesFrequently)
                Spacer()
                
                Rectangle()
                    .frame(height: 1)
                    .frame(maxWidth: .infinity)
                    .foregroundStyle(colors.text)
            }
        }
        .frame(height: 40)
        .frame(maxWidth: .infinity)
        .accessibilityElement(children: .contain)
    }
    
    @ViewBuilder
    private func buildTabView(lesson: LessonItemList) -> some View {
        TabView(selection: $viewModel.selectedIndex) {
            ForEach(Array(lesson.content.enumerated()), id: \.0) { index, content in
                buildContent(content)
                    .tag(index)
            }
        }
        .tabViewStyle(PageTabViewStyle(indexDisplayMode: .never))
    }
    
    @ViewBuilder
    private func buildContent(_ content: LessonContent) -> some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 16) {
                Group {
                    Text(languageManager.localizedString(forKey: content.headerKey))
                        .font(Fonts.font(.bold, 24))
                    
                    ForEach(content.texts, id: \.self) { text in
                        text.view
                    }
                    Spacer()
                }
                .foregroundStyle(colors.text)
            }
            .frame(maxWidth: .infinity, alignment: .leading)
        }
    }
    
    @ViewBuilder
    private func buildDock() -> some View {
        HStack(spacing: 20) {
            PadawanButton(
                icon: Image(systemName: "chevron.left"),
                action: {
                    viewModel.goBack()
                }
            )
            .frame(height: 30)
            .accessibilityLabel(LessonsDetailAssets.accPrevPage)
            .accessibilityRemoveTraits(.isImage)
            
            PadawanButton(
                icon: viewModel.nextPageImage,
                action: {
                    viewModel.goToNextPage()
                }
            )
            .frame(height: 30)
            .accessibilityLabel(
                viewModel.selectedIndex == viewModel.totalPages - 1
                ? LessonsDetailAssets.accFinishLesson
                : LessonsDetailAssets.accNextPage
            )
            .accessibilityRemoveTraits(.isImage)
        }
        .padding()
    }
}

#if DEBUG
#Preview {
    NavigationStack(path: .constant(.init())) {
        LessonsDetailView(
            path: .constant(.init()),
            lesson: .init(
                id: "l1",
                title: "What is the bitcoin Signet?",
                navigationTitle: "Bitcoin Networks",
                content: [
                    .init(
                        headerKey: "Header 1",
                        texts: [
                            AnyHashableView(Text("Texto 1"))
                        ]),
                    .init(
                        headerKey: "Header 2",
                        texts: [
                            AnyHashableView(Text("Texto 2"))
                        ]),
                    .init(
                        headerKey: "Header 3",
                        texts: [
                            AnyHashableView(Text("Texto 3"))
                        ])
                ],
                sort: 1
            )
        )
        .environment(\.padawanColors, PadawanColorTheme.tatooine.colors)
        .environmentObject(LanguageManager.shared)
    }
}
#endif
