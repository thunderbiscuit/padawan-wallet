package com.libertysoftware.padawanwallet.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.libertysoftware.padawanwallet.PadawanWalletApplication
import com.libertysoftware.padawanwallet.R
import com.libertysoftware.padawanwallet.databinding.FragmentWalletVerifyBinding
import org.bitcoindevkit.bdkjni.Types.CreateTxResponse
import org.bitcoindevkit.bdkjni.Types.RawTransaction
import org.bitcoindevkit.bdkjni.Types.SignResponse
import org.bitcoindevkit.bdkjni.Types.Txid
import timber.log.Timber

class WalletVerify : Fragment() {

    private lateinit var binding: FragmentWalletVerifyBinding
    private lateinit var amount: String
    private lateinit var address: String
    private lateinit var transactionDetails: CreateTxResponse

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        amount = requireArguments().getString("amount", "No amount provided")
        address = requireArguments().getString("address", "No address provided")

        binding = FragmentWalletVerifyBinding.inflate(inflater, container, false)
        val view: View = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fees: Int = 0
        val total: String = (amount.toInt() + fees).toString()

        binding.amountTransacted.text = amount
        binding.fees.text = fees.toString()
        binding.total.text = total
        binding.sendToAddress.text = address

        binding.buttonBroadcastTransaction.setOnClickListener {
            verifyTransaction()
            broadcastTransaction()
        }

        val navController = Navigation.findNavController(view)

        binding.buttonBackTransactionBuilder.setOnClickListener {
            navController.navigate(R.id.action_walletVerify_to_walletSend)
        }
    }

    private fun verifyTransaction() {
        val app = requireActivity().application as PadawanWalletApplication
        val addresseesAndAmounts: List<Pair<String, String>> = listOf(Pair(address, amount))
        val feeRate = 1F

        Timber.i("[PADAWANLOGS] Send addresses are: $addresseesAndAmounts")

        try {
            transactionDetails = app.createTransaction(feeRate, addresseesAndAmounts, false, null, null, null)
            Timber.i("[PADAWANLOGS] transactionDetails from WalletVerify fragment is: $transactionDetails")
        } catch (e: Throwable) {
            Timber.i("[PADAWANLOGS] Verify transaction failed: ${e.message}")
            // TODO: Show toast related to error
            return
        }
    }

    private fun broadcastTransaction() {
        val app = requireActivity().application as PadawanWalletApplication
        var txidString: String = "string of txid"

        try {
            val signResponse: SignResponse = app.sign(transactionDetails.psbt)
            val rawTx: RawTransaction = app.extractPsbt(signResponse.psbt)
            val txid: Txid = app.broadcast(rawTx.transaction)
            txidString = txid.toString()
            Timber.i("[PADAWANLOGS] Transaction was broadcasted! txid: $txid, txidString: $txidString")
        } catch (e: Throwable) {
            Timber.i("[PADAWANLOGS] ${e.message}")
        }
        showBroadcastSuccessToast(txid = txidString)
    }

    private fun showBroadcastSuccessToast(txid: String) {
        val text: String = "Transaction was broadcasted successfully\ntxid: $txid"
        val toast: Toast = Toast.makeText(this@WalletVerify.context, text, Toast.LENGTH_LONG)
        toast.show()
    }
}
