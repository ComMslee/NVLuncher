package com.nv.customview.topbar


import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.nv.nvluncher.R

import kotlinx.android.synthetic.main.view_top_bar.view.*
import java.text.SimpleDateFormat
import java.util.*


class TopBarView : ConstraintLayout {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }


    fun init(){
        val view: View = View.inflate(context, R.layout.view_top_bar, this)

        top_bar_store.setOnClickListener {
            val launchIntent = context.packageManager
                .getLaunchIntentForPackage("com.android.vending")
            context.startActivity(launchIntent)

        }
        top_bar_setting.setOnClickListener {
            val launchIntent = context.packageManager
                .getLaunchIntentForPackage("com.android.settings")
            context.startActivity(launchIntent)

        }

//        val c: Date = Calendar.getInstance().getTime()
//
//        val df = SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault())
//        val formattedDate: String = df.format(c)
//
//        top_bar_date.text = formattedDate

        var timerTask = object : TimerTask() {
            override fun run() {
                val c: Date = Calendar.getInstance().getTime()

                val df = SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault())
                val formattedDate: String = df.format(c)

                top_bar_date.post{
                    top_bar_date.text = formattedDate
                }
            }

        }

        Timer().schedule(timerTask,1000,1000)


    }


}