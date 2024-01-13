//
//  ContentView.swift
//  iosApp
//
//  Created by geigerzaehler on 2023-12-07.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
//import SharedPadawan

struct ContentView: View {
    
// MARK: PROPERTIES
    
   // @EnvironmentObject var viewModel: WalletViewModel
    @State var selectedTab: Int = 0
//	let greet = Greeting().greet()

    enum Tab: Int {
        case firstTab = 0, secondTab, thirdTab
    }
    
    @State public var firstTimeRunning: Bool = true
    
// MARK: BODY
	var body: some View {
        
        Group {
                    
            //if viewModel.firstTimeRunning {
                
            //viewModel.firstTimeRunningToggle()

            TabView(selection: $selectedTab) {
                            
                WalletView(selectedTab: $selectedTab)
                    .tabItem {
                        Image(systemName: "bitcoinsign.square.fill")
                        Text("Wallet")
                    }
                    .tag(Tab.firstTab.rawValue)
                
                LearnView(selectedTab: $selectedTab)
                    .tabItem {
                        Image(systemName: "graduationcap.fill")
                        Text("Learn")
                    }
                    .tag(Tab.secondTab.rawValue)
                
                MenuView(selectedTab: $selectedTab)
                    .font(.largeTitle)
                    .foregroundColor(.blue)
                    .tabItem{
                        Image(systemName: "text.justify.trailing")
                        Text("Menu")
                    }
                    .tag(Tab.thirdTab.rawValue)
            }
            .fullScreenCover(isPresented: $firstTimeRunning, content: {
                WelcomeView()
            })
        }
	}
}

// MARK: PREVIEW

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
            .environmentObject(WalletViewModel())
	}
}
