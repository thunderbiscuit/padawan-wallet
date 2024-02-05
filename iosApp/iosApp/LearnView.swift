//
//  LearnView.swift
//  iosApp
//
//  Created by geigerzaehler on 2023-12-07.
//  Copyright © 2023 orgName. All rights reserved.
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
