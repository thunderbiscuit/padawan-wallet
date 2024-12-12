//
//  PadawanBdkWrapper.swift
//  PadawanBdkWrapper
//
//  Created by thunderbiscuit on 2024-12-12.
//

import Foundation
import BitcoinDevKit

@objc public class PadawanBdkWrapper: NSObject {
    private var innerWallet: Wallet
    
    @objc public override init() {
        let connection = try! Connection.newInMemory()
        self.innerWallet = try! Wallet(
            descriptor: descriptor,
            changeDescriptor: changeDescriptor,
            network: Network.signet,
            connection: connection
        )
    }

    @objc public func newAddress() -> NSString {
        let address = innerWallet.revealNextAddress(keychain: KeychainKind.external).address.description
        return NSString(string: address)
    }
}

private let descriptor = try! Descriptor(
    descriptor: "wpkh(tprv8ZgxMBicQKsPf2qfrEygW6fdYseJDDrVnDv26PH5BHdvSuG6ecCbHqLVof9yZcMoM31z9ur3tTYbSnr1WBqbGX97CbXcmp5H6qeMpyvx35B/84h/1h/0h/2/*)",
    network: Network.signet
)
private let changeDescriptor = try! Descriptor(
    descriptor: "wpkh(tprv8ZgxMBicQKsPf2qfrEygW6fdYseJDDrVnDv26PH5BHdvSuG6ecCbHqLVof9yZcMoM31z9ur3tTYbSnr1WBqbGX97CbXcmp5H6qeMpyvx35B/84h/1h/0h/3/*)",
    network: Network.signet
)
