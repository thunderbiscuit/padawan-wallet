/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import SwiftUI

private struct TransactionsCardAssets {
    static var title: String { LanguageManager.shared.localizedString(forKey: "transactions") }
    static var emptyStateDescription: String { LanguageManager.shared.localizedString(forKey: "transaction_list_empty") }
    static var emptyStateButton: String { LanguageManager.shared.localizedString(forKey: "get_coins") }
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
        let confirmed: Bool
    }
}

struct TransactionsCard: View {
    @Environment(\.padawanColors) private var colors
    @EnvironmentObject private var languageManager: LanguageManager
    @Binding private var list: [TransactionsCard.Data]
    @Binding private var isSyncing: Bool
    
    private let actionGetCoins: (_ completion: @escaping () -> Void) -> Void
    private var isFirsLoading = true
    private let onTransactionTapped: ((String) -> Void)?
    
    init(
        list: Binding<[TransactionsCard.Data]> = .constant([]),
        isSyncing: Binding<Bool> = .constant(false),
        actionGetCoins: @escaping (_ completion: @escaping () -> Void) -> Void,
            onTransactionTapped: ((String) -> Void)? = nil
    ) {
        _list = list
        _isSyncing = isSyncing
        self.actionGetCoins = actionGetCoins
                self.onTransactionTapped = onTransactionTapped
    }
    
    var body: some View {
        VStack(alignment: .leading, spacing: 12.0) {
            buildHeader()
            if list.isEmpty {
                buildEmptyState()
            } else {
                buildList()
            }
        }
    }
    
    @ViewBuilder
    private func buildHeader() -> some View {
        Text(TransactionsCardAssets.title)
            .font(Fonts.title)
            .foregroundStyle(colors.text)
    }
    
    @ViewBuilder
    private func buildEmptyState() -> some View {
        PadawanCardView(
            backgroundColor: colors.background) {
                VStack(alignment: .leading, spacing: 24) {
                    Text(TransactionsCardAssets.emptyStateDescription)
                        .font(Fonts.body)
                        .foregroundStyle(colors.text)
                    
                    PadawanButton(
                        title: TransactionsCardAssets.emptyStateButton,
                        isLoading: $isSyncing
                    ) {
                        actionGetCoins {
                            self.isSyncing = false
                        }
                    }
                    .frame(width: 180, height: 60)
                        
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .leading)
                .padding(24)
            }
    }
    
    @ViewBuilder
    private func buildList() -> some View {
        PadawanCardView(
            backgroundColor: colors.background2,
            content: {
                ForEach(list) { item in
                    buildListItem(item)
                        .onTapGesture {
                    onTransactionTapped?(item.id)
                    }
                }
            }
        )
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
        .opacity(item.confirmed ? 1.0 : 0.7)
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
#Preview("Empty") {
    TransactionsCard(list: .constant([]),actionGetCoins: { _ in},
                     onTransactionTapped: { txid in print("Preview tap: \(txid)") })
        .padding()
        .environment(\.padawanColors, PadawanColorTheme.tatooine.colors)
        .environmentObject(LanguageManager.shared)
}

#Preview("With transactions") {
    var transactions: Binding<[TransactionsCard.Data]> = .init {
        [
            .init(
                id: "123123123123123123123123123123123123123123123123123121",
                date: "August 9 2025 09:11",
                amount: "+150000 sats",
                status: .received,
                confirmed: false
            ),
        ]
    } set: { _ in }
    
    TransactionsCard(list: transactions,actionGetCoins: { _ in },
                     onTransactionTapped: { txid in print("Preview tap: \(txid)") })
        .padding()
        .environment(\.padawanColors, PadawanColorTheme.tatooine.colors)
        .environmentObject(LanguageManager.shared)
}
#endif
