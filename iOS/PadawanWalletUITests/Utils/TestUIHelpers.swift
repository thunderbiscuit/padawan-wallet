//
//  TestUIHelpers.swift
//  PadawanWalletUITests
//
//  Created by Vinicius Silva Moreira on 14/12/25.
//

import XCTest

// MARK: - helpers

func tap(_ element: XCUIElement, timeout: TimeInterval = 5, file: StaticString = #file, line: UInt = #line) {
    if element.waitForExistence(timeout: timeout) {
        element.tap()
    } else {
        XCTFail(file: file, line: line)
    }
}

func checkExists(_ element: XCUIElement, timeout: TimeInterval = 5, file: StaticString = #file, line: UInt = #line) {
    XCTAssertTrue(element.waitForExistence(timeout: timeout), file: file, line: line)
    
}


// MARK: - constants

struct UITestConstants {
    static let testAddress = "tb1qw508d6qejxtdg4y5r3zarvary0c5xw7kxpjzsx"
    static let testAmount = "1200"
}
