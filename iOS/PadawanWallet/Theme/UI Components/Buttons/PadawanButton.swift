//
//  PadawanButton.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 25/07/25.
//

import SwiftUI

struct PadawanButton: View {
    @Environment(\.padawanColors) private var colors
    
    private let title: String
    private let icon: Image?
    
    private let action: (() -> Void)?
    
    init(
        title: String,
        icon: Image? = nil,
        action: (() -> Void)? = nil
    ) {
        self.title = title
        self.icon = icon
        self.action = action
    }
    
    var body: some View {
        Button {
            action?()
        } label: {
            ZStack {
                RoundedRectangle(cornerRadius: 12)
                    .fill(.black)
                    .offset(x: 4, y: 4)
                
                HStack {
                    buildContent()
                }
                .foregroundColor(colors.text)
                .padding()
                .frame(maxWidth: .infinity, maxHeight: 80)
                .background(colors.accent2)
                .cornerRadius(12)
                .overlay(
                    RoundedRectangle(cornerRadius: 12)
                        .stroke(.black, lineWidth: 2)
                )
            }
            .contentShape(Rectangle())
            .frame(maxHeight: 80)
        }
    }
    
    @ViewBuilder
    private func buildContent() -> some View {
        Text(title)
            .font(Fonts.caption)
        if let icon {
            icon
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
            
            PadawanButton(
                title: "With Icon",
                icon: Image(systemName: "bitcoinsign")
            )
        }
        .padding()
    }
}

#Preview {
    PreviewSample()
}
#endif
