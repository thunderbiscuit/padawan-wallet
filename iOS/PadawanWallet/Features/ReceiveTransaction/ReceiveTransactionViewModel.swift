//
//  ReceiveTransactionViewModel.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 05/09/25.
//

import SwiftUI
import Foundation
import BitcoinDevKit

final class ReceiveTransactionViewModel: ObservableObject {
    
    @Binding var path: NavigationPath
    
    private let bdkClient: BDKClient
    @Published var address: String?
    
    init(
        path: Binding<NavigationPath>,
        bdkClient: BDKClient = .live
    ) {
        _path = path
        self.bdkClient = bdkClient
    }
    
    func generateAddress() {
        if let address = try? bdkClient.getAddress() {
            self.address = address
        } else {
            // error
        }
    }
    
    func copyAddress() {
        UIPasteboard.general.string = address
    }
}
