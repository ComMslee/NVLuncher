package com.nv.nvluncher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_app_list.*

class AppListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_app_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        var timerTask =object : TimerTask(){
//            override fun run() {
//                app_list.load(activity!!.packageManager)
//            }
//
//        }
//        Timer().schedule(timerTask,100000)
        app_list.load(activity!!.packageManager)
    }

    fun reload(){
        app_list.reload(activity!!.packageManager)
    }
    var isFirst :Boolean = true;


    override fun onResume() {
        super.onResume()

        if(isFirst){
            isFirst=false;
            if(app_list.adapter?.isEmpty() == true){
                app_list.reload(activity!!.packageManager)
            }
        }else{
            app_list.reload(activity!!.packageManager)
        }
//        app_list.load(activity!!.packageManager)
//        var timerTask =object : TimerTask(){
//            override fun run() {
//                app_list.load(activity!!.packageManager)
//            }
//
//        }
//        Timer().schedule(timerTask,500)

    }

}