//
//  WelcomeViewModel.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 28/07/25.
//

import SwiftUI

final class WelcomeViewModel: ObservableObject {
    
    @Binding var path: NavigationPath
    @Published var sheetScreen: WelcomeScreenNavigation?
    @Published var fullScreenCover: ImportWalletNavigation?
    
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
        } catch let error as BDKServiceError {
            fullScreenCover = .alertError(
                data: .init(error: error)
            )
        } catch {
            fullScreenCover = .alertError(
                data: .init(error: .generic)
            )
        }
    }
    
    func importWallet() {
        if isIpad {
            sheetScreen = .importWallet
        } else {
            path.append(WelcomeScreenNavigation.importWallet)
        }
    }
}
