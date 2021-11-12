package com.nv.customview.bottombar

import android.app.AlertDialog
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.viewpager2.widget.ViewPager2
import com.nv.lutil.listener.OnItemClickListener
import com.nv.lutil.util.SharedPreferencesKeys
import com.nv.nvluncher.MainActivity
import com.nv.nvluncher.R
import com.nv.lutil.util.Util
import kotlinx.android.synthetic.main.view_bottom_bar.view.*

class BottomBarView : ConstraintLayout {
    var pager: ViewPager2? = null
    var onLocationChangedListener: OnItemClickListener<Long>? = null

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

    private fun init() {
        val view: View = View.inflate(context, R.layout.view_bottom_bar, this)

        filterTouchesWhenObscured = true

        bottom_bar_image_view_wifi.setOnClickListener {
            context.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
        }

        bottom_bar_image_view_sound.setOnClickListener {
            Thread {
                Instrumentation()
                    .sendKeyDownUpSync(KeyEvent.KEYCODE_VOLUME_UP)
            }.start()
        }

        bottom_bar_image_view_app_list.setOnClickListener {
            context.startActivity(Intent(context, MainActivity::class.java))
            pager?.setCurrentItem(1, true)

        }
        bottom_bar_image_view_home.setOnClickListener {
            pager?.setCurrentItem(0, true)
            context.startActivity(Intent(context, MainActivity::class.java))
        }

        bottom_bar_image_view_back.setOnClickListener {
            Thread {
                Instrumentation()
                    .sendKeyDownUpSync(KeyEvent.KEYCODE_BACK)
            }.start()
        }

        bottom_bar_image_view_setting.setOnClickListener {

            val spinner = Spinner(context)
            val userNames = arrayOf("아래", "왼쪽", "오른쪽")
            val arrayadapter =
                ArrayAdapter(context, android.R.layout.simple_spinner_item, userNames)
            spinner.adapter = arrayadapter


            val alertDialogBuilder = AlertDialog.Builder(context)
            alertDialogBuilder.setTitle("네비게이션 바 설정")
            alertDialogBuilder.setMessage("네비게이션 위치를 설정해주세요.")
            alertDialogBuilder.setView(spinner)
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()

            var sel = Util.getSharedPreferences(context)!!.getInt(SharedPreferencesKeys.BAR, 0)
            spinner.setSelection(sel, false)

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    setPosition(position)
                    onLocationChangedListener?.onItemClick(position, id)
                    alertDialog.hide()

                    var editor = Util.getEditor(context)
                    editor?.putInt(SharedPreferencesKeys.BAR, position)
                    editor?.commit()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
        }

        var sel = Util.getSharedPreferences(context)!!.getInt(SharedPreferencesKeys.BAR, 0)

        setPosition(sel)
    }

    fun setPosition(position: Int) {
        var orientation = when (position) {
            1, 2 -> {
                LinearLayout.VERTICAL
            }
            else -> {
                LinearLayout.HORIZONTAL
            }
        }

        if (bottom_bar_ly_bar.orientation == orientation) return

        bottom_bar_ly_bar.orientation = orientation
        val layoutParams = bottom_bar_ly_bar.layoutParams
        val temp = layoutParams.width
        layoutParams.width = layoutParams.height
        layoutParams.height = temp
        bottom_bar_ly_bar.layoutParams = layoutParams

        bottom_bar_ly_bar.children.forEach {
            val layoutParams = it.layoutParams
            val temp = layoutParams.width
            layoutParams.width = layoutParams.height
            layoutParams.height = temp
            it.layoutParams = layoutParams
        }
    }
}