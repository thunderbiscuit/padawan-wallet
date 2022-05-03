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
