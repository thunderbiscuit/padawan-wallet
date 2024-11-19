package com.coyotebitcoin.padawanwallet

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
