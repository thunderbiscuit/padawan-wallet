//
//  BalanceCard.swift
//  PadawanWallet
//
//  Created by thunderbiscuit on 2025-06-04.
//

import SwiftUI

struct BalanceCard: View {
    var body: some View {
        ZStack(alignment: .topLeading) {
            RoundedRectangle(cornerRadius: 20)
                .fill(Color("BalanceCardBackground"))
                .shadow(color: .black.opacity(0.2), radius: 5, x: 5, y: 5)

            VStack(alignment: .leading, spacing: 16) {
                HStack {
                    Text("bitcoin signet")
                        .foregroundColor(.orange)
                        .font(.subheadline)

                    Spacer()

                    HStack {
                        Text("btc")
                            .foregroundColor(.black.opacity(0.4))
                        Text("sats")
                            .bold()
                    }
                    .padding(.horizontal, 12)
                    .padding(.vertical, 6)
                    .background(Color("ToggleBackground"))
                    .cornerRadius(12)
                }

                HStack {
                    Text("0")
                        .font(.system(size: 48, weight: .bold))
                    Text("sats")
                        .font(.title3)
                        .foregroundColor(.black)
                }

                Button("sync") {}
                    .font(.headline)
                    .foregroundColor(.white)
                    .padding(.vertical, 8)
                    .padding(.horizontal, 40)
                    .background(Color.black)
                    .clipShape(Capsule())
            }
            .padding()
        }
        .frame(maxWidth: .infinity)
    }
}
