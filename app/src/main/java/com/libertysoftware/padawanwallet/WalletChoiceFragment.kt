package com.libertysoftware.padawanwallet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment

class WalletChoiceFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wallet_choice, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button2).setOnClickListener {
            val intent: Intent = Intent(this@WalletChoiceFragment.context, MainActivity::class.java)
            startActivity(intent)
            // val activity? : Activity = getActivity()
            // activity.overridePendingTransition(R.anim.slide_in_bottom, R.anim.fragment_fade_exit)
        }

        view.findViewById<Button>(R.id.button3).setOnClickListener {
            // toast
            val text = "Currently in development..."
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(this@WalletChoiceFragment.context, text, duration)
            toast.show()
        }
    }
}
