/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.wallet.wallet


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.data.Tx
import com.goldenraven.padawanwallet.data.Wallet
import com.goldenraven.padawanwallet.databinding.FragmentWalletBuildBinding
import com.goldenraven.padawanwallet.utils.SnackbarLevel
import com.goldenraven.padawanwallet.utils.fireSnackbar
import com.goldenraven.padawanwallet.utils.isSend
import com.goldenraven.padawanwallet.utils.netSendWithoutFees
import com.goldenraven.padawanwallet.wallet.WalletViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.bitcoindevkit.bdkjni.Types.*
import timber.log.Timber

class WalletBuild : Fragment() {

    private lateinit var binding: FragmentWalletBuildBinding
    private lateinit var address: String
    private lateinit var amount: String
    var feeRate : Float = 1F
    private lateinit var addressFromScanner: String
    private lateinit var transactionDetails: CreateTxResponse
    private lateinit var viewModel: WalletViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentWalletBuildBinding.inflate(inflater, container, false)
        addressFromScanner = arguments?.getString("addressFromScanner", "0") ?: "0"
        Timber.i("padawanlogs $addressFromScanner")
        if (addressFromScanner != "0") {
            binding.sendAddress.setText(addressFromScanner)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = Navigation.findNavController(view)

        binding.buttonScan.setOnClickListener {
            navController.navigate(R.id.action_walletBuild_to_walletScan)
        }

        binding.button5.setOnClickListener {
            navController.navigate(R.id.action_walletSend_to_walletHome)
        }

        binding.buttonVerify.setOnClickListener {
            val validInputs: Boolean = verifyTransaction()

            if (validInputs) {
                var broadcastMessage =
                        MaterialAlertDialogBuilder(this@WalletBuild.requireContext(), R.style.MyCustomDialogTheme)
                                .setTitle("Broadcast Transaction")
                                .setMessage(buildMessage())
                                .setPositiveButton("Broadcast") { _, _ ->
                                    Log.i("Padawan Wallet", "User is attempting to broadcast transaction")
                                    broadcastTransaction()
                                    navController.navigate(R.id.action_walletSend_to_walletHome)
                                }
                                .setNegativeButton("Go back") { _, _ ->
                                    Log.i("Padawan Wallet", "User is not broadcasting")
                                }
                broadcastMessage.show()
            } else {
                Timber.i("[PADAWANLOGS] Inputs were not valid")
            }
        }
    }

    private fun buildMessage(): String {
        val sendToAddress: String = binding.sendAddress.text.toString().trim()
        val sendAmount: String = binding.sendAmount.text.toString().trim()
        val fees: String = transactionDetails.details.fees.toString()

        val address = "Send to address:\n$sendToAddress\n"
        val amount = "\nAmount Transacted:  $sendAmount\n"
        val feeRate = "Fee Rate:  $fees\n\n"
        val total = "Total:  ${fees.toLong() + sendAmount.toLong()}\n\n"

        Log.i("PadawanWallet", "Message has inputs $sendToAddress, $sendAmount, $fees")
        return amount + feeRate + total + address
    }


    private fun verifyTransaction(): Boolean {
        address = binding.sendAddress.text.toString().trim()
        amount = binding.sendAmount.text.toString().trim()
        feeRate = binding.feeRate.text.toString().toFloat()

        if (address == "") {
            fireSnackbar(
                requireView(),
                SnackbarLevel.WARNING,
                "Address is missing!"
            )
            Timber.i("[PADAWANLOGS] Address field was empty")
            return false
        }
        if (amount == "") {
            fireSnackbar(
                requireView(),
                SnackbarLevel.WARNING,
                "Amount is missing!"
            )
            Timber.i("[PADAWANLOGS] Amount field was empty")
            return false
        }
        if (feeRate <= 0 || feeRate > 200) {
            fireSnackbar(
                    requireView(),
                    SnackbarLevel.WARNING,
                    "Please input fee rate between 1 to 200"
            )
            return false
        }
        val addresseesAndAmounts: List<Pair<String, String>> = listOf(Pair(address, amount))

        Timber.i("[PADAWANLOGS] Send addresses are: $addresseesAndAmounts")

        try {
            transactionDetails = Wallet.createTransaction(feeRate, addresseesAndAmounts, false, null, null, null)
        } catch (e: Throwable) {
            Timber.i("[PADAWANLOGS] Verify transaction failed: ${e.message}")
            fireSnackbar(
                requireView(),
                SnackbarLevel.ERROR,
                "Error: ${e.message}"
            )
            return false
        }

        return true
    }
    private fun broadcastTransaction() {
        var txidString: String = "string of txid"

        try {
            val signResponse: SignResponse = Wallet.sign(transactionDetails.psbt)
            val rawTx: RawTransaction = Wallet.extractPsbt(signResponse.psbt)
            val txid: Txid = Wallet.broadcast(rawTx.transaction)
            txidString = txid.toString()

            Timber.i("[PADAWANLOGS] Transaction was broadcast! Data added to database:")
            Timber.i("[PADAWANLOGS] Transaction was broadcast! txid: ${transactionDetails.details.txid}")
            Timber.i("[PADAWANLOGS] Transaction was broadcast! timestamp: ${transactionDetails.details.timestamp}")
            Timber.i("[PADAWANLOGS] Transaction was broadcast! received: ${transactionDetails.details.received.toInt()}")
            Timber.i("[PADAWANLOGS] Transaction was broadcast! sent: ${transactionDetails.details.sent.toInt()}")
            Timber.i("[PADAWANLOGS] Transaction was broadcast! fees: ${transactionDetails.details.fees.toInt()}")
            // add tx to database
            addTxToDatabase(
                    transactionDetails.details.txid,
                    transactionDetails.details.timestamp.toString(),
                    transactionDetails.details.received.toInt(),
                    transactionDetails.details.sent.toInt(),
                    transactionDetails.details.fees.toInt(),
            )
            Timber.i("[PADAWANLOGS] Transaction was broadcast! txid: $txid, txidString: $txidString")
            fireSnackbar(
                    requireView(),
                    SnackbarLevel.INFO,
                    "Transaction was broadcast successfully!"
            )
        } catch (e: Throwable) {
            Timber.i("[PADAWANLOGS] ${e.message}")
            fireSnackbar(
                requireView(),
                SnackbarLevel.ERROR,
                "Error: ${e.message}"
            )
        }
    }

    private fun addTxToDatabase(txid: String, timestamp: String, txSatsIn: Int, txSatsOut: Int, fees: Int) {
        // val isSend: Boolean = isSend(sent = valueOut, received = valueIn)
        // val tx = Tx(txid, timestamp, valueIn, valueOut, fees, isSend)
        val isSend: Boolean = isSend(sent = txSatsOut, received = txSatsIn)
        var valueIn: Int = 0
        var valueOut: Int = 0
        when (isSend) {
            true -> {
                valueOut = netSendWithoutFees(
                        txSatsOut = txSatsOut,
                        txSatsIn = txSatsIn,
                        fees = fees
                )
            }
            false -> {
                valueIn = txSatsIn
            }
        }
        val transaction: Tx = Tx(
                txid = txid,
                date = timestamp,
                valueIn = valueIn,
                valueOut = valueOut,
                fees = fees,
                isSend = isSend
        )
        Timber.i("[PADAWANLOGS] From addTxToDatabase, the tx is $transaction")
        viewModel.addTx(transaction)
    }
}
