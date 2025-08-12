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
    
    private let bdkClient: BDKClient
    
    private var updateProgress: @Sendable (UInt64, UInt64) -> Void {
        { [weak self] inspected, total in
            DispatchQueue.main.async {
                print("\(total > 0 ? Float(inspected) / Float(total) : 0)")
//                self?.totalScripts = total
//                self?.inspectedScripts = inspected
//                self?.progress = total > 0 ? Float(inspected) / Float(total) : 0
            }
        }
    }

    private var updateProgressFullScan: @Sendable (UInt64) -> Void {
        { [weak self] inspected in
            DispatchQueue.main.async {
                print("\(inspected)")
//                self?.inspectedScripts = inspected
            }
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
        } catch {
            print(error)
        }
    }
    
    @MainActor
    func syncWallet() async {
        guard Session.shared.walletExists() else {
            return
        }
        
        if bdkClient.needsFullScan() {
            await fullSync()
        } else {
            await incrementalSync()
        }
        
        do {
            balance = try self.bdkClient.getBalance().total.toSat()
        } catch {
            print(error)
        }
    }
    
    // MARK: - Private
    
    private func fullSync() async {
        do {
            let inspector = WalletFullScanScriptInspector(updateProgress: updateProgressFullScan)
            try await bdkClient.fullScanWithInspector(inspector)
            Session.shared.isFullScanRequired = false
            print("Synced")
        } catch {
            print(error)
        }
    }
    
    private func incrementalSync() async {
        do {
            let inspector = WalletSyncScriptInspector(updateProgress: updateProgress)
            try await bdkClient.syncWithInspector(inspector)
            print("Synced")
        } catch BDKServiceError.needResync {
            print(BDKServiceError.needResync)
//            Session.shared.isFullScanRequired = true
            await syncWallet()
        } catch {
            
            print(error)
        }
    }
}
