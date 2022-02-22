package com.nv.customview.topbar

import android.content.Context
import android.util.AttributeSet
import android.view.View
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

    fun init() {
        val view: View = View.inflate(context, R.layout.view_top_bar, this)

        top_bar_store.setOnClickListener {
            callApp("com.android.vending")
        }

        top_bar_setting.setOnClickListener {
            callApp("com.android.settings")
        }

        val timerTask = object : TimerTask() {
            override fun run() {
//                val df = SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault())
                val language = Locale.getDefault()
                val format =
                    android.text.format.DateFormat.getBestDateTimePattern(language, "yyyyMMMMdd")
                val df = SimpleDateFormat(format, Locale.getDefault())

                val c: Date = Calendar.getInstance().time
                val formattedDate: String = df.format(c)

                top_bar_date.apply {
                    if (top_bar_date.text.toString() != formattedDate) {
                        post {
                            text = formattedDate
                        }
                    }
                }
            }
        }
        Timer().schedule(timerTask, 0, 1000)
    }

    fun callApp(packagename: String) {
        try {
            context.packageManager.getLaunchIntentForPackage(packagename).apply {
                context.startActivity(this)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}