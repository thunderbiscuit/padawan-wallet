/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.utils

public fun netSendWithoutFees(txSatsOut: Int, txSatsIn: Int, fees: Int): Int {
    return txSatsOut - (txSatsIn + fees)
}
