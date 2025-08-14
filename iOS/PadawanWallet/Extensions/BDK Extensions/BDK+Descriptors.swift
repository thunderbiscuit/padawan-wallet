//
//  BDK+Descriptors.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 02/08/25.
//
import BitcoinDevKit

extension Descriptor {
    
    static func createDescriptors(
        secretKey: DescriptorSecretKey,
        network: Network
    ) -> (descriptor: Descriptor, changeDescriptor: Descriptor) {
        
        let descriptor = Descriptor.newBip84(
            secretKey: secretKey,
            keychainKind: .external,
            network: network
        )
        let changeDescriptor = Descriptor.newBip84(
            secretKey: secretKey,
            keychainKind: .internal,
            network: network
        )
        return (descriptor, changeDescriptor)
    }
    
    static func createPublicDescriptors(
        publicKey: DescriptorPublicKey,
        fingerprint: String,
        network: Network
    ) -> (descriptor: Descriptor, changeDescriptor: Descriptor) {
        let descriptor = Descriptor.newBip84Public(
            publicKey: publicKey,
            fingerprint: fingerprint,
            keychainKind: .external,
            network: network
        )
        let changeDescriptor = Descriptor.newBip84Public(
            publicKey: publicKey,
            fingerprint: fingerprint,
            keychainKind: .internal,
            network: network
        )
        return (descriptor, changeDescriptor)
    }
}
