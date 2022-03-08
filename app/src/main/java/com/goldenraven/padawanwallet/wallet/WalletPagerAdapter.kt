/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.wallet

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class WalletPagerAdapter(fragmentactvity: FragmentActivity) : FragmentStateAdapter(fragmentactvity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return mainTabFragments[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }

    private val mainTabFragments: Map<Int, () -> Fragment> = mapOf(
            0 to { WalletFragment() },
            1 to { TutorialsFragment() }
    )
}
