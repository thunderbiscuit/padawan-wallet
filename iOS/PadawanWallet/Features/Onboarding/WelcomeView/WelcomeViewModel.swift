//
//  WelcomeViewModel.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 28/07/25.
//

import SwiftUI

enum WelcomeNavigation: Hashable {
    case createWallet
    case importWallet
}

final class WelcomeViewModel: ObservableObject {
    
    @Binding var path: NavigationPath
    
    init(path: Binding<NavigationPath>) {
        _path = path
    }
    
    // MARK: - Navigation
    
    func createWallet() {
        path.append(WelcomeNavigation.createWallet)
    }
    
    func importWallet() {
        path.append(WelcomeNavigation.importWallet)
    }
}
