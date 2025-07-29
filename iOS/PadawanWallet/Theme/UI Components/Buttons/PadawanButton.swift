//
//  PadawanButton.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 25/07/25.
//

import SwiftUI

struct PadawanButton: View {
    @Environment(\.padawanColors) private var colors
    
    static let maxHeight: CGFloat = 110
    
    private let title: String
    private let icon: Image?
    private let isDestructive: Bool
    
    private let action: (() -> Void)?
    
    init(
        title: String,
        icon: Image? = nil,
        action: (() -> Void)? = nil,
        isDestructive: Bool = false
    ) {
        self.title = title
        self.icon = icon
        self.action = action
        self.isDestructive = isDestructive
    }
    
    var body: some View {
        let bgColor = isDestructive ? colors.accent3 : colors.accent1
        if let action {
            Button {
                action()
            } label: {
                PadawanCardView(backgroundColor: bgColor) {
                    buildContent()
                }
                .foregroundColor(colors.text)
            }
        } else {
            PadawanCardView(backgroundColor: bgColor) {
                buildContent()
            }
            .foregroundColor(colors.text)
        }
    }
    
    @ViewBuilder
    private func buildContent() -> some View {
        HStack {
            Text(title)
                .font(Fonts.caption)
            if let icon {
                icon
            }
        }
    }
}

#if DEBUG
private struct PreviewSample: View {
    var body: some View {
        VStack(spacing: 16) {
            PadawanButton(
                title: "Title"
            )
            .frame(height: 80)
            
            PadawanButton(
                title: "With Icon",
                icon: Image(systemName: "bitcoinsign")
            )
            .frame(height: 80)
            
            PadawanButton(
                title: "Title",
                isDestructive: true
            )
            .frame(height: 80)
        }
        .padding()
    }
}

#Preview {
    PreviewSample()
}
#endif
