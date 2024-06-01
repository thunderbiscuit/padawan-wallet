package com.goldenraven.padawanwallet.data

import android.content.SharedPreferences
import com.goldenraven.padawanwallet.domain.bitcoin.WalletRepository
import com.goldenraven.padawanwallet.utils.RequiredInitialWalletData
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*


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
        verify(editorMock, times(1)).putBoolean("initialized", true)
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

    // @Test
    // fun `Check if load first time tutorials is correct`() {
    //     `when`(sharedPrefsMock.getBoolean("firstTimeTutorialsDone", true)).thenReturn(true)
    //     val loadTutorialDoneTest = repo.loadTutorialsDone()
    //     verify(editorMock, times(9)).putBoolean(anyString(), anyBoolean())
    //     assertEquals(mutableMapOf(
    //         "e1" to false,
    //         "e2" to false,
    //         "e3" to false,
    //         "e4" to false,
    //         "e5" to false,
    //         "e6" to false,
    //         "e7" to false,
    //         "e8" to false,
    //     ), loadTutorialDoneTest)
    // }

    // @Test
    // fun `Check if load future tutorials is correct`() {
    //     `when`(sharedPrefsMock.getBoolean("firstTimeTutorialsDone", true)).thenReturn(false)
    //     `when`(sharedPrefsMock.getBoolean("e1", false)).thenReturn(true)
    //     `when`(sharedPrefsMock.getBoolean("e2", false)).thenReturn(true)
    //     `when`(sharedPrefsMock.getBoolean("e3", false)).thenReturn(true)
    //     `when`(sharedPrefsMock.getBoolean("e4", false)).thenReturn(true)
    //     `when`(sharedPrefsMock.getBoolean("e5", false)).thenReturn(true)
    //     `when`(sharedPrefsMock.getBoolean("e6", false)).thenReturn(true)
    //     `when`(sharedPrefsMock.getBoolean("e7", false)).thenReturn(true)
    //     `when`(sharedPrefsMock.getBoolean("e8", false)).thenReturn(true)
    //     val loadTutorialDoneTest = repo.loadTutorialsDone()
    //     assertEquals(mutableMapOf(
    //         "e1" to true,
    //         "e2" to true,
    //         "e3" to true,
    //         "e4" to true,
    //         "e5" to true,
    //         "e6" to true,
    //         "e7" to true,
    //         "e8" to true,
    //     ), loadTutorialDoneTest)
    // }

    // @Test
    // fun `Check if updating tutorials is correct`() {
    //     repo.updateTutorialsDone(tutorialNumber = 1)
    //     verify(editorMock, times(1)).putBoolean("e1", true)
    // }

    // @Test
    // fun `Check if resetting tutorials is correct`() {
    //     repo.resetTutorials()
    //     verify(editorMock, times(8)).putBoolean(anyString(), anyBoolean())
    // }
}
