//
//  CameraScannView.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 05/09/25.
//

import AVFoundation
import CodeScanner
import SwiftUI

struct CameraScannView: View {
    @Environment(\.dismiss) private var dismiss
    @EnvironmentObject private var languageManager: LanguageManager
    @State private var fullScreenMode: SendTransactionScreenNavigation?
    private let codeTypes: [AVMetadataObject.ObjectType] = [
        .qr
    ]
    private let actionRecognized: (String) -> Void
    
    init(actionRecognized: @escaping (String) -> Void) {
        self.actionRecognized = actionRecognized
    }
    
    var body: some View {
        BackgroundView {
            CodeScannerView(
                codeTypes: codeTypes,
                completion: codeScannerHandler
            )
            .frame(maxWidth: .infinity, maxHeight: .infinity)
        
            VStack {
                Spacer()
                PadawanButton(title: languageManager.localizedString(forKey: "cancel")) {
                    dismiss()
                }
                .frame(width: 150)
                .fixedSize(horizontal: false, vertical: true)
            }
            .padding(.bottom, 90)
        }
        .ignoresSafeArea()
        .fullScreenCover(item: $fullScreenMode) { item in
            switch item {
            case .alert(let data):
                AlertModalView(data: data)
                    .background(BackgroundClearView())
                
            default:
                EmptyView()
            }
        }
    }
    
    // MARK: - Private
    
    private func codeScannerHandler(_ result: Result<ScanResult, ScanError>) {
        switch result {
        case .success(let response):
            let scannedAddress = response.string.lowercased().replacingOccurrences(
                of: "bitcoin:",
                with: ""
            )
            actionRecognized(scannedAddress)
            dismiss()
        case .failure(let failure):
            fullScreenMode = .alert(
                    data: .init(
                    titleKey: "generic_title_error",
                    subtitle: failure.localizedDescription
                )
            )
        }
    }
}
