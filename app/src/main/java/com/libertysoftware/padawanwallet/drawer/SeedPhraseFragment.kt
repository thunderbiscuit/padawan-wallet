package com.libertysoftware.padawanwallet.drawer

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.libertysoftware.padawanwallet.R
import timber.log.Timber

class SeedPhraseFragment : Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seedphrase, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        populateSeedPhrase()
    }

    fun populateSeedPhrase() {
        // retrieve seed phrase from shared preferences
        val sharedPref = this.getActivity()?.getSharedPreferences("current_wallet", Context.MODE_PRIVATE)
        val seedphrase: String? = sharedPref?.getString("seedphrase", "No seed phrase")
        Timber.i("Seed phrase: $seedphrase")
        getView()?.findViewById<TextView>(R.id.seedPhraseText)?.text = seedphrase
    }
}
