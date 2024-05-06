//
//  LearnView.swift
//  PadawanWallet
//
//  Copyright 2024 thunderbiscuit, geigerzaehler, and contributors.
//  Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE.txt file.
//

import SwiftUI

struct LearnView: View {

    @Environment(WalletViewModel.self) private var walletViewModel
    @Binding var selectedTab: Int

    var body: some View {
        ZStack {
            //Color.red.ignoresSafeArea()
            VStack {
                
                Image("Logo")
                Spacer()
                
                Text("Block Height")
                    .font(.largeTitle)
                    .foregroundColor(.blue)
                
                Text("\(String(walletViewModel.blockHeight))")
                    //.font(.largeTitle)
                    .font(.system(size: 60))
                    .foregroundColor(.blue)
                
                Spacer()
                
            }
        }
        .onAppear{
            walletViewModel.load()
            walletViewModel.getBlockHeight()
        }
    }
}

#Preview {
    LearnView(selectedTab: .constant (1))
        .environment(WalletViewModel())
}
