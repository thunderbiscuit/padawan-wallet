//
//  MenuView.swift
//  iosApp
//
//  Created by geigerzaehler on 2023-12-07.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct MenuView: View {
    
    @EnvironmentObject var viewModel: WalletViewModel
    @Binding var selectedTab: Int
    
        @State var isSelected: Bool = false
    
        var body: some View {
    
            VStack(spacing: 40) {
    
                RoundedRectangle(cornerRadius: 20)
                    .frame(height: 200)
                    .foregroundColor(isSelected ? Color.red : Color.purple)
    
                HStack() {
    
                    Button(action: {
                        isSelected.toggle()
                    }, label: {
                        Text("Receive ↓")
                            .font(.headline)
                            .foregroundColor(.white)
                            .frame(height: 55)
                            .frame(maxWidth: .infinity)
                            .background(Color.teal)
                            .cornerRadius(20)
                    })
                    //.fullScreenCover(isPresented: $isSelected, content: {
                    //    ReceiveView(isSelected: $isSelected)
                    //})
    
                    Button(action: {
                        isSelected.toggle()
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
    
                Text("Double TAP Gesture")
                    .font(.headline)
                    .foregroundColor(.white)
                    .frame(height: 55)
                    .frame(maxWidth: .infinity)
                    .background(Color.blue)
                    .cornerRadius(20)
    //                .onTapGesture {
    //                    isSelected.toggle()
    //                }
                    .onTapGesture(count: 2, perform: {
                        isSelected.toggle()
                    })
    
                Spacer()
            }
            .padding(40)
        }
    

}

#Preview {
    MenuView(selectedTab: .constant (0))
        .environmentObject(WalletViewModel())
}
