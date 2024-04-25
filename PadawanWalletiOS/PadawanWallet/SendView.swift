//
//  SendView.swift
//  PadawanWallet
//
//  Copyright 2024 thunderbiscuit, geigerzaehler, and contributors.
//  Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE.txt file.
//

import SwiftUI
import BitcoinDevKit

struct SendView: View {
    
    @EnvironmentObject var viewModel: WalletViewModel
    
    @State private var feesSatsPerVByte = 4.0
    @State private var isEditing = false
    @State private var satsAmount: String = ""
    @State private var btcAddress: String = ""
    @State private var satsBalance: String = "0"
    @State private var feeSatPerVbyte: Float = 0.0
    
    var onSend : (String, UInt64, Float) -> ()
    @State private var sendFailed = false
    
    var body: some View {
        
        VStack{
            
            HStack{
                Spacer()
                Text("Balance")
                    .font(.title)
            }
            
            HStack{
                Spacer()
                Text(viewModel.balanceText)
                    .font(.title2)
                Text(" sats")
                    .font(.title2)
            }
            
            HStack{
                Text("Amount")
                    .font(/*@START_MENU_TOKEN@*/.title/*@END_MENU_TOKEN@*/)
                Spacer()
            }
            
            VStack(alignment: .leading) {
                TextField("Enter amount sats", text: $satsAmount)
                    //.textFieldStyle(RoundedBorderTextFieldStyle())
                    .keyboardType(.numberPad)
                    .submitLabel(.return)
            }
            .padding()
            .overlay(RoundedRectangle(cornerRadius: 10)
                .stroke(lineWidth: 1)
                .foregroundColor(Color.gray))
            
            HStack{
                Text("Address")
                    .font(/*@START_MENU_TOKEN@*/.title/*@END_MENU_TOKEN@*/)
                Spacer()
            }
            
            HStack {
                
                TextField("Enter a bitcoin testnet address", text: $btcAddress)
                    .keyboardType(.asciiCapable)
                    .submitLabel(.return)
                Image(systemName: "camera")
            }
            .padding()
            .overlay(RoundedRectangle(cornerRadius: 10)
                .stroke(lineWidth: 1)
                .foregroundColor(Color.gray))
            
            Spacer()
            HStack{
                Text("Fees")
                    .font(/*@START_MENU_TOKEN@*/.title/*@END_MENU_TOKEN@*/)
                Spacer()
            }
            
            VStack {
                Slider(
                    value: $feesSatsPerVByte,
                    in: 1...8,
                    step: 1) {
                        Text("Speed")
                    } minimumValueLabel: {
                        Text("1")
                    } maximumValueLabel: {
                        Text("8")
                    } onEditingChanged: { editing in
                        isEditing = editing
                    }
                    Text("\(Int(feesSatsPerVByte)) sats/vByte")
                        .foregroundColor(isEditing ? .red : .blue)
                        .font(.title2)
            }
            .navigationTitle("Send Bitcoin")
        
            Spacer()
            
            Button(action: {
                
                //btcAddress = "bc1qu5ujlp9dkvtgl98jakvw9ggj9uwyk79qhvwvrg" //for testing
                //onSend(btcAddress, (UInt64(satsAmount) ?? 0), feeSatPerVbyte)
                let sendResult = viewModel.onSend(recipient: btcAddress, amount: (UInt64(satsAmount) ?? 0), fee: feeSatPerVbyte)
                
                sendFailed = true //sendResult
                
            }, label: {
                Text("Verify Transaction")
                    .font(.headline)
                    .foregroundColor(.white)
                    .frame(height: 55)
                    .frame(maxWidth: .infinity)
                    .background(Color.orange)
                    .cornerRadius(20)
            })
//        .alert("Send Failed",
//               isPresented: $sendFailed) {
//               Button("Ok", role: .destructive) {
//                     // TODO
//               }
//        } message: {
//               Text("Please, check entries")
//        }
            
        }.padding(40)
            .onAppear(perform: {
                //viewModel.sync()
                viewModel.toggleBTCDisplay(displayOption: "sats")
            }
        )
    }
}

struct SendView_Previews: PreviewProvider {
    static func onSend(to: String, amount: UInt64, fee: Float) -> () {
        
    }
    static var previews: some View {
        SendView(onSend: self.onSend)
            .environmentObject(WalletViewModel())
    }
}
