//
//  BDKError+Extensions.swift
//  PadawanWallet
//
//  Copyright 2024 thunderbiscuit, geigerzaehler, and contributors.
//  Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE.txt file.
//

import BitcoinDevKit
import Foundation

extension BdkError {
    var description: String {
        switch self {
        case .InvalidU32Bytes(let message),
            .Generic(let message),
            .MissingCachedScripts(let message),
            .ScriptDoesntHaveAddressForm(let message),
            .NoRecipients(let message),
            .NoUtxosSelected(let message),
            .OutputBelowDustLimit(let message),
            .InsufficientFunds(let message),
            .BnBTotalTriesExceeded(let message),
            .BnBNoExactMatch(let message),
            .UnknownUtxo(let message),
            .TransactionNotFound(let message),
            .TransactionConfirmed(let message),
            .IrreplaceableTransaction(let message),
            .FeeRateTooLow(let message),
            .FeeTooLow(let message),
            .FeeRateUnavailable(let message),
            .MissingKeyOrigin(let message),
            .Key(let message),
            .ChecksumMismatch(let message),
            .SpendingPolicyRequired(let message),
            .InvalidPolicyPathError(let message),
            .Signer(let message),
            .InvalidNetwork(let message),
            .InvalidProgressValue(let message),
            .ProgressUpdateError(let message),
            .InvalidOutpoint(let message),
            .Descriptor(let message),
            .Encode(let message),
            .Miniscript(let message),
            .MiniscriptPsbt(let message),
            .Bip32(let message),
            .Secp256k1(let message),
            .Json(let message),
            .Hex(let message),
            .Psbt(let message),
            .PsbtParse(let message),
            .Electrum(let message),
            .Esplora(let message),
            .Sled(let message),
            .Rusqlite(let message),
            .HardenedIndex(let message),
            .Rpc(let message):
            return message
        }
    }
}

extension TransactionDetails: Comparable {
    public static func == (lhs: BitcoinDevKit.TransactionDetails, rhs: BitcoinDevKit.TransactionDetails) -> Bool {
        
        let lhs_timestamp: UInt64 = lhs.confirmationTime?.timestamp ?? UInt64.max;
        let rhs_timestamp: UInt64 = rhs.confirmationTime?.timestamp ?? UInt64.max;
       
        return lhs_timestamp == rhs_timestamp
    }
    
    public static func < (lhs: TransactionDetails, rhs: TransactionDetails) -> Bool {
        
        let lhs_timestamp: UInt64 = lhs.confirmationTime?.timestamp ?? UInt64.max;
        let rhs_timestamp: UInt64 = rhs.confirmationTime?.timestamp ?? UInt64.max;
        
        return lhs_timestamp < rhs_timestamp
    }
}
