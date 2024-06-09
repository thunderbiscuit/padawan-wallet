//
//  WalletView.swift
//  PadawanWallet
//
//  Copyright 2024 thunderbiscuit, geigerzaehler, and contributors.
//  Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE.txt file.
//

import SwiftUI
import BitcoinDevKit
import class PadawanKmp.FaucetCall
import class PadawanKmp.FaucetService

struct WalletView: View {
    
    @Environment(WalletViewModel.self) private var walletViewModel
    @Binding var selectedTab: Int
   
    @State var navigationPath: [String] = []
    
    @State private var satsBTC = "BTC"
    var amountDisplayOptions = ["BTC", "sats"]
    
    var body: some View {

        NavigationStack(path: $navigationPath) {
            
            VStack(spacing: 20) {
                
                RoundedRectangle(cornerRadius: 20)
                    .frame(height: 200)
                    .foregroundColor( Color.purple)
                    .overlay(
                        VStack {
                            Spacer()
                            HStack {
                                Text("bitcoin_testnet")
                                
                                Picker("Display in BTC or sats?", selection: $satsBTC) {
                                    ForEach(amountDisplayOptions, id: \.self) {
                                        Text($0)
                                    }
                                }
                                .pickerStyle(.segmented)
                                .onChange(of: satsBTC) {
                                    if satsBTC == "BTC" {
                                        walletViewModel.toggleBTCDisplay(displayOption: "BTC")
                                    } else {
                                        walletViewModel.toggleBTCDisplay(displayOption: "sats")
                                    }
                                }
                            }
                            
                            Spacer()
                            VStack {
                                Text(walletViewModel.balanceText).font(.largeTitle)
                                Text("\(satsBTC)")
                            }
                            
                            Spacer()
                            Button(action: {
                                walletViewModel.sync()
                                satsBTC = amountDisplayOptions[0] //reset segment picker to display BTC
                            }, label: {
                                Text("sync \(Image(systemName: "bitcoinsign.arrow.circlepath"))")
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
                        navigationPath.append("receive")
                    }, label: {
                        Text("receive")
                            .font(.headline)
                            .foregroundColor(.white)
                            .frame(height: 55)
                            .frame(maxWidth: .infinity)
                            .background(Color.teal)
                            .cornerRadius(20)
                    })
                    
                    Button(action: {
                        navigationPath.append("send")
                    }, label: {
                        Text("send")
                            .font(.headline)
                            .foregroundColor(.white)
                            .frame(height: 55)
                            .frame(maxWidth: .infinity)
                            .background(Color.orange)
                            .cornerRadius(20)
                    })
                }
                .navigationDestination(for: String.self) { navigaionValue in
                    
                    switch walletViewModel.state {
                        
                    case .loaded(_, _):
                            do {
                                switch navigaionValue {
                                    
                                case "receive":
                                    ReceiveView( navigationPath: $navigationPath)
                                case "send":
                                    SendView(navigationPath: $navigationPath)
                                default:
                                    Text("undefined button value")
                                }
                            }
                        default: do { }
                    }
                }
                
                HStack() {
                    Text("transactions")
                        .font(.title)
                    Spacer()
                }
                
                if walletViewModel.transactions.isEmpty {
                    
                    FaucetView()
                    
                } else {
                    List {
                                        
                        ForEach(
                            walletViewModel.transactions.sorted(
                                by: {
                                    $0.confirmationTime?.timestamp ?? $0.received > $1.confirmationTime?
                                        .timestamp ?? $1.received
                                }
                            ),
                            id: \.txid
                        )
                            { transaction in
                                
                                NavigationLink(
                                    destination: TransactionDetailsView(
                                        transaction: transaction,
                                        amount:
                                            transaction.sent > transaction.received
                                            ? transaction.sent - transaction.received
                                            : transaction.received - transaction.sent
                                    )
                                )
                                    {
            
                                        WalletTransactionsListItemView(transaction: transaction)
        //                                .refreshable {
        //                                    viewModel.sync()
        //                                    //viewModel.getBalance()
        //                                    //viewModel.getTransactions()
        //                                    //await viewModel.getPrices()
        //                                }
                                    }
                            }
                            .listRowInsets(EdgeInsets())
                            .listRowSeparator(.hidden)
                        
                    }.listStyle(.plain)
                    
                }//viewModel.transactions.isEmpty
                
            }//VStack
            .padding(30)
           
        } //navigation stack
        .onAppear{
            walletViewModel.load()
        }
    } //body
}


struct FaucetView: View {
    
    @Environment(WalletViewModel.self) var walletViewModel
    @State private var faucetFailed = false
    
    var body: some View {
        
        VStack {
            ZStack {
                RoundedRectangle(cornerRadius: 25, style: .continuous)
                        .stroke(Color.black , lineWidth: 1)
                        .frame(width: 350, height: 150, alignment: Alignment.top   )
                        .layoutPriority(1) // default is 0, now higher priority than Text()
                Text("looks_like_your_transaction_list_is_empty").padding(20)
            }
        
            if walletViewModel.syncState == .synced { //only show button once synced!
                Button(action: {
                    let faucetService = FaucetService()
                    let response: FaucetCall = faucetService.callTatooineFaucet(
                        address: "1234abcd",
                        faucetUrl: "http://padawanwallet.com",
                        faucetUsername: "padawan",
                        faucetPassword: "password"
                    )
                    handleResponse(response: response)
                    faucetFailed = true
                }, label: {
                    HStack{
                        Text("get_coins") + Text(" ") + Text("bitcoin_sign")
                    }
                        .font(.headline)
                        .foregroundColor(.white)
                        .frame(height: 55)
                        .frame(maxWidth: .infinity)
                        .background(Color.orange)
                        .cornerRadius(20)
                })
                .padding(20)
                .alert(
                    "errorFaucet",
                    isPresented: $faucetFailed
                ) {
                    Button("Ok", role: .destructive) {  }
                } message: {
                    Text("try_to_recover_a_wallet_instead")
                }
            }
        }
    }
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
                        transaction.sent > transaction.received
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
                    ?? "unconfirmed"
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

struct TransactionDetailsView: View {
//    @ObservedObject var viewModel: TransactionDetailsViewModel
    @Environment(WalletViewModel.self) var viewModel
    
    let transaction: TransactionDetails
    let amount: UInt64
    @State private var isCopied = false
    @State private var showCheckmark = false

    var body: some View {

        VStack {

            VStack(spacing: 8) {
                Image(systemName: "bitcoinsign.circle.fill")
                    .resizable()
                    .foregroundColor(.orange)
                    .fontWeight(.bold)
                    .frame(width: 100, height: 100, alignment: .center)
                HStack(spacing: 3) {
                    Text(
                        transaction.sent > transaction.received ? "send" : "receive"
                    )
                    if transaction.confirmationTime == nil {
                        Text("unconfirmed")
                    } else {
                        Text("confirmed")
                    }
                }
                .fontWeight(.semibold)
                if let height = transaction.confirmationTime?.height {
                    Text("Block \(height.delimiter)")
                        .foregroundColor(.secondary)
                }
            }
            .font(.caption)

            Spacer()

            VStack(spacing: 8) {
                HStack {
                    Text(amount.delimiter)
                    Text("sats")
                }
                .lineLimit(1)
                .minimumScaleFactor(0.5)
                .font(.largeTitle)
                .foregroundColor(.primary)
                .fontWeight(.bold)
                .fontDesign(.rounded)
                VStack(spacing: 4) {
                    if transaction.confirmationTime == nil {
                        Text("unconfirmed")
                    } else {
                        VStack {
                            if let timestamp = transaction.confirmationTime?.timestamp {
                                Text(
                                    timestamp.toDate().formatted(
                                        date: .abbreviated,
                                        time: Date.FormatStyle.TimeStyle.shortened
                                    )
                                )
                            }
                        }
                    }
                    if let fee = transaction.fee {
                        Text("\(fee) sats fee")
                    }
                }
                .foregroundColor(.secondary)
                .font(.callout)
            }

            Spacer()

            HStack {
//                if viewModel.network != Network.regtest.description {
//                    Button {
//                        if let esploraURL = viewModel.esploraURL {
//                            let urlString = "\(esploraURL)/tx/\(transaction.txid)"
//                                .replacingOccurrences(of: "/api", with: "")
//                            if let url = URL(string: urlString) {
//                                UIApplication.shared.open(url)
//                            }
//                        }
//                    } label: {
//                        Image(systemName: "safari")
//                            .fontWeight(.semibold)
//                            .foregroundColor(.bitcoinOrange)
//                    }
//                    Spacer()
//                }
                Text(transaction.txid)
                    .lineLimit(1)
                    .truncationMode(.middle)
                Spacer()
                Button {
                    UIPasteboard.general.string = transaction.txid
                    isCopied = true
                    showCheckmark = true
                    DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
                        isCopied = false
                        showCheckmark = false
                    }
                } label: {
                    HStack {
                        withAnimation {
                            Image(systemName: showCheckmark ? "checkmark" : "doc.on.doc")
                        }
                    }
                    .fontWeight(.semibold)
                    .foregroundColor(.orange)
                }
            }
            .fontDesign(.monospaced)
            .font(.caption)
            .padding()
            .onAppear {
 //               viewModel.getNetwork()
 //               viewModel.getEsploraUrl()
            }

        }
        .padding()

    }
}

func handleResponse(response: FaucetCall) {
    switch response {
    case let success as FaucetCall.Success:
        // Access properties like `status` and `description_`
        print("Handling the Success: \(success.status) - \(success.description_)")
    case let error as FaucetCall.Error:
        // Note the property `description_` is used instead of `description` to avoid conflict with Swift’s default `description`
        print("Handling the Error like a professional: \(error.status) - \(error.description_)")
    case let exception as FaucetCall.ExceptionThrown:
        // Handle exception, might need to access specific properties or methods
        print("Exception is too bad: \(exception.exception)")
    default:
        print("Unknown response type")
    }
}

#Preview {
    WalletView(selectedTab: .constant (0))
        .environment(WalletViewModel())
}
