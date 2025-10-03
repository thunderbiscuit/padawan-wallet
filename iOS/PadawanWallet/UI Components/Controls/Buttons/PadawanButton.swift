//
//  PadawanButton.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 25/07/25.
//

import SwiftUI

struct PadawanButton: View {
    @Environment(\.padawanColors) private var colors
    
    @Binding private var isLoading: Bool
    
    static let maxHeight: CGFloat = 110
    
    private let title: String?
    private let icon: Image?
    private let isDestructive: Bool
    
    private let action: (() -> Void)?
    
    init(
        title: String? = nil,
        icon: Image? = nil,
        isLoading: Binding<Bool> = .constant(false),
        isDestructive: Bool = false,
        action: (() -> Void)? = nil
    ) {
        _isLoading = isLoading
        self.title = title
        self.icon = icon
        self.action = action
        self.isDestructive = isDestructive
    }
    
    var body: some View {
        Group {
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
        .frame(minHeight: 30.0)
    }
    
    @ViewBuilder
    private func buildContent() -> some View {
        HStack {
            if isLoading {
                ProgressView()
            } else {
                if let title  {
                    Text(title)
                        .font(Fonts.caption)
                }
            }
            if let icon {
                icon
            }
        }
        .padding(.vertical)
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
                title: "Title",
                isLoading: .constant(true)
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
