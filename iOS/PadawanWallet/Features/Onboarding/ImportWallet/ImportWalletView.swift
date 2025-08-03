//
//  ImportWalletView.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 28/07/25.
//

import SwiftUI

struct ImportWalletView: View {
    @Environment(\.padawanColors) private var colors
    
    private let viewModel: ImporViewModel
    
    init(
        path: Binding<NavigationPath>,
        bdkClient: BDKClient = .live
    ) {
        self.viewModel = .init(path: path, bdkClient: bdkClient)
    }
    
    var body: some View {
        VStack {
            Button {
                viewModel.importWallet()
            } label: {
                Text("Import Wallet")
            }
        }
        .navigationDestination(for: ImportWalletNavigation.self) { destination in
            switch destination {
            case .home:
                Text("Home")
            }
        }
    }
}

#if DEBUG
#Preview {
    ImportWalletView(path: .constant(.init()))
        .environment(\.padawanColors, .vaderDark)
}
#endif
