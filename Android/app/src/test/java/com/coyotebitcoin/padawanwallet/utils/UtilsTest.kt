/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.coyotebitcoin.padawanwallet.utils

import com.coyotebitcoin.padawanwallet.domain.utils.netSendWithoutFees
import org.junit.Assert.assertEquals
import org.junit.Test

class UtilsTest {

    // You cannot use the bdk-android types in the unit tests yet. Would need to build for the local platform.

    // val sat300: Amount = Amount.fromSat(300uL)
    // val sat20: Amount = Amount.fromSat(20uL)
    // val sat0: Amount = Amount.fromSat(0uL)
    //
    // @Test
    // fun `Transaction is a payment (outbound)`() {
    //     val txType: TxType = TxType.fromAmounts(sat300, received = sat20)
    //     assertEquals("Payment transaction incorrectly identified as receive", TxType.OUTBOUND, txType)
    // }
    //
    // @Test
    // fun `Transaction is a receive (inbound)`() {
    //     val txType: TxType = TxType.fromAmounts(sent = sat0, received = sat20)
    //     assertEquals("Receive transaction incorrectly identified as payment", TxType.INBOUND, txType)
    // }

    @Test
    fun `Function netSendWithoutFees() calculates correctly`() {
        val testFees = 15uL
        val netSendWithoutFeesTest: ULong = netSendWithoutFees(txSatsOut = 300uL, txSatsIn = 20uL, fee = testFees)
        // expected = 300 - (20 + 15) = 265
        assertEquals("netSendWithoutFees() calculation failed", 265uL, netSendWithoutFeesTest)
    }
}
