/*
 * Copyright 2020-2026 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.coyotebitcoin.padawanwallet.domain.utils

sealed class WalletCreateType {
    data object FROMSCRATCH : WalletCreateType()

    class RECOVER(val recoveryPhrase: String) : WalletCreateType()
}
