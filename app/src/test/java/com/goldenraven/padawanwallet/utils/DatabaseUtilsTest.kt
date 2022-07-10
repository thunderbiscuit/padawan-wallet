/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class DatabaseUtilsTest {

    private val satoshisOut300 = SatoshisOut(300)
    private val satoshisIn20 = SatoshisIn(20)
    private val satoshisOut0 = SatoshisOut(0)

    @Test
    fun `Transaction is a payment`() {
        val isPayment: Boolean = isPayment(sent = satoshisOut300, received = satoshisIn20)
        assertEquals("Payment transaction incorrectly identified as receive", true, isPayment)
    }

    @Test
    fun `Transaction is a receive`() {
        val isPayment: Boolean = isPayment(sent = satoshisOut0, received = satoshisIn20)
        assertEquals("Receive transaction incorrectly identified as payment", false, isPayment)
    }

    @Test
    fun `Function netSendWithoutFees() calculates correctly`() {
        val testFees = 15
        val netSendWithoutFeesTest: Int = netSendWithoutFees(txSatsOut = satoshisOut300, txSatsIn = satoshisIn20, fees = testFees)
        // expected = 300 - (20 + 15) = 265
        assertEquals("netSendWithoutFees() calculation failed", 265, netSendWithoutFeesTest)
    }
}
