/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import SwiftUI

struct WalletRootView: View {
    @Environment(\.padawanColors) private var colors
    @StateObject private var viewModel: WalletViewModel
    
    init(
        path: Binding<NavigationPath>,
        bdkClient: BDKClient = .live
    ) {
        _viewModel = StateObject(wrappedValue: WalletViewModel(path: path, bdkClient: bdkClient))
    }
    
    var body: some View {
        NavigationStack {
            ScrollView {
                VStack(spacing: 24) {
                    BalanceCard(
                        balance: $viewModel.balance,
                        isSyncing: $viewModel.isSyncing
                    )
                    SendReceiveButtons()
                    TransactionsCard()
                }
                .padding()
                .background(colors.background)
            }
            .background(colors.background)
        }
        .task {
            viewModel.loadWallet()
            await viewModel.syncWallet()
            print($viewModel.balance.wrappedValue)
        }
    }
}
