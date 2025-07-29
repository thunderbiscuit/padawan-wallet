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
    @Published var fullScreenCover: ImportWalletNavigation?
    
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
            if let error = validate() {
                throw BDKServiceError.errorWith(message: error)
            }
            
            let seed = words.joined(separator: " ")
            try bdkClient.importWallet(seed)
            
            showHome()
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
    
    // MARK: - Navigation
    
    func showHome() {
        path.removeLast(path.count)
        Session.shared.setNeedOnboarding(false)
    }
    
    // MARK: - Private
    
    private func validate() -> String? {
        for i in 0..<12 {
            if words[i].isEmpty {
                return "Word \(i+1) is empty"
            }
        }
        
        return nil
    }
}
