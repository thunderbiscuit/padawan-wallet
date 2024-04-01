//
//  WalletView.swift
//  iOSApp
//
//  Copyright 2024 thunderbiscuit, geigerzaehler, and contributors.
//  Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE.txt file.
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
                                    ReceiveView( navigationPath: $navigationPath)
                                case "Send ↑":
                                    SendView(onSend: { recipient, amount, fee in
                                        do {
                                            let address = try Address(address: recipient, network: wallet.network())
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
                    
                    if viewModel.transactions.isEmpty {
                        Text("No Transactions")
                            .font(.caption)
                            .listRowInsets(EdgeInsets())
                            .listRowSeparator(.hidden)
                    } else {
                        ForEach(
                            viewModel.transactions.sorted(
                                by: {
                                    $0.confirmationTime?.timestamp ?? $0.received > $1.confirmationTime?
                                        .timestamp ?? $1.received
                                }
                            ),
                            id: \.txid
                        ) { transaction in
                            
//                                NavigationLink(
//                                    destination: TransactionDetailsView(
//                                        transaction: transaction,
//                                        amount:
//                                            transaction.sent > 0
//                                            ? transaction.sent - transaction.received
//                                            : transaction.received - transaction.sent
//                                    )
//                                ) {
//
                                   WalletTransactionsListItemView(transaction: transaction)
//                                }
                        }
                        .listRowInsets(EdgeInsets())
                        .listRowSeparator(.hidden)
                    }
                    
                }
                .listStyle(.plain)
                
                //Spacer(minLength: 0)
            }
           .padding(30)

        } //navigation stack
        .onAppear{
            viewModel.load()
        }
    } //body
}

struct WalletTransactionsListItemView: View {
    let transaction: TransactionDetails
    let isRedacted: Bool

    init(transaction: TransactionDetails, isRedacted: Bool = false) {
        self.transaction = transaction
        self.isRedacted = isRedacted
    }

    var body: some View {
        HStack(spacing: 15) {
            
            if isRedacted {
                Image(
                    systemName:
                        "circle.fill"
                )
                .font(.largeTitle)
                .symbolRenderingMode(.palette)
                .foregroundStyle(
                    Color.gray.opacity(0.5)
                )
            } else {
                Image(
                    systemName:
                        transaction.sent > 0
                    ? "arrow.up.circle.fill" : "arrow.down.circle.fill"
                )
                .font(.largeTitle)
                .symbolRenderingMode(.palette)
                .foregroundStyle(
                    transaction.confirmationTime != nil
                    ? Color.orange : Color.secondary,
                    isRedacted ? Color.gray.opacity(0.5) : Color.gray.opacity(0.05)
                )
            }
            
            VStack(alignment: .leading, spacing: 5) {
                Text(transaction.txid)
                    .truncationMode(.middle)
                    .lineLimit(1)
                    .fontDesign(.monospaced)
                    .fontWeight(.semibold)
                    .font(.callout)
                    .foregroundColor(.primary)
                Text(
                    transaction.confirmationTime?.timestamp.toDate().formatted(
                        .dateTime.day().month().hour().minute()
                    )
                    ?? "Unconfirmed"
                )
            }
            .foregroundColor(.secondary)
            .font(.caption)
            .padding(.trailing, 30.0)
            .redacted(reason: isRedacted ? .placeholder : [])
            
            Spacer()
            Text(
                transaction.sent > transaction.received
                ? "- \(transaction.sent - transaction.received) sats"
                : "+ \(transaction.received - transaction.sent) sats"
            )
            .font(.caption)
            .fontWeight(.semibold)
            .fontDesign(.rounded)
            .redacted(reason: isRedacted ? .placeholder : [])
        }
        .padding(.vertical, 15.0)
        .padding(.vertical, 5.0)
    }//body
}

#Preview {
    WalletView(selectedTab: .constant (0))
        .environmentObject(WalletViewModel())
}
