/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.coyotebitcoin.padawanwallet.domain.utils

data class RequiredInitialWalletData(
    val descriptor: String,
    val changeDescriptor: String
)
