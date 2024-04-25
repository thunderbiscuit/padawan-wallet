//
//  LearnView.swift
//  PadawanWallet
//
//  Copyright 2024 thunderbiscuit, geigerzaehler, and contributors.
//  Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE.txt file.
//

import SwiftUI

struct LearnView: View {

    @EnvironmentObject var viewModel: WalletViewModel
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
                
                Text("\(String(viewModel.blockHeight))")
                    //.font(.largeTitle)
                    .font(.system(size: 60))
                    .foregroundColor(.blue)
                
                Spacer()
                
            }
        }
        .onAppear{
            viewModel.load()
            viewModel.getBlockHeight()
        }
    }
}

#Preview {
    LearnView(selectedTab: .constant (1))
        .environmentObject(WalletViewModel())
}
