//
//  TransactionDetailsViewModel.swift
//  PadawanWallet
//
//  Created by Vinicius Silva Moreira on 06/11/25.
//

import SwiftUI
import BitcoinDevKit

final class TransactionDetailsViewModel: ObservableObject {
    
    @Published var detailItems: [TransactionDetailItem] = []
    
    enum DisplayType {
        case standard
        case link
        case badge
    }
    
    struct TransactionDetailItem: Identifiable {
        let id = UUID()
        let key: String
        let value: String
        var displayType: DisplayType = .standard
        var addDivider: Bool = false
        var url: URL? = nil
    }
    
    struct TransactionDetailsKeys {
        static let balanceDelta = "balance_delta"
        static let transactionType = "transaction_type"
        static let time = "time"
        static let block = "block"
        static let txid = "txid"
        static let feesPaid = "fees_paid"
    }
    
    private let transaction: BitcoinDevKit.TxDetails
    
    init(transaction: BitcoinDevKit.TxDetails) {
        self.transaction = transaction
        buildDetailItems()
    }
    
    private func buildDetailItems() {
        var items: [TransactionDetailItem] = []
        
        let balanceValue = transaction.balanceDelta
        var prefix = ""
        
        if balanceValue > 0 {
            prefix = "+"
        }
        let balanceString = "\(prefix)\(balanceValue) sats"
        items.append(TransactionDetailItem(key: TransactionDetailsKeys.balanceDelta,
                                           value: balanceString, displayType: .standard))
        
        let typeString: String
        if balanceValue > 0 {
            typeString = "receive"
        } else {
            typeString = "sent"
        }
        items.append(TransactionDetailItem(key: TransactionDetailsKeys.transactionType,
                                           value: typeString, displayType: .badge))
        
        if items.count > 1 {
            items[1].addDivider = true
        }
        
        switch transaction.chainPosition {
        case .unconfirmed(let timestamp):
            
            let timeString = formatTimestamp(timestamp)
            items.append(TransactionDetailItem(key: TransactionDetailsKeys.time, value: timeString, displayType: .standard))
            
        case .confirmed(let blockTime, _):
            
            let date = blockTime.confirmationTime.toDate()
            let timeString = formatDate(date)
            
            let blockString = String(blockTime.blockId.height)
            items.append(TransactionDetailItem(key: TransactionDetailsKeys.time, value: timeString, displayType: .standard))
            items.append(TransactionDetailItem(key: TransactionDetailsKeys.block, value: blockString, displayType: .standard))
        }
        
        let txidString = transaction.txid.description
        items.append(TransactionDetailItem(key: TransactionDetailsKeys.txid,
                                           value: txidString,
                                           displayType: .link,
                                           url: buildURL(txid: txidString)))
        
        let fees: UInt64
        if let feeAmount = transaction.fee {
            fees = feeAmount.toSat()
        } else {
            fees = 0
        }
        items.append(TransactionDetailItem(key: TransactionDetailsKeys.feesPaid,
                                           value: "\(fees) sats", displayType: .standard))
        
        self.detailItems = items
    }
    
    private func formatTimestamp(_ timestamp: UInt64?) -> String {
        guard let timestamp = timestamp else { return "pending" }
        let date = Date(timeIntervalSince1970: TimeInterval(timestamp))
        return formatDate(date)
    }
    
    private func formatDate(_ date: Date) -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "MMMM d yyyy HH:mm"
        return dateFormatter.string(from: date)
    }
    private func buildURL(txid: String) -> URL? {
        let urlString = "https://mempool.space/signet/tx/\(txid)"
        return URL(string: urlString)
    }
}
