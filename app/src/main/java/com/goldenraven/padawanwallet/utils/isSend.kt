/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.utils

import timber.log.Timber

public fun isSend(sent: Int, received: Int): Boolean {
    Timber.i("[PADAWANLOGS] isSend value is ${received - sent}")
    return received - sent < 0
}