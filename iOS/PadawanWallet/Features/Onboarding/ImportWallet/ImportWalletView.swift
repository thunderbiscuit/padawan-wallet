//
//  ImportWalletView.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 28/07/25.
//

import SwiftUI

struct ImportWalletView: View {
    
    private let viewModel: ImporViewModel
    
    init(
        viewModel: ImporViewModel = .init(path: .constant(.init()))
    ) {
        self.viewModel = viewModel
    }
    
    var body: some View {
        VStack {
            Button {
                viewModel.showHome()
            } label: {
                Text("Home")
            }
        }
        .navigationDestination(for: ImportNavigation.self) { destination in
            switch destination {
            case .home:
                Text("Home")
            }
        }
    }
}

#Preview {
    ImportWalletView()
}
