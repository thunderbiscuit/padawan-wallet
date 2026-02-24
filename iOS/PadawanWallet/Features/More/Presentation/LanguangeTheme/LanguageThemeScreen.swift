//
//  LanguageThemeScreen.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 21/09/25.
//

import SwiftUI

struct LanguageThemeScreen: View {
    @Environment(\.padawanColors) private var colors
    @EnvironmentObject private var languageManager: LanguageManager
    @StateObject private var viewModel: LanguageThemeScreenViewModel
    
    init() {
        _viewModel = StateObject(wrappedValue: LanguageThemeScreenViewModel())
    }
    
    var body: some View {
        BackgroundView {
            ScrollView {
                VStack(alignment: .leading, spacing: 12.0) {
                    Text(languageManager.localizedString(forKey: "select_language"))
                        .font(Fonts.subtitle)
                        .foregroundStyle(colors.text)
                        .accessibilityAddTraits(.isHeader)
                    
                    buildSection(
                        title: languageManager.localizedString(forKey: "app_level_language"),
                        type: PadawanLanguage.self,
                        itemSelected: viewModel.selectedLanguage,
                        disableItems: viewModel.disabledLanguages
                    )
                    
                    Spacer().frame(height: 18)
                    
                    buildSection(
                        title: languageManager.localizedString(forKey: "color_theme"),
                        type: PadawanColorTheme.self,
                        itemSelected: viewModel.selectedTheme,
                        disableItems: viewModel.disabledThemes
                    )
                    
                    Spacer()
                }
                .frame(maxWidth: .maxWidthScreen)
                .padding()
            }
        }
        .fullScreenCover(item: $viewModel.fullScreenCover, content: { item in
            switch item {
            case MoreScreenNavigation.alert(let data):
                AlertModalView(data: data)
                
            default:
                EmptyView()
            }
        })
        .navigationTitle(languageManager.localizedString(forKey: "change_language"))
        .navigationBarTitleDisplayMode(.inline)
    }
    
    @ViewBuilder
    private func buildSection<T: LanguageThemeItemProtocol>(
        title: String,
        type: T.Type,
        itemSelected: T,
        disableItems: [T]
    ) -> some View {
        VStack(alignment: .leading) {
            SectionTitleView(title)
            .accessibilityAddTraits(.isHeader)
            Spacer().frame(height: 10)
            
            buildItems(
                type,
                itemSelected: itemSelected,
                disableItems: disableItems
            )
        }
        .frame(maxWidth: .infinity, alignment: .leading)
    }
    
    @ViewBuilder
    private func buildItems<T: LanguageThemeItemProtocol>(
        _ type: T.Type,
        itemSelected: T,
        disableItems: [T]
    ) -> some View {
        ForEach(Array(T.allCases)) { item in
            let isSelectedItem = itemSelected.rawValue == item.rawValue
            let isDisabled = disableItems.contains(item)
            Button {
                viewModel.selectItem(item)
            } label: {
                let isSelectedItem = itemSelected.rawValue == item.rawValue
                HStack(spacing: 12) {
                    RadioBoxView(isSelected: .constant(isSelectedItem))
                        .allowsHitTesting(false)
                        .accessibilityHidden(true)
                    Text(item.rawValue)
                        .font(Fonts.font(.regular, 18))
                        .foregroundStyle(colors.text)
                }
                .padding(.leading, 8)
                .padding(.vertical, 6)
                .opacity(disableItems.contains(item) ? 0.2 : 1.0)
            }
            .accessibilityIdentifier("selection_\(item.rawValue)")
            .accessibilityAddTraits(isSelectedItem ? [.isButton, .isSelected] : [.isButton])
            .disabled(isDisabled)
        }
    }
}
