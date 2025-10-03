//
//  LessonDetailViiew.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 05/10/25.
//

import Foundation
import SwiftUI

struct LessonsDetailView: View {
    @Environment(\.padawanColors) private var colors
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
            .navigationTitle(viewModel.lesson.navigationTitle)
            .navigationBarTitleDisplayMode(.inline)
        }
    }
    
    @ViewBuilder
    private func buildHeader() -> some View {
        ZStack {
            colors.errorRed
                .ignoresSafeArea()
            
            VStack {
                Spacer()
                PageIndicatorView(
                    currentIndex: viewModel.selectedIndex,
                    total: viewModel.totalPages
                )
                Spacer()
                
                Rectangle()
                    .frame(height: 2)
                    .frame(maxWidth: .infinity)
                    .foregroundStyle(colors.text)
            }
            
        }
        .frame(height: 50)
        .frame(maxWidth: .infinity)
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
                    Text(content.header)
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
            
            PadawanButton(
                icon: viewModel.nextPageImage,
                action: {
                    viewModel.goToNextPage()
                }
            )
            .frame(height: 30)
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
                        header: "Header 1",
                        texts: [
                            AnyHashableView(Text("Text 1"))
                        ]),
                    .init(
                        header: "Header 2",
                        texts: [
                            AnyHashableView(Text("Text 1"))
                        ]),
                    .init(
                        header: "Header 3",
                        texts: [
                            AnyHashableView(Text("Text 1"))
                        ])
                ],
                sort: 1
            )
        )
        .environment(\.padawanColors, .tatooineDesert)
    }
}
#endif
