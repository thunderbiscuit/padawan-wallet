/*
 * Copyright 2020-2023 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.utils

enum class ScreenSize {
    Small,
    Phone,
    // Tablet,
}

fun getScreenSize(size: Int): ScreenSize {
    return when {
        size < 340 -> ScreenSize.Small
        else -> ScreenSize.Phone
        // else -> ScreenSize.Tablet
    }
}
