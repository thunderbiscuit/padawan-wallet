/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.wallet.wallet


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.data.Wallet
import com.goldenraven.padawanwallet.databinding.FragmentWalletBuildBinding
import com.goldenraven.padawanwallet.utils.SnackbarLevel
import com.goldenraven.padawanwallet.utils.fireSnackbar
import com.goldenraven.padawanwallet.wallet.WalletViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.bitcoindevkit.PartiallySignedBitcoinTransaction

private const val TAG = "WalletBuildFragment"

class WalletBuild : Fragment() {

    private lateinit var binding: FragmentWalletBuildBinding
    private lateinit var address: String
    private lateinit var amount: String
    private var feeRate : Float = 1F
    private lateinit var addressFromScanner: String
    private lateinit var psbt: PartiallySignedBitcoinTransaction
    private lateinit var viewModel: WalletViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentWalletBuildBinding.inflate(inflater, container, false)
        addressFromScanner = arguments?.getString("addressFromScanner", "0") ?: "0"
        Log.i(TAG, addressFromScanner)
        if (addressFromScanner != "0") {
            binding.sendAddress.setText(addressFromScanner)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(WalletViewModel::class.java)
        val navController = Navigation.findNavController(view)

        binding.buttonScan.setOnClickListener {
            navController.navigate(R.id.action_walletBuild_to_walletScan)
        }

        binding.button5.setOnClickListener {
            navController.navigate(R.id.action_walletSend_to_walletHome)
        }

        binding.buttonVerify.setOnClickListener {
            val validInputs: Boolean = verifyInputs()

            val addresseesAndAmounts: List<Pair<String, String>> = listOf(Pair(address, amount))

            // create transaction if possible, otherwise show snackbar with error
            try {
                psbt = Wallet.createTransaction(address, amount.toULong(), feeRate)
            } catch (e: Throwable) {
                Log.i(TAG,"${e.message}")
                fireSnackbar(
                    requireView(),
                    SnackbarLevel.ERROR,
                    "Error: ${e.message}"
                )
            }

            if (validInputs && this::psbt.isInitialized) {
                val broadcastMessage =
                        MaterialAlertDialogBuilder(this@WalletBuild.requireContext(), R.style.MyCustomDialogTheme)
                                .setTitle("Broadcast Transaction")
                                .setMessage(buildMessage(feeRate))
                                .setPositiveButton("Broadcast") { _, _ ->
                                    Log.i(TAG, "User is attempting to broadcast transaction")
                                    broadcastTransaction()
                                    navController.navigate(R.id.action_walletSend_to_walletHome)
                                }
                                .setNegativeButton("Go back") { _, _ ->
                                    Log.i(TAG, "User is not broadcasting")
                                }
                broadcastMessage.show()
            } else {
                Log.i(TAG,"Inputs were not valid")
            }
        }
    }

    private fun buildMessage(fees: Float): String {
        val sendToAddress: String = binding.sendAddress.text.toString().trim()
        val sendAmount: String = binding.sendAmount.text.toString().trim()
        val fees: String = fees.toString()
        val address = "Send to:\n$sendToAddress\n"
        val amount = "\nAmount: $sendAmount satoshis\n"
        val feeRate = "Fees: $fees satoshis\n"
        val total = "Total: ${fees.toDouble().toLong() + sendAmount.toLong()} satoshis"

        Log.i(TAG, "Message has inputs $sendToAddress, $sendAmount, $fees")
        return "$address$amount$feeRate$total"
    }


    private fun verifyInputs(): Boolean {
        address = binding.sendAddress.text.toString().trim()
        amount = binding.sendAmount.text.toString().trim()
        feeRate = binding.feeRate.value

        if (address == "") {
            fireSnackbar(
                requireView(),
                SnackbarLevel.WARNING,
                "Address is missing!"
            )
            Log.i(TAG,"Address field was empty")
            return false
        }
        if (amount == "") {
            fireSnackbar(
                requireView(),
                SnackbarLevel.WARNING,
                "Amount is missing!"
            )
            Log.i(TAG,"Amount field was empty")
            return false
        }
        if (feeRate <= 1 || feeRate > 200) {
            fireSnackbar(
                requireView(),
                SnackbarLevel.WARNING,
                "Please input a fee rate between 1 and 200"
            )
            Log.i(TAG,"Fee rate was invalid")
            return false
        }
        return true
    }

    private fun broadcastTransaction() {
        // var txidString: String = "string of txid"

        try {
            Wallet.sign(psbt)
            val txid = Wallet.broadcast(psbt)
            // val details = when (transaction) {
            //     is Transaction.Confirmed -> transaction.details
            //     is Transaction.Unconfirmed -> transaction.details
            // }
            // txidString = details.txid
            // val time : String = when(transaction) {
            //     is Transaction.Unconfirmed -> "Pending"
            //     is Transaction.Confirmed -> transaction.confirmation.timestamp.timestampToString()
            // }
            // val height : UInt = when(transaction) {
            //     is Transaction.Unconfirmed -> 100_000_000u
            //     is Transaction.Confirmed -> transaction.confirmation.height
            // }
            // val fees : ULong = details.fees ?: 0u
            // Log.i(TAG,"Transaction was broadcast! Data added to database:")
            Log.i(TAG,"Transaction was broadcast! txid: $txid")
            // Log.i(TAG,"Transaction was broadcast! timestamp: ${time}")
            // Log.i(TAG,"Transaction was broadcast! height: ${height}")
            // Log.i(TAG,"Transaction was broadcast! received: ${details.received}")
            // Log.i(TAG,"Transaction was broadcast! sent: ${details.sent}")
            // Log.i(TAG,"Transaction was broadcast! fees: ${details.fees}")

            // addTxToDatabase(
            //         details.txid,
            //         time,
            //         details.received.toInt(),
            //         details.sent.toInt(),
            //         fees.toInt(),
            //         height.toInt()
            // )
            fireSnackbar(
                requireView(),
                SnackbarLevel.INFO,
                "Transaction was broadcast successfully!"
            )
        } catch (e: Throwable) {
            Log.i(TAG,"${e.message}")
            fireSnackbar(
                requireView(),
                SnackbarLevel.ERROR,
                "Error: ${e.message}"
            )
        }
    }

    // private fun addTxToDatabase(txid: String, timestamp: String, txSatsIn: Int, txSatsOut: Int, fees: Int, height : Int) {
    //     val isSend: Boolean = isSend(sent = txSatsOut, received = txSatsIn)
    //     var valueIn: Int = 0
    //     var valueOut: Int = 0
    //     when (isSend) {
    //         true -> {
    //             valueOut = netSendWithoutFees(
    //                     txSatsOut = txSatsOut,
    //                     txSatsIn = txSatsIn,
    //                     fees = fees
    //             )
    //         }
    //         false -> {
    //             valueIn = txSatsIn
    //         }
    //     }
    //     val transaction: Tx = Tx(
    //             txid = txid,
    //             date = timestamp,
    //             valueIn = valueIn,
    //             valueOut = valueOut,
    //             fees = fees,
    //             isSend = isSend,
    //             height = height,
    //     )
    //     Log.i(TAG,"From addTxToDatabase, the tx is $transaction")
    //     viewModel.addTx(transaction)
    // }
}
