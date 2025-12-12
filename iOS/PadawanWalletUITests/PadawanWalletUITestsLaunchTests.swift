//
//  PadawanWalletUITestsLaunchTests.swift
//  PadawanWalletUITests
//
//  Created by thunderbiscuit on 2025-04-16.
//

import XCTest

final class PadawanWalletUITestsLaunchTests: XCTestCase {

    override class var runsForEachTargetApplicationUIConfiguration: Bool {
        true
    }

    override func setUpWithError() throws {
        continueAfterFailure = false

        addUIInterruptionMonitor(withDescription: "System Alerts") { (alert) -> Bool in
            let buttons = alert.buttons
            
            if buttons.count > 0 {
                let lastButton = buttons.element(boundBy: buttons.count - 1)
                if lastButton.exists {
                    lastButton.tap()
                    return true
                }
            }
            return false
        }
    }
    
    @MainActor
    func testLaunch_OnboardingFlow() throws {
        let app = XCUIApplication()
        app.launchArguments = ["-resetOnboarding"]
        app.launch()
        
        let nextButton = app.buttons["onboardingNextButton"]
        let prevButton = app.buttons["onboardingPrevButton"]
        
        XCTAssertFalse(prevButton.exists)
        
        for _ in 1...3 {
            XCTAssertTrue(nextButton.waitForExistence(timeout: 5))
            nextButton.tap()
            XCTAssertTrue(prevButton.waitForExistence(timeout: 2.0))
        }
        
        let createButton = app.buttons["createWalletButton"]
        XCTAssertTrue(createButton.waitForExistence(timeout: 5))
        
        let importButton = app.buttons["importWalletButton"]
        XCTAssertTrue(importButton.exists)
        
        let attachment = XCTAttachment(screenshot: app.screenshot())
        attachment.name = "Launch - Onboarding Flow Complete"
        attachment.lifetime = .keepAlways
        add(attachment)
    }
    
    @MainActor
    func testLaunch_WalletFlow() throws {
        let app = XCUIApplication()
        
        app.launchArguments = ["-skipOnboarding"]
        app.launch()
        
        let syncButton = app.buttons["walletSyncButton"]
        XCTAssertTrue(syncButton.waitForExistence(timeout: 10))
        
        let unitSelector = app.buttons["walletUnitSelector"]
        XCTAssertTrue(unitSelector.exists)
        
        let balanceLabel = app.descendants(matching: .any)["walletBalanceLabel"]
        XCTAssertTrue(balanceLabel.exists)
        
        let getCoinsButton = app.buttons["walletGetCoinsButton"]
        XCTAssertTrue(getCoinsButton.exists)
        
        let sendButton = app.buttons["walletSendButton"]
        let receiveButton = app.buttons["walletReceiveButton"]
        XCTAssertTrue(sendButton.exists)
        XCTAssertTrue(receiveButton.exists)
        
        let attachment = XCTAttachment(screenshot: app.screenshot())
        attachment.name = "Launch - Wallet Flow Complete"
        attachment.lifetime = .keepAlways
        add(attachment)
    }
    
    @MainActor
    func testLaunchPerformance() throws {
        measure(metrics: [XCTApplicationLaunchMetric()]) {
            XCUIApplication().launch()
        }
    }
}
