package com.libertysoftware.padawanwallet.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainPagerAdapter(fragmentactvity: FragmentActivity) : FragmentStateAdapter(fragmentactvity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return mainTabFragments[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }

    private val mainTabFragments: Map<Int, () -> Fragment> = mapOf(
            0 to { WalletFragment() },
            1 to { TutorialsFragment() }
    )
}
