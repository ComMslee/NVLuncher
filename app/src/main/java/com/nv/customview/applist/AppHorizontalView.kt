package com.nv.customview.applist

import android.content.Context
import android.content.pm.PackageManager
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nv.lutil.listener.OnItemClickListener
import gg.op.agro.decoration.SpacesHorizontalItemDecoration
import gg.op.agro.util.Util
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction0
import kotlin.reflect.KFunction2


class AppHorizontalView : RecyclerView {
    var onItemClickListener: KFunction2<Int, AppData?, Unit>? = null
    var onEmptyListener: KFunction0<Unit>? = null
    lateinit var packageManager: PackageManager

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
        var appListAdapter = AppListAdapter(1)
        appListAdapter.onItemClickListener = OnItemClickListener { position, model ->
            onItemClickListener?.let { it(position, model) }
        }

        adapter = appListAdapter
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

        addItemDecoration(
            SpacesHorizontalItemDecoration(
                Util.convertDpToPixel(8f, context),
                Util.convertDpToPixel(16f, context)
            )
        )
    }

    public fun load(packageManager: PackageManager) {
        this.packageManager = packageManager

        GlobalScope.launch {
            adapter?.clear()
            var models = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            for (i in 0 until models.size) {
//        for(i in 0..30){
                var model = models[i]
                if (context.getPackageManager()
                        .getLaunchIntentForPackage(model.packageName) != null
                ) {
                    adapter?.add(
                        AppData(
                            model.loadLabel(packageManager),
                            model.packageName,
                            model.loadIcon(packageManager)
                        )
                    )
                }
                if (i % 10 == 0) {
                    post {
                        adapter?.notifyDataSetChanged()
                    }
                }
            }
            post {
                adapter?.notifyDataSetChanged()

            }

        }

    }


    override fun getAdapter(): AppListAdapter? {
        return super.getAdapter() as AppListAdapter
    }
}

