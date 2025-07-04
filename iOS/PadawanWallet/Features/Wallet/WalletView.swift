/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import SwiftUI

struct WalletRootView: View {
    var body: some View {
        NavigationStack {
            ScrollView {
                VStack(spacing: 24) {
                    BalanceCard()
                    ActionButtons()
                    TransactionsCard()
                }
                .padding()
                .background(Color("Background"))
            }
        }
    }
}
