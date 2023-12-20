//
//  WalletView.swift
//  iosApp
//
//  Created by geigerzaehler on 2023-12-07.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct WalletView: View {
    
    @EnvironmentObject var viewModel: WalletViewModel
    @Binding var selectedTab: Int
   
    @State var navigationPath: [String] = []
    
    var body: some View {

        NavigationStack(path: $navigationPath) {
            
            VStack(spacing: 40) {
                
                RoundedRectangle(cornerRadius: 20)
                    .frame(height: 200)
                    .foregroundColor( Color.purple)
                
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
                .navigationDestination(for: String.self) { value in
                    
                    switch value {
                    case "Receive ↓":
                        ReceiveView()
                    case "Send ↑":
                        SendView()
                    default:
                        SendView()
                    }
                }
                
                HStack() {
                    Text("Transactions")
                        .font(.headline)
                    Spacer()
                }
                
                RoundedRectangle(cornerRadius: 20)
                    .frame(height: 200)
                    .foregroundColor(Color.yellow)
                
                Spacer()
            }
            .padding(40)
        }
    }
}

#Preview {
    WalletView(selectedTab: .constant (0))
        .environmentObject(WalletViewModel())
}
