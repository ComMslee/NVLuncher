package com.nv.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nv.nvluncher.AppListFragment
import com.nv.nvluncher.HomeFragment
import com.nv.nvluncher.R
import gg.op.agro.p.PListAdapter

class PagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    var homeFragment: HomeFragment? = null
    var appListFragment: AppListFragment? = null

    override fun getItemCount(): Int {
        return 2;
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                homeFragment = HomeFragment()
                homeFragment!!
            }
            else -> {
                appListFragment = AppListFragment()
                appListFragment!!
            }
        }
    }

}