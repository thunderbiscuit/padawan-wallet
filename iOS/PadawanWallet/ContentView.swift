//
//  ContentView.swift
//  PadawanWallet
//
//  Copyright 2024 thunderbiscuit, geigerzaehler, and contributors.
//  Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE.txt file.
//

import SwiftUI

struct ContentView: View {
    
// MARK: PROPERTIES
    
    @Environment(WalletViewModel.self) private var walletViewModel
    @State var selectedTab: Int = 0

    enum Tab: Int {
        case firstTab = 0, secondTab, thirdTab
    }
    
// MARK: BODY
    var body: some View {
        
        Group {

            TabView(selection: $selectedTab) {
                            
                WalletView(selectedTab: $selectedTab)
                    .tabItem {
                        Image(systemName: "bitcoinsign.square.fill")
                        Text("bottom_nav_wallet")
                    }
                    .tag(Tab.firstTab.rawValue)
                
                LearnView(selectedTab: $selectedTab)
                    .tabItem {
                        Image(systemName: "graduationcap.fill")
                        Text("bottom_nav_chapters")
                    }
                    .tag(Tab.secondTab.rawValue)
                
                MenuView(selectedTab: $selectedTab)
                    .font(.largeTitle)
                    .foregroundColor(.blue)
                    .tabItem{
                        Image(systemName: "text.justify.trailing")
                        Text("bottom_nav_settings")
                    }
                    .tag(Tab.thirdTab.rawValue)
            }
//            .fullScreenCover(isPresented: $firstTimeRunning, content: {
//                WelcomeView()
//            })
        }
        .onAppear{
            walletViewModel.load()
        }
    }
}

// MARK: PREVIEW

struct ContentView_Previews: PreviewProvider {
    
    static var previews: some View {
        ContentView()
            .environment(WalletViewModel())
    }
}
