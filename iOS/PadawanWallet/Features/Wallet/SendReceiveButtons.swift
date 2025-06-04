//
//  SendReceiveButtons.swift
//  PadawanWallet
//
//  Created by thunderbiscuit on 2025-06-04.
//

import SwiftUI

struct ActionButtons: View {
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

struct ActionButton: View {
    var title: String
    var icon: String

    var body: some View {
        HStack {
            Text(title)
            Image(systemName: icon)
        }
        .font(.headline)
        .foregroundColor(.black)
        .padding()
        .frame(maxWidth: .infinity)
        .background(Color.yellow)
        .cornerRadius(12)
        .shadow(color: .black, radius: 0, x: 2, y: 2)
        .contentShape(Rectangle())
    }
}
