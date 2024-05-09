//
//  RecoverView.swift
//  PadawanWallet
//
//  Copyright 2024 thunderbiscuit, geigerzaehler, and contributors.
//  Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE.txt file.
//

import SwiftUI

struct RecoverView: View {
    
    @Environment(WalletViewModel.self) private var walletViewModel
    @State private var words = Array(repeating: "", count: 12)

    @Environment(\.dismiss) var dismiss
    
    var body: some View {
        
        VStack {
            
            VStack {
                Text("recover_a_wallet")
                    .bold()
                    .font(/*@START_MENU_TOKEN@*/.title/*@END_MENU_TOKEN@*/)
                Text("enter_your_12_words")
            }
            
            VStack {
                
                ScrollView {
                    ForEach(0..<12) { i in
                        TextField(
                            "word \(i + 1)",
                            text: $words[i]
                        )
                    }
                    .disableAutocorrection(true).padding(.bottom, 10)
                    .textInputAutocapitalization(.never)
                    Spacer()
                }
                .textFieldStyle(.roundedBorder)
                
                Spacer(minLength: 20)
                
                Button(action: {
                    
                    if !words.isEmpty {
                        do {
                            let joinedWords = words.joined(separator: " ")
                            try walletViewModel.createWallet(words: joinedWords)
                        } catch {
                            //                       self.walletViewError = .Generic(message: "Error Getting Transactions")
                            print("error")
                        }
                    }
                    dismiss()
                }, label: {
                    Text("recover_wallet")
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
                
                Spacer(minLength: 20)
                
                Button(action: {
                    dismiss()
                }, label: {
                    Text("intro_dialog_negative")
                })
                //.font(.system(size: 16, design: .monospaced))
                .font(.title)
                .fontWeight(.bold)
                .frame(maxWidth: .infinity, maxHeight: 40)
                .foregroundColor(Color.white)
                .padding(10)
                .background(Color.red)
                .cornerRadius(20)
                //.shadow(color: Color("Shadow"), radius: 1, x: 5, y: 5)
               
            }
            .padding(40)
        }
    }
}

struct RecoverView_Previews: PreviewProvider {
    static var previews: some View {
        RecoverView()
            .environment(WalletViewModel())
    }
}
