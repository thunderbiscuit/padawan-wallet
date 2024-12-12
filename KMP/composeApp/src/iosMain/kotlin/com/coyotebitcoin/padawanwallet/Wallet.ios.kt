package com.coyotebitcoin.padawanwallet

import PadawanBdkWrapper.PadawanBdkWrapper

@OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)
actual class Wallet actual constructor() {
    private val wrapper = PadawanBdkWrapper()

    actual fun newAddress(): String {
        return wrapper.newAddress()
    }
}
