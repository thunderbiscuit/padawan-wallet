//
//  WelcomeViewModel.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 28/07/25.
//

import SwiftUI

final class WelcomeViewModel: ObservableObject {
    
    @Binding var path: NavigationPath
    
    private let bdkClient: BDKClient
    
    init(
        path: Binding<NavigationPath>,
        bdkClient: BDKClient
    ) {
        _path = path
        self.bdkClient = bdkClient
    }
    
    // MARK: - Navigation
    
    func createWallet() {
        do {
            try bdkClient.createNewWallet()
            Session.shared.setNeedOnboarding(false)
        } catch {
            print(error)
        }
    }
    
    func importWallet() {
        path.append(WelcomeScreenNavigation.importWallet)
    }
}
