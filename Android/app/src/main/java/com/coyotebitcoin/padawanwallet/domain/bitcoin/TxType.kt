/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.coyotebitcoin.padawanwallet.domain.bitcoin

import org.bitcoindevkit.Amount

/**
 * We define an outbound transaction as one where the amount sent is greater than the amount
 * received, and vice versa for inbound transactions.
 */
enum class TxType {
    OUTBOUND,
    INBOUND, ;

    companion object {
        /**
         * Determines whether a transaction is a payment (outbound) or a receive (inbound).
         */
        fun fromAmounts(sent: Amount, received: Amount): TxType {
            return if (sent.toSat() > received.toSat()) OUTBOUND else INBOUND
        }
    }
}
