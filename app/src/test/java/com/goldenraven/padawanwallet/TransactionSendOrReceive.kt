package com.goldenraven.padawanwallet

import com.goldenraven.padawanwallet.utils.isSend
import org.junit.Test
import org.junit.Assert.*

class TransactionSendOrReceive {
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
