package com.nv.nvluncher

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcelable
import android.os.Process
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nv.customview.applist.AppData
import com.nv.customview.applist.AppHorizontalView
import com.nv.customview.applist.LogoListView
import com.nv.customview.widget.WidgetHost
import com.nv.customview.widget.WidgetView
import com.nv.lutil.model.WidgetData
import gg.op.agro.util.SharedPreferencesKeys
import gg.op.agro.util.Util
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {
    private lateinit var mAppWidgetHost: AppWidgetHost
    private var mAppWidgetManager: AppWidgetManager? = null
    lateinit var alert: AlertDialog
    lateinit var alertImages: AlertDialog
    lateinit var apps: ArrayList<AppData>
    var choice = 0
    val REQUEST_PICK_APPWIDGET_LEFT: Int = 20000
    val REQUEST_PICK_APPWIDGET_RIGHT: Int = 20001
    val REQUEST_CREATE_APPWIDGET_LEFT: Int = 20002
    val REQUEST_CREATE_APPWIDGET_RIGHT: Int = 20002
    var widgetData: WidgetData = WidgetData()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onPause() {
        super.onPause()
        hideAlert()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        home_ly_widget_left.setOnLongClickListener {
//            selectWidget(REQUEST_PICK_APPWIDGET_LEFT)
//            true
//        }
//        home_ly_widget_right.setOnLongClickListener {
//            selectWidget(REQUEST_PICK_APPWIDGET_RIGHT)
//            true
//        }

        home_iv_fmt.setOnClickListener {
            startActivity(Intent(context, RadioActivity::class.java))
        }

        main_image_view_logo.setOnLongClickListener {
            var logoView = LogoListView(requireContext())
            logoView.onItemClickListener = this::onLogoClick

            alertImages = AlertDialog.Builder(requireContext()).let {
                it.setTitle(getString(R.string.choose_the_logo))
                it.setView(logoView)
                it.setNegativeButton(getString(R.string.cancle)) { dialogInterface: DialogInterface, i: Int ->
                }
            }.create().apply {
                show()
            }
            true
        }

        mAppWidgetManager = AppWidgetManager.getInstance(requireContext().applicationContext)
        mAppWidgetHost = WidgetHost(requireContext().applicationContext, 1024)
        mAppWidgetHost.deleteHost()
        mAppWidgetHost.startListening()

        val sharedPreferences = Util.getSharedPreferences(requireContext())
//        var strWidget = sharedPreferences?.getString(SharedPreferencesKeys.WIDGET, "")
//
//        if (strWidget.isNullOrEmpty()) {
//
//        } else {
//            widgetData = Gson().fromJson(strWidget, WidgetData::class.java)
//        }
//
//        var classNameLeft = widgetData.classNameLeft
//        var packageNameLeft = widgetData.packageNameLeft
//        loadWidget(packageNameLeft, classNameLeft, REQUEST_PICK_APPWIDGET_LEFT)
//
//        var classNameRight = widgetData.classNameRight
//        var packageNameRight = widgetData.packageNameRight
//        loadWidget(packageNameRight, classNameRight, REQUEST_PICK_APPWIDGET_RIGHT)

        sharedPreferences?.let { sharedPreferences ->
            sharedPreferences.getInt(SharedPreferencesKeys.LOGO, 0)?.let { logoPosition ->
                val drawables = resources.obtainTypedArray(R.array.logos)
                main_image_view_logo.setImageDrawable(drawables.getDrawable(logoPosition))
            }

            var strApps = sharedPreferences.getString(SharedPreferencesKeys.APPS, "")
            apps = ArrayList()
            if (strApps.isNullOrEmpty()) {
                apps.add(makeAppDataNavi("n_com.android.settings"))
                apps.add(makeAppDataVideo("n_android.rk.RockVideoPlayer"))
                apps.add(makeAppDataMusic("n_com.android.music"))
                apps.add(makeAppDataInternet("n_acr.browser.barebones"))
            } else {
                var packages: ArrayList<String> =
                    Gson().fromJson(strApps, object : TypeToken<ArrayList<String>>() {}.type)
                packages.forEachIndexed { index, it ->
                    apps.add(makeAppData(index, it))
                }
            }
        }

        mappingApps()
        setAppClick()
    }

    private fun loadWidget(packageName: String, className: String, requestCode: Int) {
        mAppWidgetManager!!.getInstalledProvidersForPackage(
            packageName,
            Process.myUserHandle()
        ).forEach {
            if (className.equals(it.provider.className)) {
                try {
                    loadWidget(it, requestCode)
                } catch (e: Exception) {
                    loadWidget("com.nv.nvluncher", "com.nv.nvluncher.NVClock", requestCode)
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        mAppWidgetHost.stopListening()
    }

    fun selectWidget(requestCode: Int) {
        val appWidgetId: Int = this.mAppWidgetHost.allocateAppWidgetId()
        val pickIntent = Intent(AppWidgetManager.ACTION_APPWIDGET_PICK)
        pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        addEmptyData(pickIntent)
        startActivityForResult(pickIntent, requestCode)
    }

    fun addEmptyData(pickIntent: Intent) {
        val customInfo = ArrayList<Parcelable>()
        pickIntent.putParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_INFO, customInfo)
        val customExtras = ArrayList<Parcelable>()
        pickIntent.putParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS, customExtras)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PICK_APPWIDGET_LEFT || requestCode == REQUEST_PICK_APPWIDGET_RIGHT) {
                configureWidget(data, requestCode)
            } else if (requestCode == REQUEST_CREATE_APPWIDGET_LEFT) {
                createWidget(data, REQUEST_PICK_APPWIDGET_LEFT)
            } else if (requestCode == REQUEST_CREATE_APPWIDGET_RIGHT) {
                createWidget(data, REQUEST_PICK_APPWIDGET_RIGHT)
            }
        } else if (resultCode == RESULT_CANCELED && data != null) {
            val appWidgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)
            if (appWidgetId != -1) {
                mAppWidgetHost.deleteAppWidgetId(appWidgetId)
            }
        }
    }

    private fun configureWidget(data: Intent?, requestCode: Int) {
        val extras = data!!.extras
        val appWidgetId = extras!!.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)
        val appWidgetInfo = mAppWidgetManager!!.getAppWidgetInfo(appWidgetId)

        if (requestCode == REQUEST_PICK_APPWIDGET_LEFT) {
            widgetData.classNameLeft = appWidgetInfo.provider.className
            widgetData.packageNameLeft = appWidgetInfo.provider.packageName
        } else if (requestCode == REQUEST_PICK_APPWIDGET_RIGHT) {
            widgetData.classNameRight = appWidgetInfo.provider.className
            widgetData.packageNameRight = appWidgetInfo.provider.packageName
        }

        var editor = Util.getEditor(requireContext())
        editor?.putString(SharedPreferencesKeys.WIDGET, Gson().toJson(widgetData))
        editor?.commit()
        if (appWidgetInfo.configure != null) {
            try {
                val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE)
                intent.component = appWidgetInfo.configure
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                if (requestCode == REQUEST_PICK_APPWIDGET_LEFT) {
                    startActivityForResult(intent, REQUEST_CREATE_APPWIDGET_LEFT)
                } else if (requestCode == REQUEST_PICK_APPWIDGET_RIGHT) {
                    startActivityForResult(intent, REQUEST_CREATE_APPWIDGET_RIGHT)
                }
            } catch (e: Exception) {
                loadWidget("com.nv.nvluncher", "com.nv.nvluncher.NVClock", requestCode)
            }
        } else {
            createWidget(data, requestCode)
        }
    }

    private fun loadWidget(appWidgetInfo: AppWidgetProviderInfo, requestCode: Int) {
        if (appWidgetInfo.configure != null) {
            val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE)
            intent.component = appWidgetInfo.configure
            intent.putExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                mAppWidgetHost.allocateAppWidgetId()
            )
            if (requestCode == REQUEST_PICK_APPWIDGET_LEFT) {
                startActivityForResult(intent, REQUEST_CREATE_APPWIDGET_LEFT)
            } else if (requestCode == REQUEST_PICK_APPWIDGET_RIGHT) {
                startActivityForResult(intent, REQUEST_CREATE_APPWIDGET_RIGHT)
            }
        } else {
            createWidget(appWidgetInfo, requestCode)
        }
    }

    fun createWidget(data: Intent?, requestCode: Int) {
        val extras = data?.extras
        val appWidgetId = extras!!.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)
        val appWidgetInfo = mAppWidgetManager!!.getAppWidgetInfo(appWidgetId)

        makeHostView(appWidgetId, appWidgetInfo, requestCode)
    }


    fun createWidget(appWidgetInfo: AppWidgetProviderInfo, requestCode: Int) {
        var appWidgetId = mAppWidgetHost.allocateAppWidgetId()

        val allowed = mAppWidgetManager!!.bindAppWidgetIdIfAllowed(
            appWidgetId,
            appWidgetInfo.provider
        )

        if (!allowed) {
            // Request permission
            val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_BIND)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.putExtra(
                AppWidgetManager.EXTRA_APPWIDGET_PROVIDER,
                appWidgetInfo.provider
            )
            val REQUEST_BIND_WIDGET = 1987
            startActivityForResult(intent, REQUEST_BIND_WIDGET)
        }
        makeHostView(appWidgetId, appWidgetInfo, requestCode)
    }


    private fun makeHostView(
        appWidgetId: Int,
        appWidgetInfo: AppWidgetProviderInfo,
        requestCode: Int
    ) {
        val hostView: WidgetView = mAppWidgetHost.createView(
            context?.applicationContext,
            appWidgetId,
            appWidgetInfo
        ) as WidgetView

        hostView.setAppWidget(appWidgetId, appWidgetInfo)
        hostView.longClick = View.OnLongClickListener {
            selectWidget(requestCode)
            true
        }
        hostView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        if (requestCode == REQUEST_PICK_APPWIDGET_LEFT) {
//            hostView.updateAppWidgetSize(null,0,0,home_ly_widget_left.width,home_ly_widget_left.height)
            home_ly_widget_left.removeAllViews()
            home_ly_widget_left.addView(hostView)
        } else if (requestCode == REQUEST_PICK_APPWIDGET_RIGHT) {
//            hostView.updateAppWidgetSize(null,0,0,home_ly_widget_right.width,home_ly_widget_right.height)
            home_ly_widget_right.removeAllViews()
            home_ly_widget_right.addView(hostView)
        }
    }

    fun onLogoClick(position: Int, model: Drawable?) {
        main_image_view_logo.setImageDrawable(model)
        alertImages.dismiss()

        var editor = Util.getEditor(context!!)
        editor?.putInt(SharedPreferencesKeys.LOGO, position)
        editor?.commit()
    }

    fun onAppClick(position: Int, model: AppData?) {
        var packages = ArrayList<String>()
        apps.forEachIndexed { index, appData ->
            if (index == choice) {
                packages.add(model!!.packageName)
            } else {
                packages.add(appData.packageName)
            }
        }
        val str = Gson().toJson(packages)
        val editor = Util.getEditor(context!!)
        editor?.putString(SharedPreferencesKeys.APPS, str)
        editor?.commit()

        apps.clear()
        packages.forEachIndexed { index, it ->
            apps.add(makeAppData(index, it))
        }
        mappingApps()
        alert.dismiss()
    }

    fun makeAppData(index: Int, it: String) = when (index) {
        0 -> makeAppDataNavi(it)
        1 -> makeAppDataVideo(it)
        2 -> makeAppDataMusic(it)
        3 -> makeAppDataInternet(it)
        else -> makeAppDataNavi(it)
    }

    private fun makeAppDataNavi(it: String) = AppData(
        "Navigation", it, requireContext().getDrawable(R.drawable.bg_selector_navigation)!!
    )

    private fun makeAppDataVideo(it: String) = AppData(
        "Video", it, requireContext().getDrawable(R.drawable.bg_selector_video)!!
    )

    private fun makeAppDataMusic(it: String) = AppData(
        "Music", it, requireContext().getDrawable(R.drawable.bg_selector_music)!!
    )

    private fun makeAppDataInternet(it: String) = AppData(
        "Internet", it, requireContext().getDrawable(R.drawable.bg_selector_ineternet)!!
    )

    private fun mappingApps() {
        val thumb = arrayOf(home_iv_one, home_iv_two, home_iv_three, home_iv_four)
        apps.forEachIndexed { index, appData ->
            thumb[index].setImageDrawable(appData.thumb)
        }
    }

    private fun setAppClick() {
        val appViews =
            arrayOf(hoem_ly_app_one, hoem_ly_app_two, hoem_ly_app_three, hoem_ly_app_four)
        appViews.forEachIndexed { index, linearLayout ->
            linearLayout.setOnClickListener {
                requireContext().apply {
                    try {
                        val packageName = apps[index].packageName.replace(Regex("^n_"), "")
                        startActivity(packageManager.getLaunchIntentForPackage(packageName))
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }
            }

            linearLayout.setOnLongClickListener {
                choice = index
                AppHorizontalView(requireContext()).let { view ->
                    view.onItemClickListener = this::onAppClick
                    view.load(requireContext().packageManager)
                    alert = AlertDialog.Builder(requireContext()).apply {
                        setTitle(R.string.choose_the_app)
                        setView(view)
                        setNegativeButton(R.string.cancle) { dialogInterface: DialogInterface, i: Int -> }
                    }.create().apply {
                        show()
                    }
                }
                true
            }
        }
    }

    fun hideAlert() {
        try {
            alert.dismiss()
        } catch (e: Exception) {
        }
        try {
            alertImages.dismiss()
        } catch (e: Exception) {
        }
    }
}