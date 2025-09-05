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
        var primaryButtonTitle: String? = "OK"
        var secondaryButtonTitle: String?
        var onPrimaryButtonTap: (() -> Void)?
        var onSecondaryButtonTap: (() -> Void)?
        
        init(image: Image? = nil, title: String, subtitle: String? = nil, primaryButtonTitle: String? = "OK", secondaryButtonTitle: String? = nil, onPrimaryButtonTap: (() -> Void)? = nil, onSecondaryButtonTap: (() -> Void)? = nil) {
            self.image = image
            self.title = title
            self.subtitle = subtitle
            self.primaryButtonTitle = primaryButtonTitle
            self.secondaryButtonTitle = secondaryButtonTitle
            self.onPrimaryButtonTap = onPrimaryButtonTap
            self.onSecondaryButtonTap = onSecondaryButtonTap
        }
        
        init(error: BDKServiceError, dismissAction: (() -> Void)? = nil) {
            self.title = Strings.genericTitleError
            self.subtitle = error.localizedDescription
            self.primaryButtonTitle = "OK"
            self.onPrimaryButtonTap = dismissAction
        }
        
        static func == (lhs: Data, rhs: Data) -> Bool {
            return lhs.title == rhs.title &&
            lhs.subtitle == rhs.subtitle &&
            lhs.primaryButtonTitle == rhs.primaryButtonTitle &&
            lhs.secondaryButtonTitle == rhs.secondaryButtonTitle
        }
        
        func hash(into hasher: inout Hasher) {
            hasher.combine(title)
            hasher.combine(subtitle)
            hasher.combine(primaryButtonTitle)
            hasher.combine(secondaryButtonTitle)
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
            title: "Title of sheetError",
            subtitle: "Subtractive text that explains the error",
            primaryButtonTitle: "OK",
            secondaryButtonTitle: "Secondary",
            onPrimaryButtonTap: { print("Primary button tapped") },
            onSecondaryButtonTap: { print("Secondary button tapped") }
        )
    )
}
#endif
