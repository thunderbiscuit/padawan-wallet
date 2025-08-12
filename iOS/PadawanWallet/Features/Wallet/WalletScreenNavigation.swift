//
//  WalletScreenNavigation.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 13/08/25.
//

enum WalletScreenNavigation: Hashable, Identifiable {
    var id: Self { self }
    
    case receive
    case send
    case alertError(data: BottomSheetView.Data)
}
