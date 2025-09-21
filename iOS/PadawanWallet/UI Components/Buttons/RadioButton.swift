//
//  RadioButton.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 21/09/25.
//

import SwiftUI

struct RadioBoxView: View {
    @Environment(\.padawanColors) private var colors

    private let selectedBackground: Color = .clear
    private let unselectedBackground: Color = .clear

    private let selectedImage: Image = Image(systemName: "circle.fill")

    @Binding var isSelected: Bool

    init(
        isSelected: Binding<Bool>
    ) {
        _isSelected = isSelected
    }

    var body: some View {
        ZStack {
            Group {
                Circle()
                    .fill(isSelected ? selectedBackground : unselectedBackground)
                    .overlay(
                        Circle()
                            .stroke(isSelected ? colors.accent2 : colors.text, lineWidth: 2.0)
                    )
                    .frame(width: 24, height: 24)
                if isSelected {
                    selectedImage
                        .resizable()
                        .frame(width: 16, height: 16)
                        .foregroundStyle(colors.accent2)
                }
            }
        }
        .contentShape(Rectangle())
        .onTapGesture {
            isSelected.toggle()
        }
    }
}

#if DEBUG
#Preview {
    BackgroundView {
        VStack {
            HStack(spacing: 12) {
                RadioBoxView(isSelected: .constant(false))
                Text("Unselected RadioBox")
                    .font(Fonts.font(.regular, 18))
                    .foregroundStyle(.black)
            }
            
            HStack(spacing: 12) {
                RadioBoxView(isSelected: .constant(true))
                Text("Selected RadioBox")
                    .font(Fonts.font(.regular, 18))
                    .foregroundStyle(.black)
            }
        }
    }
}
#endif
