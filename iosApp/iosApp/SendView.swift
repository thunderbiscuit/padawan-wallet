//
//  SendView.swift
//  iosApp
//
//  Created by geigerzaehler on 2023-12-20.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct SendView: View {
    @State private var feesSatsPerVByte = 50.0
    @State private var isEditing = false
    @State private var satsAmount: String = ""
    @State private var btcAddress: String = ""
    @State private var satsBalance: String = "0"
    
    var body: some View {
        
        VStack{
            HStack{
                Text("Amount")
                Spacer()
                Text("Balance ")
                Text(satsBalance)
                Text(" sats")
            }
            
            VStack(alignment: .leading) {
                TextField("Enter amount sats", text: $satsAmount)
            }
            
            HStack{
                Text("Address")
                Spacer()
            }
            
            VStack(alignment: .leading) {
                TextField("Enter a bitcoin testnet address", text: $btcAddress)
            }
           
            Spacer()
            HStack{
                Text("Fees (sats/vbytes)")
                Spacer()
            }
            
            VStack {
                Slider(
                    value: $feesSatsPerVByte,
                    in: 0...100,
                    step: 1) {
                        Text("Speed")
                    } minimumValueLabel: {
                        Text("0")
                    } maximumValueLabel: {
                        Text("100")
                    } onEditingChanged: { editing in
                        isEditing = editing
                    }
                    Text("\(feesSatsPerVByte)")
                        .foregroundColor(isEditing ? .red : .blue)
            }
            .navigationTitle("Send Bitcoin")
            
        }.padding(40)
        
        Spacer()
        
        Button(action: {
            print("hello world")
        }, label: {
            Text("Verify Transaction")
                .font(.headline)
                .foregroundColor(.white)
                .frame(height: 55)
                .frame(maxWidth: .infinity)
                .background(Color.orange)
                .cornerRadius(20)
        })
        .padding(40)
    }
}

#Preview {
    SendView()
}
