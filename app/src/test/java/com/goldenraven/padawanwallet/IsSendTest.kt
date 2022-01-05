/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet

import com.goldenraven.padawanwallet.utils.isSend
import org.junit.Test
import org.junit.Assert.*

class IsSendTest {
    @Test
    fun `transaction is a send`() {
        val isSend: Boolean = isSend(sent = 300, received = 20)
        assertEquals(isSend, true)
    }

    @Test
    fun `transaction is a receive`() {
        val isSend: Boolean = isSend(sent = 0, received = 2000)
        assertEquals(isSend, false)
    }
}
