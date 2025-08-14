/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import SwiftUI

private struct TransactionsCardAssets {
    static let title = Strings.transactions
    static let emptyStateDescription = Strings.transactionListEmpty
    static let emptyStateButton = Strings.getCoins
}

extension TransactionsCard {
    struct Data: Identifiable, Equatable {
        enum Status: String {
            case received
            case sent
            
            var icon: Image {
                switch self {
                case .received:
                    return Image(systemName: "arrow.down")
                    
                case .sent:
                    return Image(systemName: "arrow.up")
                    
                }
            }
            
            var signal: String {
                switch self {
                case .received:
                    return "+"
                    
                case .sent:
                    return "-"
                }
            }
        }
        
        let id: String
        let date: String
        let amount: String
        let status: Status
    }
}

struct TransactionsCard: View {
    @Environment(\.padawanColors) private var colors
    @Binding private var list: [TransactionsCard.Data]
    
    init(
        list: Binding<[TransactionsCard.Data]> = .constant([])
    ) {
        _list = list
    }
    
    var body: some View {
        if list.isEmpty {
            buildEmptyState()
        } else {
            buildList()
        }
    }
    
    @ViewBuilder
    private func buildEmptyState() -> some View {
        Text("Empty get coins")
    }
    
    @ViewBuilder
    private func buildList() -> some View {
        VStack(alignment: .leading, spacing: 12.0) {
            Text(TransactionsCardAssets.title)
                .font(Fonts.title)
                .foregroundStyle(colors.text)
            
            PadawanCardView(
                backgroundColor: colors.background2,
                content: {
                    ForEach(list) { item in
                        buildListItem(item)
                    }
                }
            )
        }
        .fixedSize(horizontal: false, vertical: true)
    }
    
    @ViewBuilder
    private func buildListItem(_ item: TransactionsCard.Data) -> some View {
        VStack(alignment: .leading, spacing: 6.0) {
            HStack {
                Text(item.id)
                    .font(Fonts.caption)
                    .foregroundStyle(colors.text)
                    .lineLimit(1)
                    .truncationMode(.middle)
                Text(item.amount)
                    .font(Fonts.body)
                    .foregroundStyle(colors.textFaded)
                    .padding(.leading, 30)
            }
            
            HStack {
                Text(item.date)
                    .font(Fonts.body)
                    .foregroundStyle(colors.textFaded)
                Spacer()
                buildTransactionStatus(item.status)
                    .padding(.leading, 30)
            }
            
            Divider()
                .padding(.top, 18)
        }
        .padding([.horizontal, .top], 24)
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .leading)
    }
    
    @ViewBuilder
    private func buildTransactionStatus(_ status: TransactionsCard.Data.Status) -> some View {
        ZStack {
            RoundedRectangle(cornerRadius: 8.0)
                .fill(Color.white.opacity(0.35))
            HStack {
                Text(status.rawValue)
                    .font(Fonts.font(.regular, 16))
                    .foregroundStyle(colors.text)
                
                status.icon
                    .resizable()
                    .frame(width: 12, height: 12)
                    .foregroundStyle(colors.textLight)
            }
            .padding(.vertical, 6)
            .padding(.horizontal, 10)
        }
        .fixedSize(horizontal: true, vertical: true)
    }
}

#if DEBUG
//#Preview("Empty") {
//    TransactionsCard(list: .constant([]))
//        .frame(height: 300)
//        .padding()
//}

#Preview("With transactions") {
    var transactions: Binding<[TransactionsCard.Data]> = .init {
        [
            .init(id: "123123123123123123123123123123123123123123123123123121", date: "August 9 2025 09:11", amount: "+150000 sats", status: .received),
            .init(id: "123123123123123123123123123123123123123123123123123122", date: "August 9 2025 09:11", amount: "+150000 sats", status: .sent),
            .init(id: "123123123123123123123123123123123123123123123123123123", date: "August 9 2025 09:11", amount: "+150000 sats", status: .received),
        ]
    } set: { _ in }

    TransactionsCard(list: transactions)
        .padding()
}
#endif
