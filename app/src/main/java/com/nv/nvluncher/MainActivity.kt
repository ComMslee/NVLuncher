package com.nv.nvluncher

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.nv.adapter.PagerAdapter
import com.nv.customview.applist.AppData
import gg.op.agro.util.SharedPreferencesKeys
import gg.op.agro.util.Util
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_bottom_bar.view.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private val ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE: Int = 2000

    companion object {
        var position = 0
        var preAppData: ArrayList<AppData>? = ArrayList()
    }

    private var contentObserver: ContentObserver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        load()

        setContentView(R.layout.activity_main)

        viewPager.offscreenPageLimit = 5
        viewPager.adapter = PagerAdapter(this)
        main_bottombar.pager = viewPager

        var sel = Util.getSharedPreferences(this)!!.getInt(SharedPreferencesKeys.BAR, 0)

        viewPager.currentItem = position

//        window.decorView.apply {
//            // Hide both the navigation bar and the status bar.
//            // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
//            // a general rule, you should design your app to hide the status bar whenever you
//            // hide the navigation bar.
//            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//        }
//        val uri: Uri = Uri.fromParts("package", packageName, null)
//        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, uri)
//        startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE)
//        if (!Settings.canDrawOverlays(this)) {              // 다른앱 위에 그리기 체크
//            val uri: Uri = Uri.fromParts("package", packageName, null)
//            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, uri)
//            startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE)
//        } else {
//            init()
//        }

        init()

        contentObserver = object : ContentObserver(Handler(mainLooper)) {
            override fun onChange(selfChange: Boolean) {
                Log.e("LB_NavBar", "onchange LB_NAVBAR_POSITION")
            }
        }

        contentObserver?.let {
            contentResolver.registerContentObserver(
                Settings.Global.getUriFor("lb_navabar_position"), false,
                it
            )
        }

        IntentFilter().apply {
            addAction(ACTION_HOME)
            addAction(ACTION_APP)
            addAction(ACTION_BAR)
            registerReceiver(bottomReceiver, this)
        }

        IntentFilter().apply {
            addAction(Intent.ACTION_PACKAGE_ADDED)
            addAction(Intent.ACTION_PACKAGE_REPLACED)
            addAction(Intent.ACTION_PACKAGE_REMOVED)
            addDataScheme("package")
            registerReceiver(appReceiver, this)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        var mode = intent?.getIntExtra("mode", -1)

        val pageNo = if (mode == 1) { // ALL APPS
            1
        } else { // HOME
            0
        }
        viewPager?.setCurrentItem(pageNo, true)
    }

    fun load() {
        var models = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        for (i in 0 until models.size) {
            var model = models[i]
            if (packageManager.getLaunchIntentForPackage(model.packageName) != null) {
                preAppData?.add(
                    AppData(
                        model.loadLabel(packageManager),
                        model.packageName,
                        model.loadIcon(packageManager)
                    )
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.e(TAG, "onResume") //mslee add log
    }

    override fun onPause() {
        super.onPause()
        Log.e(TAG, "onPause") //mslee add log
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(bottomReceiver)
        unregisterReceiver(appReceiver)
    }

    private fun setWindowFlag(bits: Int, on: Boolean) {
        val win = window ?: return
        val winParams = win.attributes
        winParams.flags = if (on) {
            winParams.flags or bits
        } else {
            winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    private fun init() {
//        val inflater =
//            getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        var view = BottomBarView(applicationContext)
//        view.pager = viewPager
//
//        var manager =
//            applicationContext.getSystemService(WINDOW_SERVICE) as WindowManager
//
//        val params = WindowManager.LayoutParams(
//            WindowManager.LayoutParams.MATCH_PARENT,
//            WindowManager.LayoutParams.WRAP_CONTENT,
//            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
//            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
//                    or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                    or WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
//                    or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
//                    or WindowManager.LayoutParams.FLAG_FULLSCREEN
//                    or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            PixelFormat.TRANSLUCENT
//        )
//
//        params.gravity = Gravity.BOTTOM
//
//        //make sure height includes the nav bar size (get the dimension of whole screen)
//
//        windowManager.addView(view, params)
//
//        view.bottom_bar_hide.setOnClickListener {
//            view.bottom_bar_ly_bar.visibility = View.VISIBLE
//            view.bottom_bar_hide.visibility = View.GONE
//
//            setTimer(view)
//        }
//
//        setTimer(view)
        setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
    }

    fun setTimer(view: View) {
        var timerTask = object : TimerTask() {
            override fun run() {
                view.post {
                    view.bottom_bar_ly_bar.visibility = View.GONE
                    view.bottom_bar_hide.visibility = View.VISIBLE
                }
            }
        }
        Timer().schedule(timerTask, 5000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            init()
        }
    }

    override fun onBackPressed() {}

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean =
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            false
        } else {
            super.onKeyDown(keyCode, event)
        }

    val ACTION_HOME = "com.nv.nvluncher.ACTION_HOME"
    val ACTION_APP = "com.nv.nvluncher.ACTION_APP"
    val ACTION_BAR = "android.intent.action.lb.navbaropt"

    val appReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            when (intent.action) {
                ACTION_PACKAGE_REMOVED,
                ACTION_PACKAGE_REPLACED,
                ACTION_PACKAGE_ADDED -> {
                    viewPager.setCurrentItem(1, true)
                    val adapter = viewPager.adapter
                    if (adapter is PagerAdapter) {
                        adapter.appListFragment?.reload()
                    }
                }
            }
        }
    }

    val bottomReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            when (intent.action) {
                ACTION_HOME -> {
                    viewPager.setCurrentItem(0, true)
                }

                ACTION_APP -> {
                    viewPager.setCurrentItem(1, true)
                }

                ACTION_BAR -> {
                    val adapter = viewPager.adapter
                    if (adapter is PagerAdapter) {
                        adapter.homeFragment?.hideAlert()
                    }
                }
            }
        }
    }
}