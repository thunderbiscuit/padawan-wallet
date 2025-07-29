//
//  WelcomeScreenNavigation.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 29/07/25.
//

enum WelcomeScreenNavigation: Hashable, Identifiable {
    var id: Self { self }
    
    case importWallet
    case alertError(data: BottomSheetView.Data)
}
