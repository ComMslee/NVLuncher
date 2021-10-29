package com.nv.customview.applist

import android.content.Context
import android.content.pm.PackageManager
import android.util.AttributeSet
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nv.lutil.listener.OnItemClickListener
import com.nv.nvluncher.MainActivity
import gg.op.agro.decoration.SpacesGridItemDecoration
import gg.op.agro.util.Util
import kotlin.reflect.KFunction0
import kotlin.reflect.KFunction2

class AppListView : RecyclerView {
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
        appListAdapter.onItemClickListener = OnItemClickListener { position, model ->
            val launchIntent = context.packageManager
                .getLaunchIntentForPackage(model.packageName)
            context.startActivity(launchIntent)

            Toast.makeText(context, model.packageName, Toast.LENGTH_LONG).show()

            Log.e("packageName ", model.packageName)

            if (appListAdapter.itemCount == 0) {
                onEmptyListener?.let { it() }
            }

            onItemClickListener?.let { it(position, model) }
        }

//        adapter.add(MainActivity.preAppData)

        adapter = appListAdapter
        layoutManager = GridLayoutManager(context, 6)

        addItemDecoration(SpacesGridItemDecoration(Util.convertDpToPixel(14f, context)))
    }

    fun load(packageManager: PackageManager) {
        this.packageManager = packageManager

        synchronized(isLoading) {
            if (isLoading) return
            isLoading = true
            var preAppData: ArrayList<AppData> = ArrayList()
            if (MainActivity.preAppData != null) {
                preAppData = MainActivity.preAppData ?: ArrayList()
                MainActivity.preAppData = null
            } else {
                var models = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
                for (i in 0 until models.size) {
                    var model = models[i]
                    if (context.packageManager
                            .getLaunchIntentForPackage(model.packageName) != null
                    ) {
                        preAppData.add(
                            AppData(
                                model.loadLabel(packageManager),
                                model.packageName,
                                model.loadIcon(packageManager)
                            )
                        )
                    }

                }
            }

            adapter?.apply {
                clear()
                add(preAppData)
                notifyDataSetChanged()
            }

            isLoading = false
        }
    }

    fun reload(packageManager: PackageManager) {
        this.packageManager = packageManager

        synchronized(isLoading) {
            if (isLoading) return
            isLoading = true
            var preAppData: ArrayList<AppData> = ArrayList()
            var models = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            for (i in 0 until models.size) {
                var model = models[i]
                if (context.packageManager
                        .getLaunchIntentForPackage(model.packageName) != null
                ) {
                    preAppData.add(
                        AppData(
                            model.loadLabel(packageManager),
                            model.packageName,
                            model.loadIcon(packageManager)
                        )
                    )
                }
            }
            adapter?.apply {
                clear()
                add(preAppData)
                notifyDataSetChanged()
            }

            isLoading = false
        }
    }

    override fun getAdapter(): AppListAdapter? {
        return super.getAdapter() as AppListAdapter
    }
}

