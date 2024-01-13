//
//  ReceiveView.swift
//  iosApp
//
//  Created by geigerzaehler on 2023-12-18.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import CoreImage.CIFilterBuiltins
import BitcoinDevKit

let context = CIContext()
let filter = CIFilter.qrCodeGenerator()

struct ReceiveView: View {
    
    @EnvironmentObject var viewModel: WalletViewModel
    
    @State private var address: String = "bc1qu5ujlp9dkvtgl98jakvw9ggj9uwyk79qhvwvrg"
    
    func splitAddress(address: String) -> (String, String) {
        let length = address.count
        
        return (String(address.prefix(length / 2)), String(address.suffix(length / 2)))
    }
    
    func getAddress() {
        switch viewModel.state {
            case .loaded(let wallet, _):
                do {
                    let addressInfo = try wallet.getAddress(addressIndex: AddressIndex.new)
                    address = addressInfo.address.asString()
                } catch {
                    address = "ERROR"
                }
            default: do { }
        }
    }
    
    func generateQRCode(from string: String) -> UIImage {
        let data = Data(string.utf8)
        filter.setValue(data, forKey: "inputMessage")

        if let outputImage = filter.outputImage {
            if let QRImage = context.createCGImage(outputImage, from: outputImage.extent) {
                return UIImage(cgImage: QRImage)
            }
        }

        return UIImage(systemName: "xmark.circle") ?? UIImage()
    }
    
    var body: some View {
        
        VStack {
            
            Spacer()
            
            VStack {
                
                Image(uiImage: generateQRCode(from: "bitcoin:\(address)"))
                    .interpolation(.none)
                    .resizable()
                    .scaledToFit()
                    .frame(width: 300, height: 300)
                
                Spacer()
                
                Text(splitAddress(address: address).0)
                    //.textStyle(BasicTextStyle(white: true))
                    .font(.title)
                
                Text(splitAddress(address:  address).1)
                    //.textStyle(BasicTextStyle(white: true))
                    .font(.title)
                    .onTapGesture(count: 1) {
                        UIPasteboard.general.string = address
                    }
                
                Spacer()
            }
            .contextMenu {
                
                Button(action: {
                    UIPasteboard.general.string = address}) {
                        Text("Copy to clipboard")
                    }
            }
            Spacer()
            
            Button(action: getAddress) {
                Text("Done")
                    //.font(.system(size: 16, design: .monospaced))
                    .font(.title)
                    .fontWeight(.bold)
                    .frame(maxWidth: .infinity, maxHeight: 40)
                    .foregroundColor(Color.white)
                    .padding(10)
                    .background(Color.orange)
                    .cornerRadius(20)
                    //.shadow(color: Color("Shadow"), radius: 1, x: 5, y: 5)
            }
            .padding(40)
            
            Spacer()
        }
        .navigationTitle("Receive Bitcoin")
        .padding(10)
        //.modifier(BackButtonMod())
        .onAppear(perform: {
            getAddress()
            viewModel.sync()
        })
    }
}

struct ReceiveView_Previews: PreviewProvider {
    static var previews: some View {
        ReceiveView()
            .environmentObject(WalletViewModel())
    }
}
