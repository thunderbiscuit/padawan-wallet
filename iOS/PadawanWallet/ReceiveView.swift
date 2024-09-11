//
//  ReceiveView.swift
//  PadawanWallet
//
//  Copyright 2024 thunderbiscuit, geigerzaehler, and contributors.
//  Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE.txt file.
//

import SwiftUI
import CoreImage.CIFilterBuiltins
import BitcoinDevKit

let context = CIContext()
let filter = CIFilter.qrCodeGenerator()

struct ReceiveView: View {
    
    @Environment(WalletViewModel.self) private var walletViewModel
    
    @Binding var navigationPath: [String]
    
    @State private var address: String = "bc1qu5ujlp9dkvtgl98jakvw9ggj9uwyk79qhvwvrg"
    
    func splitAddress(address: String) -> (String, String) {
        let length = address.count
        
        return (String(address.prefix(length / 2)), String(address.suffix(length / 2)))
    }
    
    func getAddress() {
        switch walletViewModel.state {
            case .loaded(let wallet, _):
                do {
                    let addressInfo = try wallet.getAddress(addressIndex: AddressIndex.lastUnused)
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
                        Text("copy_to_clipboard_image")
                    }
            }
            Spacer()
            
            Button(action: {
                getAddress()
                navigationPath.removeAll() //return to calling view
            }) {
                Text("back_to_wallet")
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
        .navigationTitle("receive_bitcoin")
        .padding(10)
        //.modifier(BackButtonMod())
        .onAppear(perform: {
            getAddress()
            walletViewModel.sync()
        })
    }
}

struct ReceiveView_Previews: PreviewProvider {
    static var previews: some View {
        ReceiveView(navigationPath: .constant (["receive"]))
            .environment(WalletViewModel())
    }
}
