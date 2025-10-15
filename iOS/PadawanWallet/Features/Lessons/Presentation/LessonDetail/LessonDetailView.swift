//
//  LessonDetailViiew.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 05/10/25.
//

import Foundation
import SwiftUI
import BitcoinUI

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
            .navigationTitle(languageManager.localizedString(forKey: viewModel.lesson.navigationTitleKey))
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
                Spacer()
                
                Rectangle()
                    .frame(height: 1)
                    .frame(maxWidth: .infinity)
                    .foregroundStyle(colors.text)
            }
        }
        .frame(height: 40)
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
                    Text(languageManager.localizedString(forKey: content.headerKey))
                        .font(Fonts.font(.bold, 24))
                    
                    ForEach(content.items) { item in
                        switch item {
                        case .text(let key):
                            let translatedText = languageManager.localizedString(forKey: key)
                            Text.lessonText(translatedText)

                        case .qrCode(let address):
                            PadawanCardView(
                                backgroundColor: colors.background2,
                                content: { QRCodeView(qrCodeType: .bitcoin(address)) }
                            )
                            .padding(.horizontal, 8)
                            .frame(height: 120)

                        case .addressCard(let address):
                            PadawanCardView(
                                backgroundColor: colors.background2,
                                content: {
                                    Text(address)
                                        .font(Fonts.font(.regular, 13))
                                        .lineLimit(2)
                                        .minimumScaleFactor(0.5)
                                        .padding(.horizontal, 16)
                                }
                            )
                            .padding(.horizontal, 8)
                            .frame(height: 120)

                        case .image(let name):
                            if let uiImage = UIImage(named: name) {
                                Image(uiImage: uiImage)
                                     .resizable()
                                     .aspectRatio(contentMode: .fit)
                                     .padding()
                                     .frame(height: 120)
                            } else {
                                Image(systemName: "photo.on.rectangle.angled")
                                     .resizable()
                                     .aspectRatio(contentMode: .fit)
                                     .padding()
                                     .frame(height: 120)
                            }
                        }
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
    NavigationStack {
        LessonsDetailView(
            path: .constant(.init()),
            lesson: .init(
                id: "preview_l1",
                titleKey: "l1_title",
                navigationTitleKey: "l1_app_bar",
                content: [
                    .init(
                        headerKey: "l1_title",
                        items: [
                            .text(key: "l1_p1"),
                            .text(key: "l1_p2")
                        ]
                    ),
                    .init(
                        headerKey: "l2_title",
                        items: [
                            .text(key: "l2_p1"),
                            .addressCard(address: "tb1pd8jmenqpe7rz2mavfdx7uc8pj7vskxv4rl6avxlqsw2u8u7d4gfs97durt"),
                            .qrCode(address: "tb1pd8jmenqpe7rz2mavfdx7uc8pj7vskxv4rl6avxlqsw2u8u7d4gfs97durt")
                        ]
                    )
                ],
                sort: 1
            )
        )
        .environment(\.padawanColors, PadawanColorTheme.tatooine.colors)
        .environmentObject(LanguageManager.shared)
    }
}
#endif
