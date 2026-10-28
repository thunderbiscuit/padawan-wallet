/*
 * Copyright 2020-2026 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.coyotebitcoin.padawanwallet.data

import android.content.Context
import java.io.FileNotFoundException
import java.util.Properties

private const val LOCAL_FILE = "faucet.properties"
private const val DEFAULTS_FILE = "faucet.defaults.properties"

data class FaucetConfig(val url: String, val token: String) {
    companion object {
        // Reads the gitignored faucet.properties asset if present, and falls back to the committed defaults otherwise
        fun load(context: Context): FaucetConfig {
            val properties = Properties()
            val stream = try {
                context.assets.open(LOCAL_FILE)
            } catch (e: FileNotFoundException) {
                context.assets.open(DEFAULTS_FILE)
            }
            stream.use { properties.load(it) }

            return FaucetConfig(
                url = properties.getProperty("url") ?: error("Missing 'url' in faucet configuration"),
                token = properties.getProperty("token") ?: error("Missing 'token' in faucet configuration"),
            )
        }
    }
}
