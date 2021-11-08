package com.nv.nvluncher

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_app_list.*

class AppListFragment : Fragment() {
    var isFirst: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_app_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        reload()
    }

    override fun onResume() {
        super.onResume()
        if (isFirst) {
            isFirst = false
            if (app_list.adapter?.isEmpty() == true) {
                reload()
            }
        } else {
            reload()
        }
    }

    fun reload() {
        app_list.load(requireContext().packageManager)
    }
}