//
//  KeyboardResponsive.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 07/08/25.
//

import SwiftUI
import UIKit

extension View {
    func dismissKeyBoard() {
        UIApplication.shared.dismissKeyboard()
    }
}

extension UIApplication {
    func dismissKeyboard() {
        sendAction(
            #selector(UIResponder.resignFirstResponder),
            to: nil,
            from: nil,
            for: nil
        )
    }
}
