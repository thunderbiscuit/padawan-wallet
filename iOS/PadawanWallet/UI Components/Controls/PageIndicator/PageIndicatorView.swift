//
//  PageIndicatorView.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 05/10/25.
//

import SwiftUI

struct PageIndicatorView: View {
    @Environment(\.padawanColors) private var colors
    
    let currentIndex: Int
    let total: Int

    var body: some View {
        HStack(spacing: 8) {
            ForEach(0..<total, id: \.self) { index in
                Rectangle()
                    .fill(index <= currentIndex ?
                          colors.text : colors.background
                    )
                    .frame(
                        width: 60.0, height: 5.0
                    )
                    .animation(.easeInOut(duration: 0.3), value: currentIndex)
            }
        }
    }
}
