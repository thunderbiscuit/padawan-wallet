//
//  WalletFullScanScriptInspector.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 09/08/25.
//

import BitcoinDevKit

actor WalletFullScanScriptInspector: @preconcurrency FullScanScriptInspector {
    private let updateProgress: @Sendable (UInt64) -> Void
    private var inspectedCount: UInt64 = 0

    init(updateProgress: @escaping @Sendable (UInt64) -> Void) {
        self.updateProgress = updateProgress
    }

    func inspect(keychain: KeychainKind, index: UInt32, script: Script) {
        inspectedCount += 1
        updateProgress(inspectedCount)
    }
}
