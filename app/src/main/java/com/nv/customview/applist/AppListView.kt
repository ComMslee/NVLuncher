package com.nv.customview.applist

//import android.widget.Toast
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nv.customview.decoration.SpacesGridItemDecoration
import com.nv.lutil.listener.OnItemClickListener
import com.nv.lutil.util.Util
import com.nv.nvluncher.MainActivity
import com.nv.nvluncher.R
import kotlin.reflect.KFunction0
import kotlin.reflect.KFunction2

class AppListView : RecyclerView {
    val TAG = "AppListView"
    var onItemClickListener: KFunction2<Int, AppData?, Unit>? = null
    var onEmptyListener: KFunction0<Unit>? = null
    lateinit var packageManager: PackageManager
    var isLoading: Boolean = false

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
        var appListAdapter = AppListAdapter()
        appListAdapter.onItemClickListener = object : OnItemClickListener<AppData> {
            override fun onItemClick(position: Int, model: AppData) {
                val launchIntent = context.packageManager
                    .getLaunchIntentForPackage(model.packageName)
                context.startActivity(launchIntent)

//            Toast.makeText(context, model.packageName, Toast.LENGTH_LONG).show()

                if (Util.DEBUG) {
                    Log.e("packageName ", model.packageName)
                }

                if (appListAdapter.itemCount == 0) {
                    onEmptyListener?.let { it() }
                }

                onItemClickListener?.let { it(position, model) }

            }

            override fun onItemLongClick(position: Int, model: AppData): Boolean {
                val appinfo = context.packageManager.getPackageInfo(
                    model.packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES
                )
                if (isUserApp(appinfo.applicationInfo)) {
                    try {
                        val packageURI = Uri.parse("package:" + model.packageName)  // 1
                        Intent(Intent.ACTION_DELETE, packageURI).apply {
                            context.startActivity(this)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    Toast.makeText(context, resources.getString(R.string.msg_delete_systemapp), Toast.LENGTH_SHORT).show()
                }
                return true
            }

        }

//        adapter.add(MainActivity.preAppData)

        adapter = appListAdapter
        layoutManager = GridLayoutManager(context, 6)

        addItemDecoration(SpacesGridItemDecoration(Util.convertDpToPixel(14f, context)))
    }

    fun isUserApp(ai: ApplicationInfo): Boolean {
        val mask = ApplicationInfo.FLAG_SYSTEM or ApplicationInfo.FLAG_UPDATED_SYSTEM_APP
        return (ai.flags and mask) == 0
    }

    fun load(packageManager: PackageManager) {
        this.packageManager = packageManager

        if (isLoading) return

        synchronized(isLoading) {
            isLoading = true
            var preAppData: ArrayList<AppData> = ArrayList()
            MainActivity.preAppData?.let {
                preAppData.addAll(it)
                MainActivity.preAppData = null
            } ?: run {
                val applist = Util.appList(packageManager)
                preAppData.addAll(applist)
            }

            adapter?.apply {
                clear()
                add(preAppData)
                notifyDataSetChanged()
            }

            isLoading = false
        }
    }

//    fun reload(packageManager: PackageManager) {
//        this.packageManager = packageManager
//
//        synchronized(isLoading) {
//            if (isLoading) return
//            isLoading = true
//            val applist = Util.appList(packageManager)
//            adapter?.apply {
//                clear()
//                add(applist)
//                notifyDataSetChanged()
//            }
//
//            isLoading = false
//        }
//    }

    override fun getAdapter(): AppListAdapter? {
        return super.getAdapter() as AppListAdapter
    }
}

