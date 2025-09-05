//
//  SendTransactionViewModel.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 05/09/25.
//

import SwiftUI
import Foundation
import BitcoinDevKit

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
                    data: .init(title: Strings.genericTitleError, subtitle: error.localizedDescription)
                )
            }
        }
    }
    
    func sendTransaction() {
        do {
            guard let amount = UInt64(amountValue) else {
                return
            }
            let fee = UInt64(feeRate)
            
            try bdkClient.send(address, amount, fee)
            fullScreenCover = .alert(
                data: .init(
                    title: Strings.transactionBroadcast,
                    onPrimaryButtonTap: { [weak self] in
                        self?.path.removeLast()
                    }
                )
            )
        } catch {
            fullScreenCover = .alert(
                data: .init(title: Strings.genericTitleError, subtitle: error.localizedDescription)
            )
        }
    }
    
    // MARK: - Private
    
    private func isValidTransaction() -> Bool {
        if amountValue.isEmpty {
            fullScreenCover = .alert(
                data: .init(title: Strings.genericTitleError, subtitle: Strings.amountErrorMessage)
            )
            return false
        }
        
        if address.isEmpty {
            fullScreenCover = .alert(
                data: .init(title: Strings.genericTitleError, subtitle: Strings.addressErrorMessage)
            )
            return false
        }
        
        return true
    }
}
