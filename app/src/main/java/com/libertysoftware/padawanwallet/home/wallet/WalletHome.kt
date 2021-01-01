package com.libertysoftware.padawanwallet.home.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.libertysoftware.padawanwallet.R
import com.libertysoftware.padawanwallet.databinding.FragmentWalletHomeBinding
import com.libertysoftware.padawanwallet.home.HomeViewModel

class WalletHome : Fragment() {

    private lateinit var binding: FragmentWalletHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWalletHomeBinding.inflate(inflater, container, false)
        val view: View = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ViewModel
        val viewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)

        viewModel.balance.observe(viewLifecycleOwner, {
            binding.balance.text = it.toString()
        })

        binding.syncButton.setOnClickListener {
            viewModel.updateBalance()
        }

        // navigation
        val navController = Navigation.findNavController(view)

        binding.receiveButton.setOnClickListener {
            navController.navigate(R.id.action_walletHome_to_walletReceive)
        }

        binding.sendButton.setOnClickListener {
            navController.navigate(R.id.action_walletHome_to_walletSend)
        }
    }
}
