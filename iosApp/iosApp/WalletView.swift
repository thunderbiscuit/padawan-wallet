//
//  WalletView.swift
//  iosApp
//
//  Created by geigerzaehler on 2023-12-07.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import BitcoinDevKit

struct WalletView: View {
    
    @EnvironmentObject var viewModel: WalletViewModel
    @Binding var selectedTab: Int
   
    @State var navigationPath: [String] = []
    
    @State private var satsBTC = "BTC"
    var amountDisplayOptions = ["BTC", "sats"]
    
    var body: some View {

        NavigationStack(path: $navigationPath) {
            
            VStack(spacing: 40) {
                
                RoundedRectangle(cornerRadius: 20)
                    .frame(height: 200)
                    .foregroundColor( Color.purple)
                    .overlay(
                        VStack {
                            Spacer()
                            HStack {
                                Text("Bitcoin Testnet")
                                //VStack {
                                    Picker("Display in BTC or sats?", selection: $satsBTC) {
                                        ForEach(amountDisplayOptions, id: \.self) {
                                            Text($0)
                                        }
                                    }
                                    .pickerStyle(.segmented)
                                    .onChange(of: satsBTC) {
                                        if satsBTC == "BTC" {
                                            viewModel.toggleBTCDisplay(displayOption: "BTC")
                                        } else {
                                            viewModel.toggleBTCDisplay(displayOption: "sats")
                                        }
                                    }
                                //}
                            }
                            
                            Spacer()
                            VStack {
                                Text(viewModel.balanceText).font(.largeTitle)
                                Text("\(satsBTC)")
                            }
                            
                            Spacer()
                            Button(action: {
                                viewModel.sync()
                                satsBTC = amountDisplayOptions[0] //reset segment picker to display BTC
                            }, label: {
                                Text("Sync \(Image(systemName: "bitcoinsign.arrow.circlepath"))")
                                .font(.headline)
                                .foregroundColor(.white)
                                .frame(height: 55)
                                .frame(maxWidth: .infinity)
                                .background(Color.black)
                                .cornerRadius(20)
                            }).padding(60)
                            
                        }.padding(20)
                    )
                
                HStack {
                    Button(action: {
                        navigationPath.append("Receive ↓")
                    }, label: {
                        Text("Receive ↓")
                            .font(.headline)
                            .foregroundColor(.white)
                            .frame(height: 55)
                            .frame(maxWidth: .infinity)
                            .background(Color.teal)
                            .cornerRadius(20)
                    })
                    
                    Button(action: {
                        navigationPath.append("Send ↑")
                    }, label: {
                        Text("Send ↑")
                            .font(.headline)
                            .foregroundColor(.white)
                            .frame(height: 55)
                            .frame(maxWidth: .infinity)
                            .background(Color.orange)
                            .cornerRadius(20)
                    })
                }
                .navigationDestination(for: String.self) { navigaionValue in
                    
                    switch viewModel.state {
                        
                        case .loaded(let wallet, let blockchain):
                            do {
                                switch navigaionValue {
                                    
                                case "Receive ↓":
                                    ReceiveView()
                                case "Send ↑":
                                    SendView(onSend: { recipient, amount, fee in
                                        do {
                                            let address = try Address(address: recipient)
                                            let script = address.scriptPubkey()
                                            let txBuilder = TxBuilder().addRecipient(script: script, amount: amount)
                                                .feeRate(satPerVbyte: fee)
                                            let details = try txBuilder.finish(wallet: wallet)
                                            let _ = try wallet.sign(psbt: details.psbt, signOptions: nil)
                                            let tx = details.psbt.extractTx()
                                            try blockchain.broadcast(transaction: tx)
                                            let txid = details.psbt.txid()
                                            print(txid)
                                           
                                        } catch let error {
                                            print(error)
                                        }
                                    })
                                default:
                                    Text("undefined button value")
                                }
                            }
                        default: do { }
                    }
                }
                
                HStack() {
                    Text("Transactions")
                        .font(.headline)
                    Spacer()
                }
                
                List {
                    ForEach(0..<25) { _ in
                      Text("txid 123456789abcdefghi")
                    }
                }
                
//                if viewModel.transactions.isEmpty {
//                    Text("No transactions yet.").padding()
//                }
//                else {
                    //                        List {
                    //                            ForEach(0..<viewModel.transactions.count) { each in
                    //                                Text(viewModel.transactions[each].txid)
                    //                            }
                    //                        }
//                }
                
                //Spacer(minLength: 0)
            }
            .padding(40)


        } //navigation stack
        .onAppear(perform: viewModel.load)

    }
}

#Preview {
    WalletView(selectedTab: .constant (0))
        .environmentObject(WalletViewModel())
}
