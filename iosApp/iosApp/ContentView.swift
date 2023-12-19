import SwiftUI
//import SharedPadawan

struct ContentView: View {
    
// MARK: PROPERTIES
    @State var selectedTab: Int = 0
//	let greet = Greeting().greet()

    enum Tab: Int {
        case firstTab = 0, secondTab, thirdTab
    }
    
// MARK: BODY
	var body: some View {

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
	}
}

// MARK: PREVIEW

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
            .environmentObject(WalletViewModel())
	}
}
