/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.utils

import java.text.DecimalFormat

fun ULong?.formatInBtc(): String {
    val balanceInSats = if (this == 0UL || this == null) {
        0F
    } else {
        this.toFloat().div(100_000_000)
    }
    return DecimalFormat("0.00000000").format(balanceInSats)
}

fun formatCurrency(amount: String): String {
    val regex = "(\\d)(?=(\\d{3})+\$)".toRegex()
    val dotIdx = amount.indexOf('.')
    return if (dotIdx == -1)  {
        amount.replace(regex, "\$1,")
    } else {
        val num = amount.substring(0, dotIdx).replace(regex, "\$1,")
        val dec = amount.substring(dotIdx+1).replace(regex, "\$1 ")
        "$num.$dec"
    }
}
