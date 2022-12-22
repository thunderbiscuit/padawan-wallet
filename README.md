<div align="center" >
  <h1>Padawan Wallet</h1>
  <br/>
  <br/>
  <img src="./images/logo-v1.0.0.svg" alt="Logo 1.0.0" width="400">
</div>
<br/>
<br/>

[![GitHub](https://img.shields.io/github/license/thunderbiscuit/padawan-wallet?color=brightgreen)](https://github.com/thunderbiscuit/padawan-wallet/blob/master/LICENSE) 
![Kotlin Version](https://img.shields.io/badge/kotlin-v1.4.21-orange) 
[![Conventional Commits](https://img.shields.io/badge/conventional%20commits-1.0.0-yellow.svg)](https://conventionalcommits.org) 
[![GitHub release (latest by date)](https://img.shields.io/github/v/release/thunderbiscuit/padawan-wallet)](https://github.com/thunderbiscuit/padawan-wallet/releases) 
[![Support Server](https://img.shields.io/badge/discord-chat-7289da?label=Discord&logo=Discord&colorB=7289da&style=flat)](https://discord.gg/hbMszDMP3X) 
[![Twitter](https://img.shields.io/badge/twitter-%40padawanwallet-b8bb26)](https://twitter.com/padawanwallet)

A testnet-only bitcoin wallet full of tutorials on how to use bitcoin wallets.

We're building the app you'll want to recommend to your teenage cousins at Christmas or to your dad that keeps asking questions about bitcoin. It aims to be a self-study tool, getting its users acquainted with the usual workflow and basic jargon of mobile wallets in a risk-free environment perfect for experimentation and learning (testnet).

We think testnet is an underused resource outside of software development circles, and believe it can be leveraged for bitcoin-curious people everywhere. Testnet offers all the complexity of mainnet, and one of the goal of this wallet is to eventually foray into these more advanced bitcoin features (output descriptors, multisig wallets, DLCs) and offer a training and testing ground for users.

Join us on [discord](https://discord.gg/hbMszDMP3X)!  
<br/>

## Download
You can download the latest apk for this app on the [`v0.8.0` release page](https://github.com/thunderbiscuit/padawan-wallet/releases/tag/v0.8.0).  
<br/>

## Screenshots
<div align="center" >
  <img src="./images/screenshots-intro.png" alt="Padawan Screenshot Intro" width="700">
  <img src="./images/screenshots-home.png" alt="Padawan Screenshot Home" width="700">
  <img src="./images/padawan-wallet-navigation.png" alt="Padawan Wallet Navigation" width="700">
</div>
<br />

## FAQ
### 🧐 Tutorials you say?
The tutorials are broken down in two groups: _Essentials_ and _Advanced_, each of which contain _Concepts to understand_ and _Skills to master_. The currently planned tutorials are the following:

**Essentials**
1. What is the bitcoin testnet? (concept)  
2. Bitcoin units (concept)
3. Receiving bitcoin (skill)
4. What is the mempool? (concept)
5. Sending bitcoin (skill)
6. What are transaction fees? (concept)
7. What is a wallet recovery phrase? (concept)
8. Recovering a wallet from a recovery phrase (skill)

### 🧐 Where can I get testnet coins?
There are many bitcoin testnet faucets out there, but Padawan uses native segwit addresses uniquely (bech32), so you'll need a faucet that can send to those. We suggest [this one](https://testnet-faucet.mempool.co/).

### How can I delete my wallet?
You cannot delete your wallet directly from within the app. This is on purpose, to make sure people don't delete wallets without putting in some work. If you are certain that your wallet is empty (if it's not, send the testnet coins back to us!), you can delete the wallet by clearing all app data for Padawan in your Android settings, or you can uninstall and reinstall the app directly from the Play Store. 

### Building and running Padawan
To build and run the app from source, you'll need:
- Android Studio
- A phone with Android 6 OS or above (Android Marshmallow, API level 23) with USB debugging activated OR an emulator on your development machine
- The bitcoindevkit library

#### **Bitcoindevkit library**
The bitcoindevkit library for Android (`bdk-jni`) is not yet available on public repositories of Android libraries. This means that in order to acquire it, one must build it from source.

To build it from source, head to the [bdk-jni library repository](https://github.com/bitcoindevkit/bdk-jni) and follow the instructions to build and publish to your local Maven repository.

Once you have the library available locally, you can build and run the app just like any other Android application!

### 🧐 How can I contribute?
If you think this project is interesting and would like to contribute, get access to the early release on the app store, or simply provide feedback and bounce ideas, check out [our Discord server](https://discord.gg/hbMszDMP3X). Users and devs welcome.
