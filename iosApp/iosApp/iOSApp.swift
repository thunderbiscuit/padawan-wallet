import SwiftUI

@main
struct iOSApp: App {
    
    @AppStorage("isOnboarding") var isOnboarding: Bool = true
    
	var body: some Scene {
		WindowGroup {
            
            if isOnboarding {
                WelcomeView()
                .environmentObject(WalletViewModel())
            } else {
                ContentView()
                .environmentObject(WalletViewModel())
            }
            
			
		}
	}
}
