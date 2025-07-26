//
//  PadawanCardView.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 26/07/25.
//

import SwiftUI

struct PadawanCardView<Content: View>: View {
    @Environment(\.padawanColors) private var colors
    @ViewBuilder private let content: () -> Content
    
    private let backgroundColor: Color
    
    init(
        backgroundColor: Color,
        @ViewBuilder content: @escaping () -> Content
    ) {
        self.backgroundColor = backgroundColor
        self.content = content
    }
    
    var body: some View {
        ZStack {
            RoundedRectangle(cornerRadius: 12)
                .fill(colors.cardShadowColor)
                .offset(x: 4, y: 4)
            
            VStack {
                content()
            }
            .padding()
            .frame(maxWidth: .infinity, maxHeight: .infinity)
            .background(backgroundColor)
            .cornerRadius(12)
            .overlay(
                RoundedRectangle(cornerRadius: 12)
                    .stroke(colors.cardShadowColor, lineWidth: 2)
            )
        }
        .contentShape(Rectangle())
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
}

#if DEBUG
private struct PreviewSample: View {
    
    @Environment(\.padawanColors) private var colors
    
    var body: some View {
        ZStack {
            colors.background
            
            VStack(spacing: 16) {
                PadawanCardView(
                    backgroundColor: colors.accent3
                ) {
                    Text("Some card")
                }
                .frame(height: 180)
                .foregroundStyle(colors.text)
                
                PadawanCardView(
                    backgroundColor: colors.background
                ) {
                    VStack(alignment: .leading) {
                        Text("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.")
                        Spacer().frame(height: 30)
                        PadawanCardView(
                            backgroundColor: colors.accent1
                        ) {
                            Text("Get me a button")
                        }
                        .frame(maxWidth: 160)
                        .frame(height: 60)
                        .foregroundStyle(colors.text)
                    }
                    .padding()
                    .frame(maxWidth: .infinity, alignment: .leading)
                }
                .frame(maxHeight: 320)
                .foregroundStyle(colors.text)
                
                PadawanCardView(
                    backgroundColor: colors.accent1
                ) {
                    Text("Almost button!")
                }
                .frame(height: 80)
                .foregroundStyle(colors.text)
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
