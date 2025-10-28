//
//  SendTransactionViewModel.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 05/09/25.
//

import SwiftUI
import Foundation
import BitcoinDevKit

@MainActor
final class SendTransactionViewModel: ObservableObject {
    
    @Binding var path: NavigationPath
    @Published var sheetScreen: SendTransactionScreenNavigation?
    @Published var fullScreenCover: SendTransactionScreenNavigation?
    
    @Published var amountValue: String = ""
    @Published var address: String = ""
    @Published var feeRate: Double = 1.0
    let feeRateRange: ClosedRange<Double> = 1...12
    let feeRateStep: Double = 1.0
    
    private var transaction: Psbt?
    
    private let bdkClient: BDKClient
    
    init(
        path: Binding<NavigationPath>,
        bdkClient: BDKClient = .live
    ) {
        _path = path
        self.bdkClient = bdkClient
    }
    
    func openCamera() {
        fullScreenCover = .openCamera
    }
    
    func getBalance() -> String {
        let balance = try? bdkClient.getBalance()
        return balance?.confirmed.toSat().formattedSats() ?? "0"
    }
    
    func verifyTransaction() {
        if isValidTransaction() {
            do {
                guard let amount = UInt64(amountValue) else {
                    return
                }
                let fee = UInt64(feeRate)
                
                let transaction = try bdkClient.createTransaction(address, amount, fee)
                let tax = try transaction.fee()
                sheetScreen = .verifyTransaction(
                    amount: "\(amountValue) sats",
                    address: address,
                    tax: "\(tax) sats"
                )
                self.transaction = transaction
            } catch {
                fullScreenCover = .alert(
                    data: .init(titleKey: "generic_title_error", subtitle: error.localizedDescription)
                )
            }
        }
    }
    
    func sendTransaction() {
        Task {
            do {
                guard let amount = UInt64(amountValue) else { return }
                let fee = UInt64(feeRate)
                
                try await bdkClient.send(address, amount, fee)
                fullScreenCover = .alert(
                    data: .init(

                        titleKey: "transaction_broadcast",
                        onPrimaryButtonTap: { [weak self] in
                            self?.path.removeLast()
                        }
                    )
                )
            } catch {
                self.fullScreenCover = .alert(
                    data: .init(titleKey: "generic_title_error", subtitle: error.localizedDescription)
                )
            }
        }
    }
    
    // MARK: - Private
    
    private func isValidTransaction() -> Bool {
        if amountValue.isEmpty {
            fullScreenCover = .alert(
                data: .init(titleKey: "generic_title_error", subtitleKey: "amount_error_message")
            )
            return false
        }
        
        if address.isEmpty {
            fullScreenCover = .alert(
                data: .init(titleKey: "generic_title_error", subtitleKey: "address_error_message")
            )
            return false
        }
        
        return true
    }
}
