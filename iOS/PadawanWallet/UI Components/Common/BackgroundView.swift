//
//  BackgroundView.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 26/07/25.
//

import SwiftUI

struct BackgroundView<Content: View>: View {
    @Environment(\.padawanColors) private var colors
    
    @ViewBuilder private let content: () -> Content
    
    init(@ViewBuilder content: @escaping () -> Content) {
        self.content = content
    }
    
    var body: some View {
        ZStack {
            colors.background
                .ignoresSafeArea()
            
            content()
        }
    }
}

#if DEBUG
#Preview {
    BackgroundView {
        Text("Content")
    }
}
#endif
