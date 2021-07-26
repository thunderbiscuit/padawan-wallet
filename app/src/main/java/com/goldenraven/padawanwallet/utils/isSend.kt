/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.utils

import android.util.Log

public fun isSend(sent: Int, received: Int): Boolean {
    Log.i("Padalogs","isSend value is ${received - sent}")
    return received - sent < 0
}