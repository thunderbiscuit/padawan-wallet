/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.utils

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
        val words: WordCheckResult = checkWords(wrongRecoveryPhraseMap)
        assertEquals(words is WordCheckResult.FAILURE, true)
    }

    @Test
    fun `Function wordCheck() correctly identifies which word is missing`() {
        val wrongRecoveryPhraseMap = mapOf(
            1 to "aim", 2 to "bunker", 3 to "wash", 4 to "balance", 5 to "finish", 6 to "force",
            7 to "paper", 8 to "  ", 9 to "cabin", 10 to "spoon", 11 to "stable", 12 to "organ"
        )
        val words: WordCheckResult = checkWords(wrongRecoveryPhraseMap)
        words as WordCheckResult.FAILURE
        assertEquals("Word 8 is empty", words.errorMessage)
    }

    @Test
    fun `Function wordCheck() correctly identifies an invalid word`() {
        val wrongRecoveryPhraseMap = mapOf(
            1 to "aim", 2 to "bunkert", 3 to "wash", 4 to "balance", 5 to "finish", 6 to "force",
            7 to "paper", 8 to "  ", 9 to "cabin", 10 to "spoon", 11 to "stable", 12 to "organ"
        )
        val words: WordCheckResult = checkWords(wrongRecoveryPhraseMap)
        words as WordCheckResult.FAILURE
        assertEquals("Word 2 is not valid", words.errorMessage)
    }

    @Test
    fun `Correct recovery phrase passes the checkWord() function`() {
        val words = checkWords(correctRecoveryPhraseMap)
        assertEquals(true, words is WordCheckResult.SUCCESS)
    }

    @Test
    fun `Recovery phrase gets lowercased`() {
        val wrongRecoveryPhrase = mapOf(
            1 to "Aim", 2 to "BUNKER", 3 to "wash", 4 to "balance", 5 to "finish", 6 to "force",
            7 to "paper", 8 to "Analyst", 9 to "cabin", 10 to "spooN", 11 to "STABLE", 12 to "organ"
        )
        val wordCheckResult: WordCheckResult = checkWords(wrongRecoveryPhrase)
        wordCheckResult as WordCheckResult.SUCCESS
        assertEquals(correctRecoveryPhraseString, wordCheckResult.recoveryPhrase)
    }
}
