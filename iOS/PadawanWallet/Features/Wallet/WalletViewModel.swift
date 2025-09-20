//
//  WalletViewModel.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 09/08/25.
//

import SwiftUI
import Foundation

@MainActor
final class WalletViewModel: ObservableObject {
    
    @Binding var path: NavigationPath

    @Published var fullScreenCover: WalletScreenNavigation?
    
    @Published var isSyncing: Bool = false
    @Published var balance: UInt64 = 0
    @Published var transactions: [TransactionsCard.Data] = []
    
    let bdkClient: BDKClient
    
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
            
        } catch {
            fullScreenCover = .alertError(
                data: .init(
                    title: Strings.genericTitleError,
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
            try getTransactions()
        } catch {
            fullScreenCover = .alertError(
                data: .init(title: Strings.genericTitleError, subtitle: error.localizedDescription)
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
        let bdkTransactions = try bdkClient.transactions()
        
        let detailsTransactions: [TransactionsCard.Data] = bdkTransactions.compactMap { item in
            let status: TransactionsCard.Data.Status = item.balanceDelta > .zero ? .received : .sent
            let amount = "\(UInt64(abs(item.balanceDelta)).formattedSats()) sats"
            var formattedDate: String = ""
            var isConfirmed = false
            switch item.chainPosition {
            case .confirmed(let confirmationBlockTime, _):
                formattedDate = confirmationBlockTime
                    .confirmationTime
                    .toDate()
                    .formatted(date: .abbreviated, time: .shortened)
                isConfirmed = true
                
            case .unconfirmed(let timestamp):
                formattedDate = Strings.pending
                isConfirmed = false
            }
            
            return .init(
                id: item.txid.description,
                date: formattedDate,
                amount: amount,
                status: status,
                confirmed: isConfirmed
            )
        }
        transactions = detailsTransactions
    }
    
    func getFaucetCoins() async {
        do {
            let newAddress = try bdkClient.getAddress()
            try await getCoins(address: newAddress)
            // Wait 6 seconds for the transaction to appear on the blockchain before returning user feedback
            try await Task.sleep(nanoseconds: 6_000_000_000)
            await syncWallet()
        } catch {
            fullScreenCover = .alertError(
                data: .init(
                    title: Strings.genericTitleError,
                    subtitle: error.localizedDescription
                )
            )
        }
    }
    
    // MARK: - Private
    
    private func getCoins(address: String) async throws {
        guard let apiURL = Bundle.main.object(forInfoDictionaryKey: "FAUCET_URL") as? String,
              let token = Bundle.main.object(forInfoDictionaryKey: "FAUCET_TOKEN") as? String else {
            throw URLError(.badURL)
        }
        
        guard let url = URL(string: apiURL) else {
            throw URLError(.badURL)
        }
        
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.httpBody = address.data(using: .utf8)
        
        request.setValue("Bearer \(token)", forHTTPHeaderField: "Authorization")
        
        let (_, response) = try await URLSession.shared.data(for: request)
        guard let httpResponse = response as? HTTPURLResponse,
              (200..<300).contains(httpResponse.statusCode) else {
            throw URLError(.badServerResponse)
        }
    }
    
    private func fullSync() async {
        do {
            let inspector = WalletFullScanScriptInspector(updateProgress: updateProgressFullScan)
            try await bdkClient.fullScanWithInspector(inspector)
            Session.shared.isFullScanRequired = false
            
        } catch {
            fullScreenCover = .alertError(
                data: .init(title: Strings.genericTitleError, subtitle: error.localizedDescription)
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
                data: .init(title: Strings.genericTitleError, subtitle: error.localizedDescription)
            )
        }
    }
}
