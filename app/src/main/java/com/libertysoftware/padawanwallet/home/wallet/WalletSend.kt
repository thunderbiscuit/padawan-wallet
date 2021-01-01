package com.libertysoftware.padawanwallet.home.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.libertysoftware.padawanwallet.PadawanWalletApplication
import com.libertysoftware.padawanwallet.R
import com.libertysoftware.padawanwallet.databinding.FragmentWalletSendBinding
import org.bitcoindevkit.bdkjni.Types.CreateTxResponse
import timber.log.Timber

class WalletSend : Fragment() {

    private lateinit var binding: FragmentWalletSendBinding
    private lateinit var transactionDetails: CreateTxResponse
    private lateinit var address: String
    private lateinit var amount: String

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWalletSendBinding.inflate(inflater, container, false)
        val view: View = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = Navigation.findNavController(view)

        binding.button5.setOnClickListener {
            navController.navigate(R.id.action_walletSend_to_walletHome)
        }

        binding.buttonVerify.setOnClickListener {
            verifyTransaction()
            // val bundle = bundleOf("address" to address, "amount" to amount, "transactionDetails" to transactionDetails)
            val bundle = bundleOf("address" to address, "amount" to amount)
            navController.navigate(R.id.action_walletSend_to_walletVerify, bundle)
        }
    }

    private fun verifyTransaction() {
        val app = requireActivity().application as PadawanWalletApplication
        address = binding.sendAddress.text.toString().trim()
        amount = binding.sendAmount.text.toString()
        val addresseesAndAmounts: List<Pair<String, String>> = listOf(Pair(address, amount))
        val feeRate = 1F

        Timber.i("[PADAWANLOGS] Send addresses are: $addresseesAndAmounts")

        try {
            transactionDetails = app.createTransaction(feeRate, addresseesAndAmounts, false, null, null, null)
            Timber.i("[PADAWANLOGS] transactionDetails from WalletSend fragment is: $transactionDetails")
        } catch (e: Throwable) {
            Timber.i("[PADAWANLOGS] Verify transaction failed: ${e.message}")
            // TODO: Show toast related to error
            return
        }
    }
}
