//
//  FileManager+Extensions.swift
//  PadawanWallet
//
//  Created by Rubens Machion on 02/08/25.
//

import Foundation

extension FileManager {
 
    func ensureDirectoryExists(at url: URL) throws {
        var isDir: ObjCBool = false
        if fileExists(atPath: url.path, isDirectory: &isDir) {
            if !isDir.boolValue {
                try removeItem(at: url)
            }
        }
        if !fileExists(atPath: url.path) {
            try createDirectory(at: url, withIntermediateDirectories: true, attributes: nil)
        }
    }
    
    func removeOldFlatFileIfNeeded(at directoryURL: URL) throws {
        let flatFileURL = directoryURL.appendingPathComponent(URL.walletDirectoryName)
        var isDir: ObjCBool = false
        if fileExists(atPath: flatFileURL.path, isDirectory: &isDir) {
            if !isDir.boolValue {
                try removeItem(at: flatFileURL)
            }
        }
    }
}
