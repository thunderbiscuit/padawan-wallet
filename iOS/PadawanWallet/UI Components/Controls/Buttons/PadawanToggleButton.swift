//
//  PadawanToggleButton.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 26/07/25.
//

import SwiftUI

struct PadawanToggleButton: View {
    @Environment(\.padawanColors) private var colors
    
    private let title: String
    private let isOn: Bool
    
    private let action: (() -> Void)?
    
    init(
        title: String,
        isOn: Bool = false,
        action: (() -> Void)? = nil
    ) {
        self.title = title
        self.isOn = isOn
        self.action = action
    }
    
    var body: some View {
        Button {
            action?()
        } label: {
            PadawanCardView(
                backgroundColor: isOn ? colors.errorRed : colors.background2
            ) {
                HStack {
                    Text(title)
                        .font(Fonts.body)
                        .foregroundStyle(colors.text)
                    Spacer()
                    if isOn {
                        buildStart()
                    } else {
                        Circle()
                            .stroke(colors.text, lineWidth: 3)
                            .frame(width: 24, height: 24)
                    }
                }
                .padding()
                .frame(maxWidth: .infinity, alignment: .leading)
            }
        }
    }
    
    @ViewBuilder
    private func buildStart() -> some View {
        let imageName = "star.fill"
        ZStack {
            Image(systemName: imageName)
                .resizable()
                .frame(width: 28, height: 28)
                .foregroundColor(colors.cardShadowColor)
                .offset(x: 2, y: 2)
            
            Image(systemName: imageName)
                .resizable()
                .frame(width: 28, height: 28)
                .foregroundColor(colors.accent1)
                
        }
    }
}

#if DEBUG
private struct PreviewSample: View {
    
    @Environment(\.padawanColors) private var colors
    
    var body: some View {
        ZStack {
            colors.background
            VStack(spacing: 16) {
                PadawanToggleButton(
                    title: "1. Lesson One",
                    isOn: true
                )
                .frame(height: 80)
                
                PadawanToggleButton(
                    title: "2. Lesson two"
                )
                .frame(height: 80)
            }
            .padding()
        }
        .ignoresSafeArea()
    }
}

#Preview("TatooineDesert") {
    PreviewSample()
        .environment(\.padawanColors, .tatooineDesert)
}

#Preview("VaderDark") {
    PreviewSample()
        .environment(\.padawanColors, .vaderDark)
}
#endif
