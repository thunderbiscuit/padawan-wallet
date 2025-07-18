/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import SwiftUI

struct SendReceiveButtons: View {
    var body: some View {
        HStack(spacing: 16) {
            NavigationLink(destination: ReceiveScreen()) {
                ActionButton(title: "Receive", icon: "arrow.down")
            }

            NavigationLink(destination: SendScreen()) {
                ActionButton(title: "Send", icon: "arrow.up")
            }
        }
    }
}
