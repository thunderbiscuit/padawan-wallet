import SwiftUI
//import SharedPadawan

struct ContentView: View {
    
    // MARK: PROPERTIES
    @State var selectedTab: Int = 0
//	let greet = Greeting().greet()

    enum Tab: Int {
        case firstTab = 0, secondTab, thirdTab, fourthTab
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
            
            LearningView()
                .font(.largeTitle)
                .foregroundColor(.blue)
                .tabItem {
                    Image(systemName: "graduationcap.fill")
                    Text("Learn")
                }
                .tag(Tab.secondTab.rawValue)
            
            MenuView()
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


struct WalletView: View {
    
    @EnvironmentObject var viewModel: WalletViewModel
    @Binding var selectedTab: Int
    
    var body: some View {
        ZStack {
            //Color.red.ignoresSafeArea()
            VStack {
                
                Image("Logo")
                Spacer()
                
                
                Text("Block Height")
                    .font(.largeTitle)
                    .foregroundColor(.blue)
                
                Text("\(String(viewModel.blockHeight))")
                    //.font(.largeTitle)
                    .font(.system(size: 60))
                    .foregroundColor(.blue)
                

                Spacer()
                
            }
        }
        .onAppear(perform: viewModel.load)
    }
}
struct LearningView: View {
    
    @State var isSelected: Bool = false
    
    var body: some View {
        
        VStack(spacing: 40) {
            
            Text("Learning Tab")
            
            Spacer()
        }
        .padding(40)
        
    }
}

struct MenuView: View {
    
    @State var isSelected: Bool = false
    
    var body: some View {
        
        VStack(spacing: 40) {
            
            RoundedRectangle(cornerRadius: 25)
                .frame(height: 200)
                .foregroundColor(isSelected ? Color.green : Color.red)
            
            Button(action: {
                isSelected.toggle()
            }, label: {
                Text("Button")
                    .font(.headline)
                    .foregroundColor(.white)
                    .frame(height: 55)
                    .frame(maxWidth: .infinity)
                    .background(Color.blue)
                    .cornerRadius(25)
            })
            
            Text("Double TAP Gesture")
                .font(.headline)
                .foregroundColor(.white)
                .frame(height: 55)
                .frame(maxWidth: .infinity)
                .background(Color.blue)
                .cornerRadius(25)
//                .onTapGesture {
//                    isSelected.toggle()
//                }
                .onTapGesture(count: 2, perform: {
                    isSelected.toggle()
                })
            
            Text("Menu Item 1")
            Text("Menu Item 2")
            
            Spacer()
        }
        .padding(40)
        
    }
}

// MARK: PREVIEW

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
            .environmentObject(WalletViewModel())
	}
}
