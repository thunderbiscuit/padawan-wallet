/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import Foundation
import SwiftUI

struct PadawanTestsConfiguration {
    
    static var shouldUseMock: Bool {
        ProcessInfo.processInfo.arguments.contains("-useMockService")
    }
    
    static var bdkClient: BDKClient {
        shouldUseMock ? .mock : .live
    }
    
    static func setup() {
        #if DEBUG
        let args = ProcessInfo.processInfo.arguments
        
        if args.contains("-resetOnboarding") {
            deepClean()
        }
        
        if args.contains("-skipOnboarding") {
            configureSkipOnboarding()
        }
        #endif
    }
    
    private static func deepClean() {
        Session.shared.resetWallet()
        
// MARK: - wipe files
        
        let fileManager = FileManager.default
        if let documentsURL = fileManager.urls(for: .documentDirectory, in: .userDomainMask).first {
            try? fileManager.contentsOfDirectory(at: documentsURL, includingPropertiesForKeys: nil).forEach { url in
                try? fileManager.removeItem(at: url)
            }
        }
        
// MARK: - wipe keychain
        
        let secItemClasses = [kSecClassGenericPassword, kSecClassInternetPassword, kSecClassCertificate, kSecClassKey, kSecClassIdentity]
        secItemClasses.forEach { itemClass in
            let spec: NSDictionary = [kSecClass as String: itemClass]
            SecItemDelete(spec)
        }
    }
    
// MARK: - skip onboarding logic
    
    private static func configureSkipOnboarding() {
        Session.shared.setNeedOnboarding(false)
        
        if !shouldUseMock {
            do {
                try BDKService.shared.createWallet(nil)
                Session.shared.objectWillChange.send()
            } catch {
                fatalError("error to create wallet: \(error)")
            }
        }
    }
}
