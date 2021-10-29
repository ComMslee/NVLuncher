package com.nv.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nv.nvluncher.AppListFragment
import com.nv.nvluncher.HomeFragment

class PagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    var homeFragment: HomeFragment? = null
    var appListFragment: AppListFragment? = null

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                HomeFragment().apply {
                    homeFragment = this
                }
            }
            else -> {
                AppListFragment().apply {
                    appListFragment = this
                }
            }
        }
    }

}