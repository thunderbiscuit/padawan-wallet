//
//  ReceiveScreen.swift
//  PadawanWallet
//
//  Created by thunderbiscuit on 2025-06-04.
//

import SwiftUI
import CoreImage.CIFilterBuiltins

struct ReceiveScreen: View {
    @State private var address: String = "tb1q rm8g 8wzk v5cs qc56 fxsm 0jhw jupn 7s5k nng7 86"
    private let context = CIContext()
    private let filter = CIFilter.qrCodeGenerator()

    var body: some View {
        VStack(spacing: 24) {
            // Title
            Text("Receive bitcoin")
                .font(.title2)
                .bold()
                .frame(maxWidth: .infinity, alignment: .center)

            // QR code
            if let qrImage = generateQRCode(from: address.replacingOccurrences(of: " ", with: "")) {
                Image(uiImage: qrImage)
                    .interpolation(.none)
                    .resizable()
                    .scaledToFit()
                    .frame(width: 240, height: 240)
                    .padding()
                    .background(Color(.systemGray6))
                    .cornerRadius(20)
            }

            // Address + Copy button
            HStack {
                Text(address)
                    .font(.system(.body, design: .monospaced))
                    .lineLimit(2)
                    .minimumScaleFactor(0.5)
                    .multilineTextAlignment(.center)
                Button(action: {
                    UIPasteboard.general.string = address.replacingOccurrences(of: " ", with: "")
                }) {
                    Image(systemName: "doc.on.doc")
                        .foregroundColor(.black)
                }
            }
            .padding(.horizontal)

            Spacer()

            // Generate new address button
            Button(action: {
                // Replace with real address generation
                address = generateMockAddress()
            }) {
                Text("Generate a new address")
                    .foregroundColor(.black)
                    .frame(maxWidth: .infinity)
                    .padding()
                    .background(Color.orange)
                    .cornerRadius(12)
                    .shadow(color: .black.opacity(0.25), radius: 2, x: 4, y: 4)
            }

        }
        .padding()
        .navigationTitle("")
        .navigationBarTitleDisplayMode(.inline)
    }

    // MARK: - QR Code Generation
    func generateQRCode(from string: String) -> UIImage? {
        filter.message = Data(string.utf8)
        if let outputImage = filter.outputImage,
           let cgimg = context.createCGImage(outputImage.transformed(by: CGAffineTransform(scaleX: 10, y: 10)), from: outputImage.extent) {
            return UIImage(cgImage: cgimg)
        }
        return nil
    }

    func generateMockAddress() -> String {
        // Replace with real address generation logic
        return "tb1q v8cz 7l3g 2f9y 3gqx g3cd lue4 x5a9 wnl0 tsc5 kr"
    }
}
