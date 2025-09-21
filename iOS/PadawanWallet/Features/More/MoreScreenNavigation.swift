//
//  MoreScreenNavigation.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 12/09/25.
//

enum MoreScreenNavigation: Hashable, Identifiable {
    var id: Self { self }
    
    case recoveryPhase(words: String)
    case sendCoinsBack
    case language
    case about
    case alert(data: BottomSheetView.Data)
}
