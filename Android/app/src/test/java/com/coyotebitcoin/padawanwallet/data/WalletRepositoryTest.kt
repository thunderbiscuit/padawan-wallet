/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.coyotebitcoin.padawanwallet.data

import android.content.SharedPreferences
import com.coyotebitcoin.padawanwallet.domain.bitcoin.WalletRepository
import com.coyotebitcoin.padawanwallet.domain.utils.RequiredInitialWalletData
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
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

    @Before
    fun setUp() {
        sharedPrefsMock = mock(SharedPreferences::class.java)
        editorMock = mock(SharedPreferences.Editor::class.java)
        repo.setSharedPreferences(sharedPrefsMock)
        `when`(sharedPrefsMock.edit()).thenReturn(editorMock)
    }

    @Test
    fun `Check if wallet exists`() {
        `when`(sharedPrefsMock.getBoolean(anyString(), anyBoolean())).thenReturn(true)
        assertEquals("Wallet does not exist",true, repo.doesWalletExist())
    }

    @Test
    fun `Check if wallet data is correct`() {
        val requiredInitialWalletDataTest = RequiredInitialWalletData(descriptor = "descriptorTest", changeDescriptor = "changeTest")
        `when`(sharedPrefsMock.getString("descriptor", null)).thenReturn("descriptorTest")
        `when`(sharedPrefsMock.getString("changeDescriptor", null)).thenReturn("changeTest")
        assertEquals("Wallet data is incorrect", requiredInitialWalletDataTest, repo.getInitialWalletData())
    }

    @Test
    fun `Check if saving wallet is correct`() {
        repo.saveWallet(path = "pathTest", descriptor = "descriptorTest", changeDescriptor = "changeDescriptorTest")
        verify(editorMock, times(1)).putBoolean("initializedV1", true)
        verify(editorMock, times(1)).putString("path", "pathTest")
        verify(editorMock, times(1)).putString("descriptor", "descriptorTest")
        verify(editorMock, times(1)).putString("changeDescriptor", "changeDescriptorTest")
    }

    @Test
    fun `Check if saving Mnemonic is correct`() {
        repo.saveMnemonic(mnemonic = "mnemonicTest")
        verify(editorMock, times(1)).putString("mnemonic", "mnemonicTest")
    }

    @Test
    fun `Check if getting Mnemonic is correct when it exists`() {
        `when`(sharedPrefsMock.getString("mnemonic", "No seed phrase saved")).thenReturn("seedPhraseTest")
        assertEquals("Mnemonic get is incorrect", "seedPhraseTest", repo.getMnemonic())
    }

    @Test
    fun `Check if getting Mnemonic is correct when it does not exist`() {
        `when`(sharedPrefsMock.getString("mnemonic", "No seed phrase saved")).thenReturn(null)
        assertEquals("Mnemonic get is incorrect", "Seed phrase not there", repo.getMnemonic())
    }

    @Test
    fun `Check if faucet was called before`() {
        `when`(sharedPrefsMock.getBoolean("faucetCallDone", false)).thenReturn(true)
        assertEquals("Faucet call is wrong", true, repo.wasFaucetCallDone())
    }

    @Test
    fun `Check if faucet done call is correct`() {
        repo.offerFaucetDone()
        verify(editorMock, times(1)).putBoolean("offerFaucetDone", true)
    }
}
