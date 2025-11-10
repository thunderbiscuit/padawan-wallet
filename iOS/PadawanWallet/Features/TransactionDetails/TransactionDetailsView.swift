//
//  TransactionDetailsView.swift
//  PadawanWallet
//
//  Created by Vinicius Silva Moreira on 06/11/25.
//

import SwiftUI
import BitcoinDevKit

struct TransactionDetailsView: View {
    @EnvironmentObject private var languageManager: LanguageManager
    @StateObject private var viewModel: TransactionDetailsViewModel
    @Environment(\.padawanColors) private var colors
    
    init(transaction: BitcoinDevKit.TxDetails) {
        _viewModel = StateObject(wrappedValue: TransactionDetailsViewModel(transaction: transaction))
    }
    
    var body: some View {
        BackgroundView {
            VStack(spacing: 25) {
                ScrollView {
                    VStack(alignment: .leading, spacing: 18) {
                        ForEach(viewModel.detailItems) { item in
                            buildDetailView(for: item)
                        }
                    }
                    .padding()
                }
                .navigationTitle(languageManager.localizedString(forKey: "transaction_details"))
                .navigationBarTitleDisplayMode(.inline)
            }
        }
    }
    
    @ViewBuilder
    private func buildDetailView(for item: TransactionDetailsViewModel.TransactionDetailItem) -> some View {
        VStack(alignment: .leading) {
            HStack(alignment: .top) {
                Text(languageManager.localizedString(forKey: item.key))
                    .font(Fonts.body)
                    .lineLimit(1)
                    .minimumScaleFactor(0.7)
                    .layoutPriority(1)
                
                Spacer()
                
                switch item.displayType {
                case .link:
                    if let url = item.url {
                        Link(destination: url) {
                            (
                                Text(item.value) +
                                Text("(mempool.space)")
                            )
                            .font(Fonts.body)
                            .multilineTextAlignment(.trailing)
                        }
                        .tint(Color.orange)
                    }
                    
                case .badge:
                    Text(languageManager.localizedString(forKey: item.value))
                        .padding(.horizontal, 10)
                        .padding(.vertical, 4)
                        .background(
                            RoundedRectangle(cornerRadius: 8)
                                .fill(colors.accent1)
                        )
                    
                case .standard:
                    Text(languageManager.localizedString(forKey: item.value))
                        .font(Fonts.body)
                        .multilineTextAlignment(.trailing)
                }
            }
            
            if item.addDivider {
                Divider()
                    .padding(.horizontal)
                    .padding(.top, 10)
            }
        }
    }
}
