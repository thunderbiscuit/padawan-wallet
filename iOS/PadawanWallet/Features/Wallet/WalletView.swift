/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import SwiftUI

private struct WalletViewAssets {
    static var receiveButtonTitle: String { LanguageManager.shared.localizedString(forKey: "receive") }
    static var sendButtonTitle: String { LanguageManager.shared.localizedString(forKey: "send") }
    static var accessibilitySendButton: String { LanguageManager.shared.localizedString(forKey: "accessibility_send_hint") }
    static var accessibilityReceiveButton: String { LanguageManager.shared.localizedString(forKey: "accessibility_receive_hint") }
    
    static var receiveButtonIcon: Image = Image(systemName: "arrow.down")
    static var sendButtonIcon: Image = Image(systemName: "arrow.up")
}

struct WalletView: View {
    @Environment(\.padawanColors) private var colors
    @EnvironmentObject private var languageManager: LanguageManager
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
                        isSyncing: $viewModel.isSyncing,
                        actionSyncWallet: {
                            Task {
                                await viewModel.syncWallet()
                            }
                        }
                    )
                    
                    buildSendReceiveButtons()
                    TransactionsCard(
                        list: $viewModel.transactions,
                        isSyncing: $viewModel.isSyncing,
                        actionGetCoins: { completion in
                            viewModel.getFaucetCoinsConfirmation {
                                completion()
                            }
                        },
                        onTransactionTapped: { txid in
                            viewModel.showTransactionDetails(txid: txid)
                        }
                    )
                    .frame(minHeight: 200)
                    .frame(maxWidth: .infinity)
                }
                .frame(maxWidth: .maxWidthScreen)
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
                ReceiveTransactionView(
                    path: viewModel.$path,
                    bdkClient: viewModel.bdkClient
                )
                
            case WalletScreenNavigation.send:
                SendTransactionView(
                    path: viewModel.$path,
                    bdkClient: viewModel.bdkClient
                )
                
            case .transactionDetails(let txid):
                
                if let transaction = viewModel.getTransaction(by: txid) {
                    let detailsViewModel = TransactionDetailsViewModel(transaction: transaction)
                    TransactionDetailsView(transaction: transaction)
                } else {
                    EmptyView()
                }
                
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
            .accessibilityLabel(Text(WalletViewAssets.receiveButtonTitle))
            .accessibilityHint(Text(WalletViewAssets.accessibilityReceiveButton))
            
                PadawanButton(
                title: WalletViewAssets.sendButtonTitle,
                icon: WalletViewAssets.sendButtonIcon,
                action: {
                    viewModel.showSendScreen()
                }
            )
            .accessibilityLabel(Text(WalletViewAssets.sendButtonTitle))
            .accessibilityHint(Text(WalletViewAssets.accessibilitySendButton))
        }
        .frame(minHeight: 50)
    }
}
