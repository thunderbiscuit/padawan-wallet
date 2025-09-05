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
    
    init(image: Image, size: CGFloat = 120, backgroundColor: Color = Color(red: 0.87, green: 0.79, blue: 0.68),
         cornerRadius: CGFloat = 20,
         verticalPadding: CGFloat = 20
    ) {
        self.image = image
        self.size = size
        self.backgroundColor = backgroundColor
    }
    
    var body: some View {
        ZStack {
            RoundedRectangle(cornerRadius: 30)
                .fill(backgroundColor)
            
            image
                .resizable()
                .scaledToFit()
                .frame(width: size, height: size)
                .padding(16)
        }
        .frame(width: size + 40, height: size + 40)
    }
}
