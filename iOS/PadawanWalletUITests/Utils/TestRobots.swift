//
//  TestRobots.swift
//  PadawanWalletUITests
//
//  Created by Vinicius Silva Moreira on 14/12/25.
//

import XCTest

class BaseRobot {
    let app: XCUIApplication
    let testCase: XCTestCase
    
    init(app: XCUIApplication, testCase: XCTestCase) {
        self.app = app
        self.testCase = testCase
    }
}

// MARK: - wallet UI flow

final class WalletRobot: BaseRobot {
    
    private lazy var walletTab = app.tabBars.buttons.element(boundBy: 0)
    private lazy var receiveButton = app.buttons["walletReceiveButton"]
    private lazy var generateButton = app.buttons["receiveGenerateButton"]
    private lazy var addressLabel = app.buttons["accessibility_address_label"]
    private lazy var sendButton = app.buttons["walletSendButton"]
    private lazy var amountInput = app.textFields["sendAmountInput"]
    private lazy var addressInput = app.textFields["sendAddressInput"]
    private lazy var verifyButton = app.buttons["sendVerifyButton"]
    private lazy var backButton = app.navigationBars.buttons.element(boundBy: 0)
    
    @discardableResult
    func navigateToWallet() -> Self {
        tap(walletTab)
        return self
    }
    
    @discardableResult
    func openReceiveFlow() -> Self {
        tap(receiveButton)
        return self
    }
    
    @discardableResult
    func generateAddress() -> Self {
        tap(generateButton)
        checkExists(addressLabel)
        return self
    }
    
    @discardableResult
    func goBack() -> Self {
        tap(backButton)
        return self
    }
    
    @discardableResult
    func openSendFlow() -> Self {
        tap(sendButton)
        return self
    }
    
    @discardableResult
    func fillTransactionDetails(amount: String, address: String) -> Self {
        tap(amountInput)
        amountInput.typeText(amount)
        
        tap(addressInput)
        addressInput.typeText(address)
        
        if app.navigationBars.firstMatch.exists {
            app.navigationBars.firstMatch.coordinate(withNormalizedOffset: CGVector(dx: 0.5, dy: 0.5)).tap()
        }
        return self
    }
    
    
    @discardableResult
    func verifyTransactionAndExpectError() -> Self {
        tap(verifyButton)
        
        let errorAlert = app.alerts.firstMatch
        if errorAlert.waitForExistence(timeout: 5) {
            errorAlert.buttons.firstMatch.tap()
        } else {
            XCTAssertFalse(app.buttons["confirmBroadcastButton"].exists)
        }
        return self
    }
}

// MARK: - lessons UI flow

final class LessonsRobot: BaseRobot {
    
    private lazy var lessonsTab = app.tabBars.buttons.element(boundBy: 1)
    private lazy var firstLessonItem = app.buttons["lessonItem_1"]
    private lazy var nextButton = app.buttons["lessonNextButton"]
    
    @discardableResult
    func navigateToLessons() -> Self {
        tap(lessonsTab)
        return self
    }
    
    @discardableResult
    func openFirstLesson() -> Self {
        tap(firstLessonItem)
        return self
    }
    
    @discardableResult
    func completeLessonFlow() -> Self {
        var steps = 0
        let maxSteps = 10
        
        while nextButton.waitForExistence(timeout: 2.0) && steps < maxSteps {
            nextButton.tap()
            steps += 1
        }
        return self
    }
    
    @discardableResult
    func verifyLessonCompletion() -> Self {
        checkExists(firstLessonItem)
        
        let images = firstLessonItem.images
        let checkmarkIcon = images.firstMatch
        XCTAssertTrue(
            checkmarkIcon.waitForExistence(timeout: 5)
        )
        
        return self
    }
}

// MARK: - settings UI flow

final class SettingsRobot: BaseRobot {
    
    private lazy var settingsTab = app.tabBars.buttons.element(boundBy: 2)
    private lazy var recoveryButton = app.buttons["settingsRecoveryPhrase"]
    private lazy var resetWalletButton = app.buttons["settingsResetWallet"]
    private lazy var backButton = app.navigationBars.buttons.element(boundBy: 0)
    
    @discardableResult
    func navigateToSettings() -> Self {
        tap(settingsTab)
        return self
    }
    
    @discardableResult
    func viewRecoveryPhrase() -> Self {
        tap(recoveryButton)
        checkExists(app.descendants(matching: .any).matching(identifier: "recoveryWord_0").firstMatch)
        tap(backButton)
        return self
    }
    
    @discardableResult
    func performResetWallet() -> Self {
        app.swipeUp()
        tap(resetWalletButton)
        return self
    }
    
    @discardableResult
    func verifyResetSuccess() -> Self {
        let welcomeButton = app.buttons.firstMatch
        checkExists(welcomeButton)
        return self
    }
    
    @discardableResult
    func openSendCoinsBackAndCopy() -> Self {
        tap(app.buttons["settingsSendCoinsBack"])
        
        let copyBtn = app.buttons["sendCoinsBackCopyBtn"]
        checkExists(copyBtn)
        tap(copyBtn)
        
        let checkIcon = copyBtn.images["checkmark.circle.fill"]
        XCTAssertTrue(checkIcon.waitForExistence(timeout: 2))
        
        tap(backButton)
        return self
    }
    
    @discardableResult
    func changeLanguageAndTheme() -> Self {
        tap(app.buttons["settingsLanguage"])
        
        let tatooineBtn = app.buttons["selection_Tatooine"]
        if tatooineBtn.exists {
            tap(tatooineBtn)
            
            XCTAssertTrue(tatooineBtn.isSelected)
        }
        
        tap(backButton)
        return self
    }
    
    @discardableResult
    func verifyAboutScreen() -> Self {
        tap(app.buttons["settingsAbout"])
        
        let privacyLink = app.links.firstMatch
        checkExists(privacyLink)
        
        tap(backButton)
        return self
    }
    
    @discardableResult
    func resetLessonsHistory() -> Self {
        app.swipeUp()
        let resetLessonsBtn = app.buttons["settingsResetLessons"]
        tap(resetLessonsBtn)
        return self
    }
}
