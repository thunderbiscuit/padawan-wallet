//
//  SectionTitleView.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 03/10/25.
//

import SwiftUI

struct SectionTitleView: View {
    
    @Environment(\.padawanColors) private var colors
    
    private let title: String
    
    init(_ title: String) {
        self.title = title
    }
    
    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text(title)
                .font(Fonts.font(.semibold, 20))
                .foregroundStyle(colors.text)
        
            Rectangle()
                .frame(height: 1)
                .foregroundStyle(colors.text)
        }
        .frame(maxWidth: .infinity, alignment: .leading)
    }
}

#if DEBUG
#Preview {
    SectionTitleView("Title 1")
}
#endif
