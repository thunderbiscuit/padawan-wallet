//
//  WelcomeViewModel.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 28/07/25.
//

import SwiftUI

final class WelcomeViewModel: ObservableObject {
    
    @Binding var path: NavigationPath
    
    init(path: Binding<NavigationPath>) {
        _path = path
    }
    
    // MARK: - Navigation
    
    func createWallet() {
        // TODO: - Call BDKService
        Session.shared.setNeedOnboarding(false)
    }
    
    func importWallet() {
        path.append(WelcomeScreenNavigation.importWallet)
    }
}
