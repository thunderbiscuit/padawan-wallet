# Readme
This file is a 1-to-1 mapping of the tutorials in the app. It is meant as an easy way for developers to visualize and consume what is in the tutorials and open PRs for bettering them. 

If you are not comfortable opening a PR but have ideas on how to improve the tutorials, please propose changes through a GitHub issue, or join our [Discord channel] and come discuss your ideas there!

The tutorials are currently capped at an arbitrary 5000 characters per. This is not a technical requirement of the app but simply a way to keep the tutorials light and quickly digestible. Please consider the cap when opening pull requests.  
<br/>

# Tutorial 1: What is the Bitcoin testnet? — Concept
You can think of the Bitcoin network as thousands of computers who speak a common language and communicate directly with each other over the internet. Even though the biggest and most well known of these computer networks is the one you're most familiar with and the one most people speak of when they say "the Bitcoin network", there are in fact a few different types of Bitcoin networks one might use.

## Mainnet
The most well known of those is called _mainnet_, and refers to the bitcoins that have monetary value and can be traded on exchanges. The other networks are built for testing and training, and are used by software developers who need to develop and test their software, and by people who wish to learn how to use Bitcoin without the worry of protecting and sending around "real" (mainnet) bitcoins.

### Testnet
The testnet consists of a full-fledged bitcoin network that is available worldwide, and connects thousands of nodes (just like mainnet). The bitcoins on this network are called _testnet coins_, and look and behave most identically to normal bitcoin, except that they have no value! This feature is useful for testing applications in the real world. It also allows curious minds to explore bitcoin in a real way, make transactions, back up wallets, and understand the foundational building blocks of the network without having to touch real bitcoin before they get comfortable with the basic set of heuristics that bitcoin requires users to understand in order to use safely and effectively.

### Padawan
Padawan uses testnet _uniquely_. You cannot receive or send normal (mainnet) bitcoins to/from Padawan, and this is a feature! You can rely on Padawan never holding anything of great value other than the knowledge and experience you build with it. Any coins you have on balance or send to friends are testnet coins.
<br/>

# Tutorials 2: Bitcoin units — Concept
It is unfortunate but the word "bitcoin" has two completely different meanings. We speak of _Bitcoin_ the network, the protocol, the open source project, and we also use the word "bitcoin" as one of the units of value when transacting on that network.

A bitcoin is a unit of value that can easily be broken down in smaller parts. Just like the kilometer can be broken down into meters and centimeters and the dollar can be broken down into quarters, dimes, and pennies, bitcoin can be broken into smaller units. While there are many units to describe bitcoin, only two of those are really important: the _bitcoin_ and the _satoshi_. There are one hundred million (100,000,000) satoshis in a bitcoin.

100,000,000 satoshis = 1 bitcoin  
1 satoshi = 0.00000001 bitcoin

The symbols used to represent those units of value are not always the same. There is no central authority to dictate what should be the "official" symbol. Instead this choice is a matter of emergent consensus, and will eventually solidify over time around one symbol for each unit. Here are some of the symbols for bitcoin and satoshis you might see around in applications:

[include different symbols for bitcoin and satoshi units]

## Both units are useful
Depending on the size of a transaction, it is often more appropriate to think of the amount transacted as being in bitcoin or in satoshis. A brand new car might be worth 1.7 bitcoins (1,700,000,000 sats is not as easy for the eye to quickly understand), whereas a candy might be worth 250 satoshis (0.00000250 bitcoin is harder to read).

The ability to use both units is an important skill to develop. A lot of transactions are denominated in bitcoin, but their _network fee_ (tutorial #4) is almost always written using the satoshis unit. You can switch between the two units in the main wallet display page by touching the bitcoin/satoshi symbol.

[GIF of unit switch in wallet]  
<br/>

# Tutorial 3: Receiving Bitcoin — Skill
Padawan only uses testnet bitcoins. Make sure you never send it real bitcoins!

The way to receive bitcoin is typically to ask your wallet software—in our case, Padawan—to generate what is called a _receive address_. Deep down, this address is simply a really, really big number. But to make sure it is easy to share, copy/paste, and scan, we display that address in one of two common forms; either a string of numbers and letters that looks like this one: `tb1qk5238eluqllq2wps67lkxme3x43wll4k282s8q`, or a QR code that looks like this:

[QR code of return coins address]

The two formats above actually refer to the same address.

To receive bitcoin, you would either ask the person trying to send you the coins to scan the QR code displayed on your screen when you generate an address (if the person is nearby), or you would copy the address string generated by the wallet and paste it in a message to them, preferably on a secure and encrypted chat. You can do that by touching the copy address button; The wallet will offer you to put that address on the clipboard for you.

It\'s very important that the _exact_ right address be used by the person trying to send you bitcoins. QR codes are a good way to prevent errors for that, and using the copy/paste function of your phone\'s clipboard is another way to do it. Still, you should always double check that the complete address was read/transferred to the person trying to send you bitcoin! A common way to do that is to read aloud the letters and numbers, or, for smaller transactions, to look at the first 5 to 10 characters and the last 5 to 10 characters of an address and verify that they are the same.  
<br/>

# Tutorial 4: What is the mempool? — Concept
The word _mempool_ stands for memory pool, and you can think of it as the place where transactions go before they get mined and become "official". When you send a transaction on the bitcoin network, your transaction get propagated across all computers that comprise the bitcoin network all over the world in about 2 to 5 seconds. But at this point your transaction is not really done, i.e. included in the blockchain. Instead, it sorts of waits in this "in-between" space where it is acknowledged by the network, but not yet made official.

We can think of this space (the mempool) as a theoretical place where transactions all come together and form a lineup, waiting for the next block to be mined to include many of them. Imagine an bus that comes by a lineup of people waiting to go to their part of town every 10 minutes or so. This bus can take only a certain number of pounds on board, and because everyone weights a different weight, the exact number of people who can fit in the bus at every run is different.

The mempool is the area where transactions wait for their turn to be mined. One special thing about this "lineup" of transactions that are all waiting to be made official (to get "mined", or included in the next bitcoin block), is that you can bid money to make sure you are up near the front of the line if you need to, or, inversely, you can save a lot of money by not bidding much if you are not worried about speed and you have time for this transaction to be mined. Eventually, (at the moment every weekend or so), the line clears and every transaction waiting in line does get through.  
<br/>

# Tutorial 5: Sending Bitcoin — Skill
We send bitcoins by paying to a bitcoin address. On mobile phones, the most common way to do that is to use your phone\'s camera to scan a QR code provided by the person or service you wish to pay. Another way is to have that person or service send you the address directly in text format, and copy/pasting that address in the address field of the transaction builder.

Once we have an address, we specify the amount of bitcoin to send (either in bitcoin units or in satoshis), and broadcast the transaction to the network!

[GIF of sending bitcoin]
<br/>

# Tutorials 6: What are transaction fees? — Concept
The action of sending a transaction over the bitcoin network and having that transaction be validated, confirmed and stored across thousands of computers over the world is immensely valuable; it's also very costly.

The ability of the network to stay synchronized and distributed (which are properties important for further aspects that make it valuable, such as its censorship-resistance and its apolitical nature) implies that the load that the network can take in terms of transactions per minute is limited. The leaner and lighter the load on the network, the more resistant it is to attacks, and the more neutral and robust it stays. This desire to stay lean comes in contrast with the demand for transactions by Bitcoin users, and the balance between the two is struck by having a limited amount of space for the total amount of bytes that can be transacted over the network per amount of time, yet allowing users to "bid" on that space.

Because the space is limited in terms of bytes per block and there is only 1 new block being mined every 10 minutes or so, bitcoin users compete for space on the blockchain by providing a fee for their transaction. The transactions with the highest fees are prioritized by the miners when building blocks.  
<br/>

# Tutorial 7: What is a wallet recovery phrase? — Concept
Modern bitcoin wallets use a process called _deterministic key generation_, where every new address your wallet produces is generated in a non-random fashion. This gives them the property of being very flexible in terms of generating new addresses while not having to remember them all individually (you do not have to make individual backups for all the addresses to which you received bitcoin). In fact the process looks a little bit like in the following diagram, where generating each "branch" of the tree is a process defined in advance and shared across all bitcoin wallets:

[image of HD wallet derivation tree]

The number of addresses that can be generated from this tree is near-infinite, yet because the process (the path the branches need to follow) is predetermined, the _only_ necessary part of backing up such a "tree of addresses" is to remember the root of the tree! Each new root will generate a completely new tree, but any wallet can recreate the whole tree simply by starting with the root.

The real word for this "root" of your wallet is the _wallet seed_. This wallet seed is simply a really, really big number, but numbers are notoriously hard to remember, and humans are usually really bad at writing them down properly without making mistakes or typos. Instead, a standard for how to encode these really big numbers has emerged in the bitcoin ecosystem. This standard uses a series of 12 or 24 words in a specific order, which together can always be used to recreate the wallet seed.

Padawan uses this standard to allow users to create backups for its wallet. You'll find the 12 words (often referred to as a backup phrase) on a separate screen under the drawer options. Those words should be treated with the _utmost_ care. They hold the key to all the bitcoin stored in a wallet, and they are indeed the _only_ thing required to access all funds. This is a blessing and a curse (but mostly a blessing!), because it means you can loose the phone your wallet is on or delete the application and yet not loose any funds simply by recovering your wallet using the 12-word backup phrase.  
<br/>

# Tutorial 8: Recovering your wallet from a recovery phrase — Skill
Recovering a wallet from a recovery phrase is usually fairly easy. Recovery phrases (sometimes called mnemonics) use a standard called BIP39, which provides a common way to recover coins between different wallets. The usual pattern for backing up and recovering a wallet is the following:

1. We create a wallet and the software provides us with either 12 or 24 words to write down and keep. The order of the words is important, so make sure you write them in the right order. In Padawan, you can see those 12 words at any time by going into the "Show Seed Phrase" screen. In most other wallets, you will only get a chance to see those words once, SO MAKE SURE YOU WRITE THEM DOWN AND TREAT THEM LIKE THE MONEY THEY REPRESENT. Upon creating a new wallet, you can always try to send a tiny amount of bitcoin to the wallet, delete the wallet, and attempt a full recovery using the words provided by the wallet. If all goes well and you backed up your wallet correctly, you will be able to re-enter the same wallet and the coins will be there.

In Padawan, you would click on "I already have a Padawan wallet" and enter your 12 words backup phrase.

This ability to recover a wallet using only your words is very powerful, because it means that you don't actually need the wallet to be installed on your phone to transport your bitcoin; you can simply keep the 12 words somewhere safe (learning them by heart is also often useful, although you should not rely on that uniquely).

This aspect of bitcoin software is often misunderstood and is worth taking the time to appreciate; bitcoins are not "held" in any mobile or desktop wallet. They are simply _accessed_ through them. The testnet coins you have on Padawan can be recovered and used on a range of other mobile, desktop, and hardware wallets that support BIP39 bitcoin wallets. If your recovery phrase is properly backed up, deleting the app on your phone is not an issue at all!
