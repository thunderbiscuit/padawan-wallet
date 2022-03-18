/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet

import com.goldenraven.padawanwallet.utils.SatoshisIn
import com.goldenraven.padawanwallet.utils.SatoshisOut
import com.goldenraven.padawanwallet.utils.isPayment
import org.junit.Test
import org.junit.Assert.*

class IsSendTest {

    private val satoshisOut300 = SatoshisOut(300)
    private val satoshisIn20 = SatoshisIn(20)
    private val satoshisOut0 = SatoshisOut(0)

    @Test
    fun `Transaction is a payment`() {
        val isPayment: Boolean = isPayment(sent = satoshisOut300, received = satoshisIn20)
        assertEquals(isPayment, true)
    }

    @Test
    fun `Transaction is a receive`() {
        val isPayment: Boolean = isPayment(sent = satoshisOut0, received = satoshisIn20)
        assertEquals(isPayment, false)
    }
}
