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
    
    @Environment(WalletViewModel.self) private var walletViewModel
    
    @State private var feesSatsPerVByte = 4.0
    @State private var isEditing = false
    @State private var satsAmount: String = ""
    @State private var btcAddress: String = ""
    @State private var satsBalance: String = "0"
    @State private var feeSatPerVbyte: Float = 0.0
    
    @Binding var navigationPath: [String]
    
    @State private var sendNotReady = false
    @State private var verifyReady = false
    @State private var isSent: Bool = false
    
    var body: some View {
        
        VStack{
            
            HStack{
                Spacer()
                Text("balance")
                    .font(.title)
            }
            
            HStack{
                Spacer()
                Text(walletViewModel.balanceText)
                    .font(.title2)
                Text(" sats")
                    .font(.title2)
            }
            
            HStack{
                Text("amount")
                    .font(/*@START_MENU_TOKEN@*/.title/*@END_MENU_TOKEN@*/)
                Spacer()
            }
            
            VStack(alignment: .leading) {
                TextField("enter_amount_sats", text: $satsAmount)
                    //.textFieldStyle(RoundedBorderTextFieldStyle())
                    .keyboardType(.numberPad)
                    .submitLabel(.return)
            }
            .padding()
            .overlay(RoundedRectangle(cornerRadius: 10)
                .stroke(lineWidth: 1)
                .foregroundColor(Color.gray))
            
            HStack{
                Text("to_address")
                    .font(/*@START_MENU_TOKEN@*/.title/*@END_MENU_TOKEN@*/)
                Spacer()
            }
            
            HStack {
                
                TextField("enter_a_bitcoin_testnet_address", text: $btcAddress)
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
                Text("fees_sats_vbytes")
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
                    Text("\(Int(feesSatsPerVByte))")
                        .foregroundColor(isEditing ? .red : .blue)
                        .font(.title2)
            }
            .navigationTitle("send_bitcoin")
        
            Spacer()
            
            Button(action: {
                
                if (btcAddress == "" || satsAmount == "") {
                    sendNotReady = true
                }
                else {
                    verifyReady = true
                    sendNotReady = false
                }
            }, label: {
                Text("verify_transaction")
                    .font(.headline)
                    .foregroundColor(.white)
                    .frame(height: 55)
                    .frame(maxWidth: .infinity)
                    .background(Color.orange)
                    .cornerRadius(20)
            })
            .alert("Alert",
                   isPresented: $sendNotReady) {
                   Button("Ok", role: .cancel) {
                         // TODO
                   }
            } message: {
                   Text("Please_enter_Amount_and_Address")
            }
            
        } //VStack
        .padding(40)
        .onAppear(perform: {
            //viewModel.sync()
            walletViewModel.toggleBTCDisplay(displayOption: "sats")
        })
        .sheet(isPresented: $verifyReady, onDismiss: {
            
            isSent == true ? navigationPath.removeAll() : () //navigate to wallet view if send sucessful
        })
        {
            VerifyView(walletViewModel: walletViewModel,feesSatsPerVByte: $feesSatsPerVByte, satsAmount: $satsAmount, btcAddress: $btcAddress, verifyReady: $verifyReady, isSent: $isSent)
                .presentationDetents([.medium]) //half height sheet
                .presentationCornerRadius(50)
                .presentationBackground(alignment: .bottom) {
                    LinearGradient(colors: [Color.orange, Color.white], startPoint: .bottomLeading, endPoint: .topTrailing)
                }
                //.presentationBackground(.thinMaterial)
        }
        
    } //body
}

struct VerifyView: View {
    
    @Bindable var walletViewModel: WalletViewModel
    
    @Binding var feesSatsPerVByte: Double
    @Binding var satsAmount: String
    @Binding var btcAddress: String
    @Binding var verifyReady: Bool
    @Binding var isSent: Bool

    var body: some View {
        
        VStack {
            Text("confirm_transaction").font(.title)
            
            Spacer()
            HStack {
                Text("Send_Amount").font(.headline)
                Spacer()
            }
            HStack {
                Text(satsAmount)
                Text("satoshis")
                Spacer()
            }
            
            Spacer()
            HStack {
                Text("To_Address").font(.headline)
                Spacer()
            }
            HStack {
                Text(btcAddress)
                .fixedSize(horizontal: false, vertical: true)
//                Text("bc1qu5ujlp9dkvtgl98jakvw9ggj9uwyk79qhvwvrg") //for testing
//                .allowsTightening(true)
//                .scaledToFit()
//                .lineLimit(2)
//                .minimumScaleFactor(0.7)
                    
                Spacer()
            }
            
            Spacer()
            HStack {
                Text("Total_Fee").font(.headline)
                Spacer()
            }
            HStack {
                Text("\(feesSatsPerVByte.formatted(.number.precision(.fractionLength(0))))")
                Text("satoshis")
                Spacer()
            }
            
            Spacer()
            
        }
        .padding(40)
        
        Button(action: {
            
            walletViewModel.send(recipient: btcAddress, amount: (UInt64(satsAmount) ?? 0), fee: Float(feesSatsPerVByte))
            
            DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
                if self.walletViewModel.sendViewError == nil {
                    self.isSent = true
                    DispatchQueue.main.asyncAfter(deadline: .now() + 1.5) {
                        self.verifyReady.toggle() //cause verify sheet to dismiss
                    }
                } else {
                    self.isSent = false
                }
            }
            
        }, label: {
            Text("confirm_and_broadcast")
                .font(.headline)
                .foregroundColor(.black)
                .frame(height: 55)
                .frame(maxWidth: .infinity)
                .background(Color.orange)
                .cornerRadius(20)
        })
        .padding(40)
        .alert(isPresented: $walletViewModel.showingSendViewErrorAlert) {
            Alert(
                title: Text("Send_Error"),
                message: Text(walletViewModel.sendViewError?.description ?? "Unknown"),
                dismissButton: .default(Text("OK")) {
                    walletViewModel.sendViewError = nil
                }
            )
        }
    }
}

struct SendView_Previews: PreviewProvider {

    static var previews: some View {
        SendView(navigationPath: .constant (["send"]))
            .environment(WalletViewModel())
    }
}
