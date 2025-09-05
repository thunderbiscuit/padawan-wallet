//
//  SendTransactionScreenNavigation.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 05/09/25.
//

enum SendTransactionScreenNavigation: Hashable, Identifiable {
    var id: Self { self }
    
    case openCamera
    case verifyTransaction(amount: String, address: String, tax: String)
    case alert(data: BottomSheetView.Data)
    case pop
}
