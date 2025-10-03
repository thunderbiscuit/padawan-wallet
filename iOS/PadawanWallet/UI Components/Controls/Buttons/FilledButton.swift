//
//  FilledButton.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 25/07/25.
//

import SwiftUI

struct FilledButton: View {
    @Environment(\.padawanColors) private var colors
    
    private let title: String
    private let icon: Image?
    private let titleColor: Color?
    private let iconTintColor: Color?
    private let backgroundColor: Color?
    
    private let action: (() -> Void)?
    
    init(
        title: String,
        icon: Image? = nil,
        titleColor: Color? = nil,
        iconTintColor: Color? = nil,
        backgroundColor: Color? = nil,
        action: (() -> Void)? = nil
    ) {
        self.title = title
        self.icon = icon
        self.titleColor = titleColor
        self.iconTintColor = iconTintColor
        self.backgroundColor = backgroundColor
        self.action = action
    }
    
    var body: some View {
        Button {
            action?()
        } label: {
            HStack {
                Text(title)
                    .font(Fonts.body)
                    .foregroundStyle(titleColor ?? colors.text)
                if let icon {
                    Spacer()
                    icon
                        .foregroundStyle(iconTintColor ?? colors.accent1)
                }
            }
            .padding()
            .frame(maxWidth: .infinity, minHeight: 50)
            .background(backgroundColor ?? colors.background2)
            .clipShape(RoundedRectangle(cornerRadius: 8))
        }
    }
}

#if DEBUG
private struct PreviewSample: View {
    
    private let colors: PadawanColors
    
    init(colors: PadawanColors = .tatooineDesert) {
        self.colors = colors
    }
    
    var body: some View {
        ZStack {
            colors.background
            VStack(spacing: 16) {
                FilledButton(
                    title: "Title"
                )
                
                FilledButton(
                    title: "With Icon",
                    icon: Image(systemName: "chevron.right.2")
                )
                
                FilledButton(
                    title: "Destructive",
                    titleColor: colors.text,
                    backgroundColor: colors.errorRed
                )
                
                FilledButton(
                    title: "Fully Customized",
                    icon: Image(systemName: "square.and.arrow.up"),
                    titleColor: .white,
                    iconTintColor: colors.text,
                    backgroundColor: colors.accent1
                )
            }
            .padding()
        }
        .ignoresSafeArea()
    }
}

#Preview("TatooineDesert") {
    PreviewSample(colors: .tatooineDesert)
}

#Preview("VaderDark") {
    PreviewSample(colors: .vaderDark)
}
#endif
