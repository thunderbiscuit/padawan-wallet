/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import SwiftUI
import CoreImage.CIFilterBuiltins

struct ReceiveScreen: View {
    @State private var address: String = "tb1q rm8g 8wzk v5cs qc56 fxsm 0jhw jupn 7s5k nng7 86"
    @Environment(\.padawanColors) private var colors
    private let context = CIContext()
    private let filter = CIFilter.qrCodeGenerator()

    var body: some View {
        VStack(spacing: 24) {
            // Title
            Text("Receive bitcoin")
                .font(.title2)
                .bold()
                .foregroundColor(colors.text)
                .frame(maxWidth: .infinity, alignment: .center)

            // QR code
            if let qrImage = generateQRCode(from: address.replacingOccurrences(of: " ", with: "")) {
                Image(uiImage: qrImage)
                    .interpolation(.none)
                    .resizable()
                    .scaledToFit()
                    .frame(width: 240, height: 240)
                    .padding()
                    .background(colors.background2)
                    .cornerRadius(20)
            }

            // Address + Copy button
            HStack {
                Text(address)
                    .font(.system(.body, design: .monospaced))
                    .foregroundColor(colors.text)
                    .lineLimit(2)
                    .minimumScaleFactor(0.5)
                    .multilineTextAlignment(.center)
                Button(action: {
                    UIPasteboard.general.string = address.replacingOccurrences(of: " ", with: "")
                }) {
                    Image(systemName: "doc.on.doc")
                        .foregroundColor(colors.accent1)
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
                    .foregroundColor(colors.text)
                    .frame(maxWidth: .infinity)
                    .padding()
                    .background(colors.accent2)
                    .cornerRadius(12)
                    .shadow(color: colors.darkBackground.opacity(0.25), radius: 2, x: 4, y: 4)
            }

        }
        .padding()
        .background(colors.background)
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
