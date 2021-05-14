# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).  
<br/>

## [v0.6.0](https://github.com/thunderbiscuit/padawan-wallet/releases/tag/v0.6.0) — May 14, 2021
### Added
+ Allow new users to request testnet coins to Tatooine faucet
+ Add database to store transactions
+ Display transaction history in recyclerview
+ Use camera to scan QR code formatted bitcoin addresses
+ Add app version number to drawer  

### Changed
+ Update bitcoindevkit library to 0.2.0
+ Upload new logo `v1.0.0`  
+ Bump Kotlin version to `1.5.0`
+ Better handling of network and API failures
+ Clean up UI for larger devices
+ Tons of small UI improvements
+ Streamline use of snackbars across the app  
<br/>  

## [v0.5.0](https://github.com/thunderbiscuit/padawan-wallet/releases/tag/v0.5.0) — Apr 7, 2021
### Added
+ Add QR codes when displaying receive addresses
+ Shared preferences keep track of completed tutorials
+ All 8 essential tutorials
+ Added Discord community to readme
+ Add twitter badge
+ Prevent app from going into landscape mode
+ Add GitHub Actions basic CI workflow
+ Add splash screen

### Changed
+ Better readme and docs
+ Move gradle build files to Kotlin DSL
+ Use Repository architecture element
+ Move the bitcoindevkit to Wallet Object
+ Point bdk to blockstream.info servers  
<br/>  

## [v0.4.0](https://github.com/thunderbiscuit/padawan-wallet/releases/tag/v0.4.0) — Jan 12, 2021
### Added
+ Add tutorial fragments UI
+ Enable marking tutorials as done
+ Make tutorials list scrollable

### Changed
+ Various improvements to the codebase  
<br/>  

## [v0.3.0](https://github.com/thunderbiscuit/padawan-wallet/releases/tag/v0.3.0) — Jan 5, 2021
### Added
+ Tutorial tab now handles fragments
+ Separate wallet and tutorials in their respective packages
+ Add tutorial navigation and draft UI
+ Add license and tags badges to readme
+ Add draft launcher icon
+ Display toggle for wallet balance in either satoshi or bitcoin
+ Added changelog
+ Set official application ID
+ Streamline use of snackbars
+ Perform basic checks on inputs for transaction building fragment

### Changed
+ Use Material alert dialog
+ Remove use of theme background colour
+ Do not generate new address by default upon navigation to receive fragment  
<br/>

## [v0.2.0](https://github.com/thunderbiscuit/padawan-wallet/releases/tag/v0.2.0) — Jan 3, 2020
### Added
+ Wallet sync functionality
+ Wallet can display wallet current balance upon sync
+ Receive fragment that can generate new addresses
+ Send fragment that can create transactions
+ Verify + Broadcast fragment that can broadcast transactions  
<br/>

## [v0.1.0](https://github.com/thunderbiscuit/padawan-wallet/releases/tag/v0.1.0) — Dec 20, 2020
### Added
+ Draft UI for wallet generation
+ Draft UI for home activity (wallet and tutorials)
+ Drawer activity (settings, about, etc.)
+ Navigation drawer
+ Option to display wallet backup seed
+ Wallet is BIP84 compatible
+ Wallet recovery funtionality
