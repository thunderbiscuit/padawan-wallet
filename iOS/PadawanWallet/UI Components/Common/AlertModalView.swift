//
//  AlertModalView.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 06/08/25.
//

import SwiftUI

struct AlertModalView: View {
    @Environment(\.padawanColors) private var colors
    @Environment(\.dismiss) private var dismiss
    
    private let data: BottomSheetView.Data
    
    @State private var backgroundOpacity: Double = 0

    init(data: BottomSheetView.Data) {
        self.data = data
    }
    
    @ViewBuilder
    private var backdropView: some View {
        Group {
            Color.black.opacity(backgroundOpacity)
                .onTapGesture {
                    close()
                }
        }
        .onAppear {
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.2) {
                withAnimation {
                    self.backgroundOpacity = 0.4
                }
            }
        }
    }
    
    var body: some View {
        ZStack {
            backdropView
                .ignoresSafeArea()
            ZStack {
                colors.background
                    .clipShape(RoundedRectangle(cornerRadius: 16))
                buildContent()
            }
            .fixedSize(horizontal: false, vertical: true)
            .padding()
            .frame(maxWidth: .maxWidthScreen)
        }
        .background(BackgroundClearView())
    }
    
    @ViewBuilder
    private func buildContent() -> some View {
        VStack(spacing: 16) {
            if let image = data.image {
                image
                    .resizable()
                    .aspectRatio(contentMode: .fill)
                    .frame(maxWidth: 40.0, maxHeight: 40.0)
                    .foregroundStyle(colors.accent1)
                    .padding([.top, .horizontal], 40)
            }
            buildTitles()
            buildDock()
                .padding([.horizontal, .bottom], 45)
        }
        .frame(minWidth: 320, minHeight: 200)
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
                    .lineLimit(15)
            }
        }
        .frame(maxWidth: .maxWidthScreen, alignment: .center)
        .padding(.all, 24)
        .foregroundStyle(colors.text)
    }

    @ViewBuilder
    private func buildDock() -> some View {
        buttonsOrientation {
            if let onSecondaryButtonTap = data.onSecondaryButtonTap {
                PadawanButton(
                    title: data.secondaryButtonTitle,
                    icon: data.secondaryButtonIcon,
                    isDestructive: true,
                    action: {
                        close()
                        onSecondaryButtonTap()
                    }
                )
                .frame(height: 50)
            }
            
            PadawanButton(
                title: data.primaryButtonTitle,
                icon: data.primaryButtonIcon,
                action: {
                    close()
                    data.onPrimaryButtonTap?()
                }
            )
            .frame(height: 50)
        }
    }
    
    @ViewBuilder
    private func buttonsOrientation(
        @ViewBuilder content: () -> some View
    ) -> some View {
        if data.verticalAlignmentForButtons {
            VStack(spacing: 16) {
                content()
            }
        } else {
            HStack(spacing: 16) {
                content()
            }
        }
    }
    
    private func close() {
        self.backgroundOpacity = 0
        dismiss()
    }
}

#if DEBUG
#Preview {
    AlertModalView(
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
