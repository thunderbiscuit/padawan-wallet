/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet

import com.goldenraven.padawanwallet.utils.buildRecoveryPhrase
import com.goldenraven.padawanwallet.utils.checkWords
import org.junit.Test
import org.junit.Assert.*

class CheckWords {
    private val correctRecoveryPhraseMap = mapOf(
        1 to "aim", 2 to "bunker", 3 to "wash", 4 to "balance", 5 to "finish", 6 to "force",
        7 to "paper", 8 to "analyst", 9 to "cabin", 10 to "spoon", 11 to "stable", 12 to "organ"
    )
    private val correctRecoveryPhraseString = "aim bunker wash balance finish force paper analyst cabin spoon stable organ"

    @Test
    fun `Recovery phrase is missing a word`() {
        val wrongRecoveryPhraseMap = mapOf(
            1 to "aim", 2 to "bunker", 3 to "wash", 4 to "balance", 5 to "finish", 6 to "force",
            7 to "paper", 8 to "  ", 9 to "cabin", 10 to "spoon", 11 to "stable", 12 to "organ"
        )
        val words = checkWords(wrongRecoveryPhraseMap)
        assertEquals(words, false)
    }

    @Test
    fun `Correct recovery phrase passes the checkWord() function`() {
        val words = checkWords(correctRecoveryPhraseMap)
        assertEquals(words, true)
    }

    @Test
    fun `Recovery phrase gets lowercased`() {
        val wrongRecoveryPhrase = mapOf(
            1 to "Aim", 2 to "BUNKER", 3 to "wash", 4 to "balance", 5 to "finish", 6 to "force",
            7 to "paper", 8 to "Analyst", 9 to "cabin", 10 to "spooN", 11 to "STABLE", 12 to "organ"
        )
        val words = buildRecoveryPhrase(wrongRecoveryPhrase)
        assertEquals(words, correctRecoveryPhraseString)
    }
}
