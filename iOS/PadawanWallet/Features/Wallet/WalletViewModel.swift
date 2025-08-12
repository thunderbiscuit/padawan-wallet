//
//  WalletViewModel.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 09/08/25.
//

import SwiftUI

final class WalletViewModel: ObservableObject {
    
    @Binding var path: NavigationPath
    @Published var sheetScreen: WelcomeScreenNavigation?
    @Published var fullScreenCover: ImportWalletNavigation?
    
    @Published var isSyncing: Bool = false
    @Published var balance: UInt64 = 0
    @Published var transactions: [TransactionsCard.Data] = []
    
    private let bdkClient: BDKClient
    
    private var updateProgress: @Sendable (UInt64, UInt64) -> Void {
        { inspected, total in
            print("progress: \(total > 0 ? Float(inspected) / Float(total) : 0)")
        }
    }

    private var updateProgressFullScan: @Sendable (UInt64) -> Void {
        { inspected in
            print("inspected: \(inspected)")
        }
    }
    
    init(
        path: Binding<NavigationPath>,
        bdkClient: BDKClient
    ) {
        _path = path
        self.bdkClient = bdkClient
    }
    
    func loadWallet() {
        do {
            try bdkClient.loadWallet()
            balance = Session.shared.lastBalanceUpdate
            try getTransactions()
            
        } catch {
            fullScreenCover = .alertError(
                data: .init(
                    title: "Error",
                    subtitle: error.localizedDescription
                )
            )
        }
    }
    
    @MainActor
    func syncWallet() async {
        defer {
            isSyncing = false
        }
        guard Session.shared.walletExists() else {
            return
        }
        isSyncing = true
        if bdkClient.needsFullScan() {
            await fullSync()
        } else {
            await incrementalSync()
        }
        
        do {
            balance = try self.bdkClient.getBalance().total.toSat()
            Session.shared.lastBalanceUpdate = balance
        } catch {
            fullScreenCover = .alertError(
                data: .init(title: "Error", subtitle: error.localizedDescription)
            )
        }
    }
    
    func showReceiveScreen() {
        path.append(WalletScreenNavigation.receive)
    }
    
    func showSendScreen() {
        path.append(WalletScreenNavigation.send)
    }
    
    func getTransactions() throws {
        let detailsTransactions: [TransactionsCard.Data] = try bdkClient.transactions().compactMap { item in
            let status: TransactionsCard.Data.Status = item.balanceDelta > .zero ? .received : .sent
            let amount = "\(UInt64(item.balanceDelta).formattedSats()) sats"
            var date: Date = .init()
            switch item.chainPosition {
            case .confirmed(let confirmationBlockTime, _):
                date = confirmationBlockTime
                    .confirmationTime
                    .toDate()
                
            case .unconfirmed(let timestamp):
                date = timestamp?
                    .toDate() ?? .init()
            }
            
            return .init(
                id: item.txid.description,
                date: date.formatted(date: .abbreviated, time: .shortened),
                amount: amount,
                status: status
            )
        }
        transactions = detailsTransactions
    }
    
    // MARK: - Private
    
    private func fullSync() async {
        do {
            let inspector = WalletFullScanScriptInspector(updateProgress: updateProgressFullScan)
            try await bdkClient.fullScanWithInspector(inspector)
            Session.shared.isFullScanRequired = false
            
        } catch {
            fullScreenCover = .alertError(
                data: .init(title: "Error", subtitle: error.localizedDescription)
            )
        }
    }
    
    private func incrementalSync() async {
        do {
            let inspector = WalletSyncScriptInspector(updateProgress: updateProgress)
            try await bdkClient.syncWithInspector(inspector)
            
        } catch BDKServiceError.needResync {
            await syncWallet()
            
        } catch {
            fullScreenCover = .alertError(
                data: .init(title: "Error", subtitle: error.localizedDescription)
            )
        }
    }
}
