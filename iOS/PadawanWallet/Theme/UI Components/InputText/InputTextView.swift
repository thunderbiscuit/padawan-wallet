//
//  InputTextView.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 03/08/25.
//

import SwiftUI

struct InputTextView: View {
    @Environment(\.padawanColors) private var colors
    @Binding var text: String
    
    private let heightRect: CGFloat = 50
    
    private let label: String
    private var placeholder: String
    private var autocorrectionDisabled: Bool
    private var autocapitalization: TextInputAutocapitalization
    
    init(
        text: Binding<String>,
        label: String,
        placeholder: String,
        autocorrectionDisabled: Bool = false,
        autocapitalization: TextInputAutocapitalization = .sentences
    ) {
        _text = text
        self.label = label
        self.placeholder = placeholder
        self.autocorrectionDisabled = autocorrectionDisabled
        self.autocapitalization = autocapitalization
    }
    
    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text(label)
                .font(Fonts.font(.regular, 16))
            ZStack(alignment: .leading) {
                RoundedRectangle(cornerRadius: 8.0)
                    .stroke(colors.darkBackground, lineWidth: 1)
                    .frame(height: heightRect)
                    .zIndex(0)
                
                TextField(
                    placeholder,
                    text: $text
                )
                .font(Fonts.body)
                .padding(.horizontal, 12)
                .autocorrectionDisabled(autocorrectionDisabled)
                .textInputAutocapitalization(autocapitalization)
            }
            .frame(maxWidth: .infinity)
        }
    }
}

#if DEBUG
private struct PreviewSamples: View {
    @State private var text = ""
    
    var body: some View {
        BackgroundView {
            VStack {
                InputTextView(
                    text: $text,
                    label: "Label",
                    placeholder: "Placebolder"
                )
                .environment(\.padawanColors, .tatooineDesert)
            }.padding()
        }
    }
}

#Preview {
    PreviewSamples()
    
}
#endif
