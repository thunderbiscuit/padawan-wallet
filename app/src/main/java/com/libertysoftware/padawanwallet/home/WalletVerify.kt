package com.libertysoftware.padawanwallet.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.libertysoftware.padawanwallet.R
import com.libertysoftware.padawanwallet.databinding.FragmentWalletVerifyBinding

class WalletVerify : Fragment() {

    private lateinit var binding: FragmentWalletVerifyBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWalletVerifyBinding.inflate(inflater, container, false)
        val view: View = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val amount: String = requireArguments().getString("amount", "No amount provided")
        val address: String = requireArguments().getString("address", "No address provided")
        val fees: Int = 0
        val total: String = (amount.toInt() + fees).toString()

        binding.amountTransacted.text = amount
        binding.fees.text = fees.toString()
        binding.total.text = total
        binding.sendToAddress.text = address



        val navController = Navigation.findNavController(view)

        binding.buttonBackTransactionBuilder.setOnClickListener {
            navController.navigate(R.id.action_walletVerify_to_walletSend)
        }
    }
}
