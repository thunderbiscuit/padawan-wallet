/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import SwiftUI

private struct WalletViewAssets {
    static var receiveButtonTitle = Strings.receive
    static var sendButtonTitle = Strings.send
    
    static var receiveButtonIcon: Image = Image(systemName: "arrow.down")
    static var sendButtonIcon: Image = Image(systemName: "arrow.up")
}

struct WalletView: View {
    @Environment(\.padawanColors) private var colors
    @StateObject private var viewModel: WalletViewModel
    
    init(
        path: Binding<NavigationPath>,
        bdkClient: BDKClient = .live
    ) {
        _viewModel = StateObject(wrappedValue: WalletViewModel(path: path, bdkClient: bdkClient))
    }
    
    var body: some View {
        BackgroundView {
            ScrollView {
                VStack(spacing: 24) {
                    BalanceCard(
                        balance: $viewModel.balance,
                        isSyncing: $viewModel.isSyncing
                    )
                    
                    buildSendReceiveButtons()
                    TransactionsCard(
                        list: $viewModel.transactions,
                        actionGetCoins: {
                            
                        }
                    )
                    .frame(minHeight: 200)
                    .frame(maxWidth: .infinity)
                }
                .padding()
            }
        }
        .task {
            viewModel.loadWallet()
            await viewModel.syncWallet()
        }
        .navigationDestination(for: WalletScreenNavigation.self) { item in
            switch item {
            case WalletScreenNavigation.receive:
                ReceiveScreen()
                
            case WalletScreenNavigation.send:
                SendScreen()
                
            default:
                EmptyView()
            }
        }
        .fullScreenCover(item: $viewModel.fullScreenCover) { item in
            switch item {
            case .alertError(let data):
                AlertModalView(data: data)
                    .background(BackgroundClearView())
                
            default:
                EmptyView()
            }
        }
    }
    
    @ViewBuilder
    private func buildSendReceiveButtons() -> some View {
        HStack(spacing: 16) {
            PadawanButton(
                title: WalletViewAssets.receiveButtonTitle,
                icon: WalletViewAssets.receiveButtonIcon,
                action: {
                    viewModel.showReceiveScreen()
                }
            )
            
            PadawanButton(
                title: WalletViewAssets.sendButtonTitle,
                icon: WalletViewAssets.sendButtonIcon,
                action: {
                    viewModel.showSendScreen()
                }
            )
        }
        .frame(minHeight: 50)
    }
}
