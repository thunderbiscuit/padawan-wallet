//
//  MoreViewModel.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 12/09/25.
//

import SwiftUI
import Foundation

@MainActor
final class MoreViewModel: ObservableObject {
    
    @Binding var path: NavigationPath

    @Published var fullScreenCover: MoreScreenNavigation?
    @Published var version: String = ""
    
    let bdkClient: BDKClient
    
    init(
        path: Binding<NavigationPath>,
        bdkClient: BDKClient
    ) {
        _path = path
        self.bdkClient = bdkClient
        buildVersion()
    }
    
    func showRecoveryPhrase() {
        guard let phrase = try? getRecoveryPhase() else { return }
        path.append(MoreScreenNavigation.recoveryPhase(words: phrase))
    }
    
    func showSendCoinsBack() {
        path.append(MoreScreenNavigation.sendCoinsBack)
    }
    
    func showLanguage() {
        path.append(MoreScreenNavigation.language)
    }
    
    func showAbout() {
        path.append(MoreScreenNavigation.about)
    }
    
    func resetWallet() {
        Session.shared.resetWallet()
    }
    
    // MARK: - Private
    
    private func getRecoveryPhase() throws -> String {
        try bdkClient.getRecoveryPhase()
    }
    
    private func buildVersion() {
        let shortVersion = Bundle.main.infoDictionary?["CFBundleShortVersionString"] as? String ?? ""
        let buildNumber = Bundle.main.infoDictionary?["CFBundleVersion"] as? String ?? ""
        
        version = "\(shortVersion)(\(buildNumber))"
    }
}
