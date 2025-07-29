//
//  ImportWalletScreenNavigation.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 29/07/25.
//

enum ImportWalletNavigation: Hashable, Identifiable {
    var id: Self { self }
    
    case home
    case alertError(data: BottomSheetView.Data)
}
