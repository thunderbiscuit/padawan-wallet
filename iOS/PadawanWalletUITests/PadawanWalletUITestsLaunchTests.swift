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
}
