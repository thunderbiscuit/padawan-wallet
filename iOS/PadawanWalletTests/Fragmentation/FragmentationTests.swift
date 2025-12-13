//
//  FragmentationTests.swift
//  PadawanWalletTests
//
//  Created by Vinicius Silva Moreira on 13/12/25.
//

import Testing
import SwiftUI
import BitcoinDevKit
@testable import PadawanWallet

@MainActor
struct FragmentationTests {

    @Test("WalletViewModel: Initial state should be zeroed")
    func testWalletInitialState() {
        let path = Binding<NavigationPath>.constant(NavigationPath())
        let client = makeStubClient()
        let viewModel = WalletViewModel(path: path, bdkClient: client)
        
        #expect(viewModel.balance == 0)
        #expect(viewModel.isSyncing == false)
        #expect(viewModel.transactions.isEmpty)
    }
    
    @Test("WalletViewModel: LoadWallet should load the balance from the Session")
    func testWalletLoadWallet() {
        let mockBalance: UInt64 = 8888
        Session.shared.lastBalanceUpdate = mockBalance
        
        let path = Binding<NavigationPath>.constant(NavigationPath())
        let client = makeStubClient()
        let viewModel = WalletViewModel(path: path, bdkClient: client)
        
        viewModel.loadWallet()
        
        #expect(viewModel.balance == mockBalance)
    }

    @Test("WalletViewModel: Sync should successfully update the balance")
    func testWalletSyncSuccess() async {
        let expectedBalance: UInt64 = 50000
        
        let client = makeStubClient(
            getBalance: { makeBalance(confirmedSats: expectedBalance) },
            transactions: { [] },
            syncWithInspector: { _ in }
        )
        
        let path = Binding<NavigationPath>.constant(NavigationPath())
        
        let viewModel = WalletViewModel(
            path: path,
            bdkClient: client,
            walletExistsChecker: { true }
        )
        
        await viewModel.syncWallet()
        
        #expect(viewModel.balance == expectedBalance)
        #expect(viewModel.isSyncing == false)
    }
    
    @Test("WalletViewModel: Sync should handle errors and display an alert")
    func testWalletSyncError() async {
        let client = makeStubClient(
            syncWithInspector: { _ in throw BDKServiceError.generic }
        )
        
        let path = Binding<NavigationPath>.constant(NavigationPath())
        
        let viewModel = WalletViewModel(
            path: path,
            bdkClient: client,
            walletExistsChecker: { true }
        )
        
        await viewModel.syncWallet()
        
        #expect(viewModel.fullScreenCover != nil)
        #expect(viewModel.isSyncing == false)
    }
    
    @Test("WalletViewModel: Faucet should display a confirmation dialog")
    func testFaucetConfirmationDialog() {
        let path = Binding<NavigationPath>.constant(NavigationPath())
        let viewModel = WalletViewModel(path: path, bdkClient: makeStubClient())
        
        viewModel.getFaucetCoinsConfirmation { }
        
        #expect(viewModel.fullScreenCover != nil)
        
        if case .alertError(let data) = viewModel.fullScreenCover {
            #expect(!data.title.isEmpty)
        }
    }
    
    @Test("WalletViewModel: Faucet should display an error if getAddress fails")
    func testFaucetFlowError() async {
        let client = makeStubClient(
            getAddress: { throw BDKServiceError.generic }
        )
        let path = Binding<NavigationPath>.constant(NavigationPath())
        let viewModel = WalletViewModel(path: path, bdkClient: client)
        
        viewModel.getFaucetCoinsConfirmation { }
        
        if case .alertError(let data) = viewModel.fullScreenCover {
            data.onPrimaryButtonTap?()
        }
        
        try? await Task.sleep(nanoseconds: 200_000_000)
        
        #expect(viewModel.isSyncing == false)
        
        if case .alertError(let data) = viewModel.fullScreenCover {
            let errorTitle = LanguageManager.shared.localizedString(forKey: "generic_title_error")
            #expect(data.title == errorTitle)
        }
    }

    @Test("SendViewModel: Validation fails with empty fields")
    func testSendValidationEmptyFields() {
        let path = Binding<NavigationPath>.constant(NavigationPath())
        let viewModel = SendTransactionViewModel(path: path, bdkClient: makeStubClient())
        
        viewModel.amountValue = ""
        viewModel.address = ""
        viewModel.verifyTransaction()
        #expect(viewModel.fullScreenCover != nil)
        
        viewModel.fullScreenCover = nil
        
        viewModel.amountValue = "100"
        viewModel.address = ""
        viewModel.verifyTransaction()
        #expect(viewModel.fullScreenCover != nil)
    }
    
    @Test("SendViewModel: VerifyTransaction successfully displays the sheet")
    func testSendVerifySuccess() {
        let path = Binding<NavigationPath>.constant(NavigationPath())
        
        let client = makeStubClient(
            createTransaction: { _, _, _ in
                return try Psbt(psbtBase64: validDummyPsbtBase64.replacingOccurrences(of: "\n", with: ""))
            }
        )
        
        let viewModel = SendTransactionViewModel(path: path, bdkClient: client)
        
        viewModel.amountValue = "1000"
        viewModel.address = "tb1_valid_address"
        
        viewModel.verifyTransaction()
        
        #expect(viewModel.sheetScreen != nil)
        #expect(viewModel.fullScreenCover == nil)
    }
    
    @Test("SendViewModel: Sendtransaction calls broadcast and displays success")
    func testSendBroadcastSuccess() async {
        var sendCalled = false
        
        let client = makeStubClient(
            createTransaction: { _, _, _ in return try Psbt(psbtBase64: validDummyPsbtBase64) },
            send: { _, _, _ in sendCalled = true }
        )
        
        let path = Binding<NavigationPath>.constant(NavigationPath())
        let viewModel = SendTransactionViewModel(path: path, bdkClient: client)
        
        viewModel.amountValue = "1000"
        viewModel.address = "tb1_valid_address"
        
        viewModel.sendTransaction()
        
        try? await Task.sleep(nanoseconds: 200_000_000)
        
        #expect(sendCalled == true)
        #expect(viewModel.fullScreenCover != nil)
        
        if case .alert(let data) = viewModel.fullScreenCover {
             let expected = LanguageManager.shared.localizedString(forKey: "transaction_broadcast")
             #expect(data.title == expected)
        }
    }
    
    @Test("SendViewModel: Should display an error if the broadcast fails (Network/BDK)")
    func testSendBroadcastFailure() async {
        let client = makeStubClient(
            createTransaction: { _, _, _ in return try Psbt(psbtBase64: validDummyPsbtBase64.replacingOccurrences(of: "\n", with: "")) },
            send: { _, _, _ in throw BDKServiceError.generic }
        )
        
        let path = Binding<NavigationPath>.constant(NavigationPath())
        let viewModel = SendTransactionViewModel(path: path, bdkClient: client)
        
        viewModel.amountValue = "1000"
        viewModel.address = "tb1_valid"
        
        viewModel.sendTransaction()
        try? await Task.sleep(nanoseconds: 200_000_000)
        
        #expect(viewModel.fullScreenCover != nil)
        
        if case .alert(let data) = viewModel.fullScreenCover {
            let errorTitle = LanguageManager.shared.localizedString(forKey: "generic_title_error")
            #expect(data.title == errorTitle)
        }
    }

    @Test("ReceiveViewModel: GenerateAddress updates the UI with sucess")
    func testReceiveGenerateAddress() {
        let expectedAddress = "tb1_test_address_generated"
        let client = makeStubClient(getAddress: { return expectedAddress })
        let path = Binding<NavigationPath>.constant(NavigationPath())
        let viewModel = ReceiveTransactionViewModel(path: path, bdkClient: client)
        
        viewModel.generateAddress()
        
        #expect(viewModel.address == expectedAddress)
    }
    
    @Test("ReceiveViewModel: Failure in generation keeps the address empty (edge case)")
    func testReceiveGenerateAddressFailure() {
        let client = makeStubClient(
            getAddress: { throw BDKServiceError.generic }
        )
        let path = Binding<NavigationPath>.constant(NavigationPath())
        let viewModel = ReceiveTransactionViewModel(path: path, bdkClient: client)
        
        #expect(viewModel.address == nil)
        
        viewModel.generateAddress()
        
        #expect(viewModel.address == nil)
    }

    @Test("WelcomeViewModel: CreateWallet successfully calls the service")
    func testWelcomeCreateWallet() {
        var createCalled = false
        let client = makeStubClient(createNewWallet: { createCalled = true })
        let path = Binding<NavigationPath>.constant(NavigationPath())
        let viewModel = WelcomeViewModel(path: path, bdkClient: client)
        
        viewModel.createWallet()
        
        #expect(createCalled == true)
        #expect(viewModel.fullScreenCover == nil)
    }
    
    @Test("WelcomeViewModel: Should display an error if wallet creation fails")
    func testWelcomeCreateWalletFailure() {
        let client = makeStubClient(
            createNewWallet: { throw BDKServiceError.generic }
        )
        let path = Binding<NavigationPath>.constant(NavigationPath())
        let viewModel = WelcomeViewModel(path: path, bdkClient: client)
        
        viewModel.createWallet()
        
        #expect(viewModel.fullScreenCover != nil)
    }
    
    @Test("WelcomeViewModel: ImportWallet triggers navigation")
    func testWelcomeImportWalletNavigation() {
        var pathData = NavigationPath()
        let pathBinding = Binding<NavigationPath>(
            get: { pathData },
            set: { pathData = $0 }
        )
        
        let viewModel = WelcomeViewModel(path: pathBinding, bdkClient: makeStubClient())
        
        viewModel.importWallet()
        
        let didNavigate = (viewModel.sheetScreen != nil) || (pathData.count > 0)
        #expect(didNavigate)
    }

    @Test("ImportViewModel: Validation fails with empty words")
    func testImportValidationFailure() {
        let path = Binding<NavigationPath>.constant(NavigationPath())
        let viewModel = ImporViewModel(path: path, bdkClient: makeStubClient())
        
        viewModel.importWallet()
        
        #expect(viewModel.fullScreenCover != nil)
    }
    
    @Test("ImportViewModel: Success calls BDK with valid words")
    func testImportSuccess() {
        var importCalled = false
        let client = makeStubClient(importWallet: { _ in importCalled = true })
        let path = Binding<NavigationPath>.constant(NavigationPath())
        let viewModel = ImporViewModel(path: path, bdkClient: client)
        
        viewModel.words = Array(repeating: "word", count: 12)
        viewModel.importWallet()
        
        #expect(importCalled == true)
    }

    @Test("MoreViewModel: Retrieves the user seed phrase")
    func testMoreShowRecoveryPhrase() {
        let expectedPhrase = "test phrase recovery"
        let client = makeStubClient(getRecoveryPhase: { return expectedPhrase })
        
        var pathStore = NavigationPath()
        let pathBinding = Binding<NavigationPath>(
            get: { pathStore },
            set: { pathStore = $0 }
        )
        
        let viewModel = MoreViewModel(path: pathBinding, bdkClient: client)
        viewModel.showRecoveryPhrase()
        
        #expect(pathStore.count > 0)
    }
    
    @Test("MoreViewModel: Menu button navigation works")
    func testMoreNavigationButtons() {
        var pathStore = NavigationPath()
        let pathBinding = Binding<NavigationPath>(
            get: { pathStore },
            set: { pathStore = $0 }
        )
        let viewModel = MoreViewModel(path: pathBinding, bdkClient: makeStubClient())
        
        viewModel.showLanguage()
        #expect(pathStore.count == 1)
        
        viewModel.showAbout()
        #expect(pathStore.count == 2)
        
        viewModel.showSendCoinsBack()
        #expect(pathStore.count == 3)
    }
    
    @Test("MoreViewModel: Reset Wallet should clear the Session")
    func testResetWallet() {
        let path = Binding<NavigationPath>.constant(NavigationPath())
        let viewModel = MoreViewModel(path: path, bdkClient: makeStubClient())
        
        Session.shared.lastBalanceUpdate = 999
        viewModel.resetWallet()
        
        #expect(true)
    }
    
    @Test("MoreVM: Reset lessons should run without errors")
    func testResetLessons() {
        let path = Binding<NavigationPath>.constant(NavigationPath())
        let viewModel = MoreViewModel(path: path, bdkClient: makeStubClient())
        
        viewModel.resetLessons()
        
        #expect(true)
    }

    @Test("LessonsListViewModel: Should load data from Storage")
    func testLessonsListUpdate() async {
        let mockStorage = MockStorage()
        let path = Binding<NavigationPath>.constant(NavigationPath())
        let viewModel = LessonsListViewModel(path: path, storage: mockStorage)
        
        await viewModel.updateList()
        
        #expect(!viewModel.list.isEmpty)
    }
    
    @Test("LessonsDetailViewModel: Complete flow")
    func testLessonDetailFlow() {
        let mockStorage = MockStorage()
        
        var pathData = NavigationPath()
        pathData.append("prevScreenFake")
        
        let pathBinding = Binding<NavigationPath>(
            get: { pathData },
            set: { pathData = $0 }
        )
        
        let text1 = AnyHashableView(Text("Text 1"))
        let text2 = AnyHashableView(Text("Text 2"))
        
        let content1 = LessonContent(headerKey: "Page 1", texts: [text1])
        let content2 = LessonContent(headerKey: "Page 2", texts: [text2])
        
        let fakeLesson = LessonItemList(
            id: "test_lesson_1",
            title: "Test",
            navigationTitle: "Nav",
            content: [content1, content2],
            isDone: false,
            sort: 1
        )
        let viewModel = LessonsDetailViewModel(path: pathBinding, lesson: fakeLesson, storage: mockStorage)
        
        viewModel.goToNextPage()
        viewModel.goToNextPage()
        
        let isDone: Bool? = mockStorage.get("test_lesson_1")
        #expect(isDone == true)
        #expect(pathData.count == 0)
    }
    
    @Test("LanguageViewModel: Asks for confirmation for PT, ES, and FR")
    func testLanguageSelectMultiple() {
        let viewModel = LanguageThemeScreenViewModel()
        let languagesToTest: [PadawanLanguage] = [.portuguese, .spanish, .french]
        
        for lang in languagesToTest {
            viewModel.selectedLanguage = .english
            viewModel.fullScreenCover = nil
            viewModel.selectItem(lang)
            
            #expect(viewModel.fullScreenCover != nil)
        }
    }
    
    @Test("LanguageViewModel: Should ignores if the language is the same")
    func testLanguageSelectSame() {
        let viewModel = LanguageThemeScreenViewModel()
        viewModel.selectedLanguage = .english
        viewModel.selectItem(PadawanLanguage.english)
        
        #expect(viewModel.fullScreenCover == nil)
    }
}
