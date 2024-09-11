//
//  WelcomeView.swift
//  PadawanWallet
//
//  Copyright 2024 thunderbiscuit, geigerzaehler, and contributors.
//  Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE.txt file.
//

import SwiftUI

struct WelcomeView: View {
    @Environment(WalletViewModel.self) private var walletViewModel
    @Environment(\.dismiss) var dismiss
    @State private var isPresentedRecoverView = false
    
    @AppStorage("isOnboarding") var isOnboarding: Bool?
    
    var body: some View {
        VStack {
            Image("logo")
                .resizable()
                .scaledToFill() // add if you need
                .frame(width: 80.0, height: 80.0)
                .clipped()
            
            Spacer()
                    
            Text("padawan_wallet")
                .bold()
                .font(/*@START_MENU_TOKEN@*/.title/*@END_MENU_TOKEN@*/)
            
            Spacer()
            
            VStack {
                HStack{
                    Text("welcome_statement_1")
                    Spacer()
                }
                Text("\n")
                HStack{
                    Text("welcome_statement_2")
                    Spacer()
                }
                Text("\n")
                HStack{
                    Text("welcome_statement_3").fixedSize(horizontal: false, vertical: true)
                    Spacer()
                }
                Text("\n")
                HStack{
                    Text("welcome_statement_4").fixedSize(horizontal: false, vertical: true)
                    Spacer()
                    Text("\n")
                }
            }
            
            Spacer(minLength: 20)
            
            Button(action: {
                do {
                    try walletViewModel.createWallet(words: nil)
                } catch {
                    print("error")
                }
                
                dismiss()
            }, label: {
                Text("create_a_wallet")
            })
                    //.font(.system(size: 16, design: .monospaced))
                    .font(.title)
                    .fontWeight(.bold)
                    .frame(maxWidth: .infinity, maxHeight: 40)
                    .foregroundColor(Color.white)
                    .padding(10)
                    .background(Color.orange)
                    .cornerRadius(20)
                    //.shadow(color: Color("Shadow"), radius: 1, x: 5, y: 5)
            
            Spacer(minLength: 50)
            HStack {
                Text("already_have_a_wallet")
                Spacer()
                Button(action: {
                    isPresentedRecoverView = true
                }, label: {
                    Text("recover_it_here")
                })
                .fullScreenCover(isPresented: $isPresentedRecoverView, content: {
                    RecoverView()
                })
            }
            
            Spacer()
        }.padding(40)
    }
}

#Preview {
    WelcomeView()
        .environment(WalletViewModel())
}
