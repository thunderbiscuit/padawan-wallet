//
//  WelcomeViewModel.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 28/07/25.
//

import SwiftUI

final class ImporViewModel: ObservableObject {
    
    @Binding var path: NavigationPath
    private let bdkClient: BDKClient
    @Published var showAlertError: Bool = false
    
    var words: [String] = .init(repeating: "", count: 12)
    
    init(
        path: Binding<NavigationPath>,
        bdkClient: BDKClient
    ) {
        _path = path
        self.bdkClient = bdkClient
    }
    
    func importWallet() {
        do {
            print(words)
            
//            let seed = "abstract keep priority feed interest pencil squeeze index choice wrestle palace hotel"
//            try bdkClient.importWallet(seed)
            
//            showHome()
        } catch {
            print(error)
        }
    }
    
    // MARK: - Navigation
    
    func showHome() {
        path.removeLast(path.count)
        Session.shared.setNeedOnboarding(false)
    }
}
