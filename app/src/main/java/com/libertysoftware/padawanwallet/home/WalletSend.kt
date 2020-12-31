package com.libertysoftware.padawanwallet.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.libertysoftware.padawanwallet.R
import com.libertysoftware.padawanwallet.databinding.FragmentWalletSendBinding
import timber.log.Timber

class WalletSend : Fragment() {

    private lateinit var binding: FragmentWalletSendBinding
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
            val address: String = binding.sendAddress.text.toString().trim().toLowerCase()
            val amount: String = binding.sendAmount.text.toString()
            Timber.i("[PADAWANLOGS] Send address is: $address")
            Timber.i("[PADAWANLOGS] Amount to send is: $amount")

            val bundle = bundleOf("address" to address, "amount" to amount)
            navController.navigate(R.id.action_walletSend_to_walletVerify, bundle)
        }
    }
}
