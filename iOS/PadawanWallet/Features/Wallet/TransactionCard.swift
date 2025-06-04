//
//  TransactionCard.swift
//  PadawanWallet
//
//  Created by thunderbiscuit on 2025-06-04.
//

import SwiftUI

struct TransactionsCard: View {
    var body: some View {
        VStack(alignment: .leading, spacing: 16) {
            Text("Transactions")
                .font(.title3)
                .bold()

            VStack(alignment: .leading, spacing: 12) {
                Text("Hey! It looks like your transaction list is empty. Take a look around, and come back to get some coins so you can start playing with the wallet!")

                Button("Get Signet coins") {}
                    .font(.headline)
                    .foregroundColor(.black)
                    .padding()
                    .background(Color.yellow)
                    .cornerRadius(12)
                    .shadow(color: .black, radius: 0, x: 2, y: 2)
            }
            .padding()
            .background(Color.white)
            .cornerRadius(16)
            .overlay(
                RoundedRectangle(cornerRadius: 16)
                    .stroke(Color.black, lineWidth: 1)
            )
        }
    }
}
