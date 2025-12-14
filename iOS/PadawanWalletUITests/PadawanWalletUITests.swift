//
//  PadawanWalletUITests.swift
//  PadawanWalletUITests
//
//  Created by thunderbiscuit on 2025-04-16.
//

import XCTest

final class PadawanWalletUITests: XCTestCase {

    var app: XCUIApplication!

    override func setUpWithError() throws {
        continueAfterFailure = false
        app = XCUIApplication()
        app.launchArguments = ["-skipOnboarding", "-resetOnboarding", "-AppleLanguages", "(en)", "-AppleLocale", "en_US"]
        app.launch()
    }

    override func tearDownWithError() throws {
        app = nil
    }

    func testWalletUI() {
        WalletRobot(app: app, testCase: self)
            .navigateToWallet()
            .openReceiveFlow()
            .generateAddress()
            .goBack()
            .openSendFlow()
            .fillTransactionDetails(
                amount: UITestConstants.testAmount,
                address: UITestConstants.testAddress
            )
            .verifyTransactionAndExpectError()
    }

    func testLessonsUI() {
        LessonsRobot(app: app, testCase: self)
            .navigateToLessons()
            .openFirstLesson()
            .completeLessonFlow()
            .verifyLessonCompletion()
    }

    func testSettingsUI() {
        SettingsRobot(app: app, testCase: self)
            .navigateToSettings()
            .viewRecoveryPhrase()
            .openSendCoinsBackAndCopy()
            .verifyAboutScreen()
            .changeLanguageAndTheme()
            .resetLessonsHistory()
            .performResetWallet()
            .verifyResetSuccess()
    }
}
