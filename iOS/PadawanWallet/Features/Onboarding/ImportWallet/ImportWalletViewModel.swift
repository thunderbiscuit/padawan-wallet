//
//  WelcomeViewModel.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 28/07/25.
//

import SwiftUI

enum ImportNavigation: Hashable {
    case home
}

final class ImporViewModel: ObservableObject {
    
    @Binding var path: NavigationPath
    
    init(path: Binding<NavigationPath>) {
        _path = path
    }
    
    // MARK: - Navigation
    
    func showHome() {
        path.append(ImportNavigation.home)
    }
}
