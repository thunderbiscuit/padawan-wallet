//
//  BottomSheetView.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 06/08/25.
//

import SwiftUI

extension BottomSheetView {
    struct Data: Equatable, Hashable, Identifiable {
        var id: String { title }
        var image: Image?
        var title: String
        var subtitle: String?
        var primaryButtonTitle: String?
        var secondaryButtonTitle: String?
        var primaryButtonIcon: Image?
        var secondaryButtonIcon: Image?
        var verticalAlignmentForButtons: Bool
        var onPrimaryButtonTap: (() -> Void)?
        var onSecondaryButtonTap: (() -> Void)?

        init(
            image: Image? = nil,
            titleKey: String,
            subtitle: String? = nil,
            subtitleKey: String? = nil,
            verticalAlignmentForButtons: Bool = true,
            primaryButtonTitleKey: String? = "OK",
            secondaryButtonTitleKey: String? = nil,
            primaryButtonIcon: Image? = nil,
            secondaryButtonIcon: Image? = nil,
            overrideLanguage: PadawanLanguage? = nil,
            onPrimaryButtonTap: (() -> Void)? = nil,
            onSecondaryButtonTap: (() -> Void)? = nil
        ) {
            self.image = image
            self.onPrimaryButtonTap = onPrimaryButtonTap
            self.onSecondaryButtonTap = onSecondaryButtonTap
            self.subtitle = subtitle
            self.verticalAlignmentForButtons = verticalAlignmentForButtons
            self.primaryButtonIcon = primaryButtonIcon
            self.secondaryButtonIcon = secondaryButtonIcon

            if let lang = overrideLanguage {
                self.title = LanguageManager.translate(key: titleKey, to: lang)
                self.subtitle = subtitleKey != nil ? LanguageManager.translate(key: subtitleKey!, to: lang) : self.subtitle
                self.primaryButtonTitle = primaryButtonTitleKey != nil ? LanguageManager.translate(key: primaryButtonTitleKey!, to: lang) : nil
                self.secondaryButtonTitle = secondaryButtonTitleKey != nil ? LanguageManager.translate(key: secondaryButtonTitleKey!, to: lang) : nil
            } else {
                self.title = LanguageManager.shared.localizedString(forKey: titleKey)
                self.subtitle = subtitleKey != nil ? LanguageManager.shared.localizedString(forKey: subtitleKey!) : self.subtitle
                self.primaryButtonTitle = primaryButtonTitleKey != nil ? LanguageManager.shared.localizedString(forKey: primaryButtonTitleKey!) : nil
                self.secondaryButtonTitle = secondaryButtonTitleKey != nil ? LanguageManager.shared.localizedString(forKey: secondaryButtonTitleKey!) : nil
            }
        }
        
        init(error: BDKServiceError, dismissAction: (() -> Void)? = nil) {
            self.init(
                titleKey: "generic_title_error",
                subtitle: error.localizedDescription,
                primaryButtonTitleKey: "OK",
                onPrimaryButtonTap: dismissAction
            )
        }
        
        static func == (lhs: Data, rhs: Data) -> Bool {
            return lhs.title == rhs.title && lhs.subtitle == rhs.subtitle
        }
        
        func hash(into hasher: inout Hasher) {
            hasher.combine(title)
            hasher.combine(subtitle)
        }
    }
}

struct BottomSheetView: View {
    @Environment(\.padawanColors) private var colors
    @Environment(\.dismiss) private var dismiss

    private let data: BottomSheetView.Data

    init(data: BottomSheetView.Data) {
        self.data = data
    }

    var body: some View {
        ZStack {
            colors.background
                .ignoresSafeArea()
            VStack(spacing: 16) {
                if let image = data.image {
                    image
                        .resizable()
                        .aspectRatio(contentMode: .fill)
                        .frame(maxWidth: 40.0, maxHeight: 40.0)
                        .foregroundStyle(colors.accent1)
                }
                buildTitles()
                buildDock()
                    .padding(.all, 24)
            }
            .frame(maxWidth: .maxWidthScreen)
        }
        .presentationDragIndicator(.visible)
    }

    @ViewBuilder
    private func buildTitles() -> some View {
        VStack(spacing: 16) {
            Text(data.title)
                .font(Fonts.font(.bold, 22))
                .multilineTextAlignment(.center)
            if let subtitle = data.subtitle {
                Text(subtitle)
                    .font(Fonts.font(.regular, 20))
                    .multilineTextAlignment(.center)
                    .lineLimit(5)
            }
        }
        .frame(maxWidth: .maxWidthScreen, alignment: .center)
        .padding(.all, 24)
        .foregroundStyle(colors.text)
    }

    @ViewBuilder
    private func buildDock() -> some View {
        if data.primaryButtonTitle == nil && data.secondaryButtonTitle == nil {
            EmptyView()
        } else {
            VStack(spacing: 16) {
                if let primaryButtonTitle = data.primaryButtonTitle {
                    PadawanButton(
                        title: primaryButtonTitle,
                        action: {
                            dismiss()
                            data.onPrimaryButtonTap?()
                        }
                    )
                    .frame(height: 50)
                }

                if let secondaryButtonTitle = data.secondaryButtonTitle {
                    PadawanButton(
                        title: secondaryButtonTitle,
                        action: {
                            dismiss()
                            data.onSecondaryButtonTap?()
                        }
                    )
                    .frame(height: 50)
                }
            }
        }
    }
}

#if DEBUG
#Preview {
    BottomSheetView(
        data: .init(
            image: Image(systemName: "person"),
            titleKey: "attention",
            subtitleKey: "alert_change_language",
            primaryButtonTitleKey: "button_yes",
            secondaryButtonTitleKey: "button_no",
            onPrimaryButtonTap: { print("Primary button tapped") },
            onSecondaryButtonTap: { print("Secondary button tapped") }
        )
    )
    .environment(\.padawanColors, PadawanColorTheme.tatooine.colors)
    .environmentObject(LanguageManager.shared)
}
#endif
