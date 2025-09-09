//
//  PadawanCardIconView.swift
//  PadawanWallet
//
//  Created by Vinicius Silva Moreira on 05/09/25.
//

import SwiftUI

struct PadawanCardIconView: View {
    let image: Image
    let size: CGFloat
    let backgroundColor: Color
    let hasBorder: Bool
    
    init(
        image: Image,
        size: CGFloat = 120,
        backgroundColor: Color = Color(red: 0.87, green: 0.79, blue: 0.68),
        cornerRadius: CGFloat = 20,
        verticalPadding: CGFloat = 20,
        hasBorder: Bool = true
    ) {
        self.image = image
        self.size = size
        self.backgroundColor = backgroundColor
        self.hasBorder = hasBorder
    }
    
    var body: some View {
        ZStack {
            if hasBorder {
                RoundedRectangle(cornerRadius: 30)
                    .fill(backgroundColor)
            }
            
            image
                .resizable()
                .scaledToFit()
                .frame(width: size, height: size)
                .padding(16)
        }
        .frame(width: size + 80, height: size + 40)
    }
}
