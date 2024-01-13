//
//  SendView.swift
//  iosApp
//
//  Created by geigerzaehler on 2023-12-20.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import BitcoinDevKit

struct SendView: View {
    
    @EnvironmentObject var viewModel: WalletViewModel
    
    @State private var feesSatsPerVByte = 50.0
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
                Text("Amount")
                Spacer()
                Text("Balance ")
                Text(viewModel.balanceText)
                Text(" sats")
            }
            
            VStack(alignment: .leading) {
                TextField("Enter amount sats", text: $satsAmount)
            }
            
            HStack{
                Text("Address")
                Spacer()
            }
            
            VStack(alignment: .leading) {
                TextField("Enter a bitcoin testnet address", text: $btcAddress)
            }
           
            Spacer()
            HStack{
                Text("Fees (sats/vbytes)")
                Spacer()
            }
            
            VStack {
                Slider(
                    value: $feesSatsPerVByte,
                    in: 0...100,
                    step: 1) {
                        Text("Speed")
                    } minimumValueLabel: {
                        Text("0")
                    } maximumValueLabel: {
                        Text("100")
                    } onEditingChanged: { editing in
                        isEditing = editing
                    }
                    Text("\(feesSatsPerVByte)")
                        .foregroundColor(isEditing ? .red : .blue)
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
        .onAppear(perform: viewModel.sync)
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
