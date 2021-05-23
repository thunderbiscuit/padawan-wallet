package com.goldenraven.padawanwallet.utils

public fun isSend(sent: Int, received: Int): Boolean {
    return received - sent < 0
}