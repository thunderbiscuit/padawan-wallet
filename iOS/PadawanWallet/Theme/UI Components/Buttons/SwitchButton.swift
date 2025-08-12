//
//  SwitchButton.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 12/08/25.
//

import SwiftUI

struct SwitchButton: View {
    @Environment(\.padawanColors) private var colors
    
    private let options: [String]
    @Binding var currentOption: String
    
    init(
        options: [String],
        currentOption: Binding<String>
    ) {
        self.options = options
        _currentOption = currentOption
    }
    
    var body: some View {
        ZStack {
            RoundedRectangle(cornerRadius: 12)
                .fill(colors.background2)
            
            HStack {
                ForEach(options, id: \.self) { option in
                    Button {
                        currentOption = option
                    } label: {
                        Text(option)
                            .font(Fonts.body)
                            .foregroundColor(
                                currentOption != option ?
                                    colors.text.opacity(0.3) :
                                    colors.text
                            )
                    }
                }
            }
            .padding(.all, 12)
        }
        .fixedSize(horizontal: true, vertical: true)
    }
}

#if DEBUG
#Preview {
    SwitchButton(
        options: ["option 1", "option 2"],
        currentOption: .constant("option 1")
    )
    .environment(\.padawanColors, .tatooineDesert)
}
#endif
