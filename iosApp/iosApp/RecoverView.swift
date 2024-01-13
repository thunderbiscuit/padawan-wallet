//
//  RecoverView.swift
//  MyFirstApp
//
//  Created by geigerzaehler on 12/1/24.
//

import SwiftUI

struct RecoverView: View {
    @State private var words = Array(repeating: "", count: 12)
    @Environment(\.dismiss) var dismiss
    
    var body: some View {
        
        VStack {
            
            VStack {
                Text("Recover a wallet")
                    .bold()
                    .font(/*@START_MENU_TOKEN@*/.title/*@END_MENU_TOKEN@*/)
                Text("Enter your 12 word recovery phrase below")
            }
            
            VStack {
                
                ScrollView {
                    ForEach(0..<12) { i in
                        TextField(
                            "Word \(i + 1)",
                            text: $words[i]
                        )
                    }
                    .disableAutocorrection(true).padding(.bottom, 10)
                    .textInputAutocapitalization(.never)
                    
                }
                .textFieldStyle(.roundedBorder)
                
                Spacer(minLength: 20)
                
                Button(action: {
                    //TODO use $words to recover address
                    dismiss()
                }, label: {
                    Text("Recover Wallet")
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
                    Text("Cancel")
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
    }
}
