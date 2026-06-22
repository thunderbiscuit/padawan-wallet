/*
 * Copyright 2020-2026 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.coyotebitcoin.padawanwallet.data

import android.content.SharedPreferences
import com.coyotebitcoin.padawanwallet.domain.bitcoin.WalletRepository
import com.coyotebitcoin.padawanwallet.domain.utils.RequiredInitialWalletData
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import org.mockito.Mockito.anyBoolean
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class WalletRepositoryTest {

    private val repo: WalletRepository = WalletRepository
    private lateinit var sharedPrefsMock: SharedPreferences
    private lateinit var editorMock: SharedPreferences.Editor

    @BeforeTest
    fun setUp() {
        sharedPrefsMock = mock(SharedPreferences::class.java)
        editorMock = mock(SharedPreferences.Editor::class.java)
        repo.setSharedPreferences(sharedPrefsMock)
        `when`(sharedPrefsMock.edit()).thenReturn(editorMock)
    }

    @Test
    fun `Check if wallet exists`() {
        `when`(sharedPrefsMock.getBoolean(anyString(), anyBoolean())).thenReturn(true)
        assertEquals(true, repo.doesWalletExist(), "Wallet does not exist")
    }

    @Test
    fun `Check if wallet data is correct`() {
        val requiredInitialWalletDataTest =
            RequiredInitialWalletData(descriptor = "descriptorTest", changeDescriptor = "changeTest")
        `when`(sharedPrefsMock.getString("descriptor", null)).thenReturn("descriptorTest")
        `when`(sharedPrefsMock.getString("changeDescriptor", null)).thenReturn("changeTest")
        assertEquals(requiredInitialWalletDataTest, repo.getInitialWalletData(), "Wallet data is incorrect")
    }

    @Test
    fun `Check if saving Mnemonic is correct`() {
        repo.saveMnemonic(mnemonic = "mnemonicTest")
        verify(editorMock, times(1)).putString("mnemonic", "mnemonicTest")
    }

    @Test
    fun `Check if getting Mnemonic is correct when it exists`() {
        `when`(sharedPrefsMock.getString("mnemonic", "No seed phrase saved")).thenReturn("seedPhraseTest")
        assertEquals("seedPhraseTest", repo.getMnemonic(), "Mnemonic get is incorrect")
    }

    @Test
    fun `Check if getting Mnemonic is correct when it does not exist`() {
        `when`(sharedPrefsMock.getString("mnemonic", "No seed phrase saved")).thenReturn(null)
        assertEquals("Seed phrase not there", repo.getMnemonic(), "Mnemonic get is incorrect")
    }
}
