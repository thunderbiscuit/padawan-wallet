//
//  WalletSyncScriptInspector.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 09/08/25.
//

import BitcoinDevKit
import Foundation

actor WalletSyncScriptInspector: @preconcurrency SyncScriptInspector {
    private let updateProgress: @Sendable (UInt64, UInt64) -> Void
    private var inspectedCount: UInt64 = 0
    private var totalCount: UInt64 = 0

    init(updateProgress: @escaping @Sendable (UInt64, UInt64) -> Void) {
        self.updateProgress = updateProgress
    }

    func inspect(script: Script, total: UInt64) {
        totalCount = total
        inspectedCount += 1

        let delay: TimeInterval =
            if total <= 5 {
                0.2
            } else if total < 10 {
                0.15
            } else if total < 20 {
                0.1
            } else {
                0
            }
        if delay > 0 {
            Task {
                try? await Task.sleep(nanoseconds: UInt64(delay * 1_000_000_000))
                updateProgress(inspectedCount, totalCount)
            }
        } else {
            updateProgress(inspectedCount, totalCount)
        }
    }
}
