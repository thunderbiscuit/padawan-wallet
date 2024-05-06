//
//  MenuView.swift
//  PadawanWallet
//
//  Copyright 2024 thunderbiscuit, geigerzaehler, and contributors.
//  Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE.txt file.
//

import SwiftUI

struct MenuView: View {
    
    @Environment(WalletViewModel.self) private var walletViewModel
    @Binding var selectedTab: Int
    
    @Environment(\.dismiss) var dismiss
    @State private var isPresentedRecoverView = false
    
    @State var isSelected: Bool = false
    
        var body: some View {
    
            VStack(spacing: 40) {
    
                RoundedRectangle(cornerRadius: 20)
                    .frame(height: 200)
                    .foregroundColor(isSelected ? Color.red : Color.purple)
    
                HStack() {
    
                    Button(action: {
                        isSelected.toggle()
                    }, label: {
                        Text("Do Nothing")
                            .font(.headline)
                            .foregroundColor(.white)
                            .frame(height: 55)
                            .frame(maxWidth: .infinity)
                            .background(Color.teal)
                            .cornerRadius(20)
                    })
                    //.fullScreenCover(isPresented: $isSelected, content: {
                    //    ReceiveView(isSelected: $isSelected)
                    //})
    
                    Button(action: {
                        isSelected.toggle()
                        isPresentedRecoverView = true
                    }, label: {
                        Text("Recover Wallet")
                            .font(.headline)
                            .foregroundColor(.white)
                            .frame(height: 55)
                            .frame(maxWidth: .infinity)
                            .background(Color.orange)
                            .cornerRadius(20)
                    })
                    .fullScreenCover(isPresented: $isPresentedRecoverView, content: {
                        RecoverView()
                    })
                }
    
                Text("Double TAP Gesture")
                    .font(.headline)
                    .foregroundColor(.white)
                    .frame(height: 55)
                    .frame(maxWidth: .infinity)
                    .background(Color.blue)
                    .cornerRadius(20)
    //                .onTapGesture {
    //                    isSelected.toggle()
    //                }
                    .onTapGesture(count: 2, perform: {
                        isSelected.toggle()
                    })
    
                Spacer()
            }
            .padding(40)
        }
    

}

#Preview {
    MenuView(selectedTab: .constant (2))
        .environment(WalletViewModel())
}
