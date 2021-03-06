package com.nv.customview.applist

import android.content.Context
import android.content.pm.PackageManager
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nv.lutil.listener.OnItemClickListener
import com.nv.customview.decoration.SpacesHorizontalItemDecoration
import com.nv.lutil.util.Util
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction2

class AppHorizontalView : RecyclerView {
    var onItemClickListener: KFunction2<Int, AppData?, Unit>? = null
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

    private fun init() {
        val appListAdapter = AppListAdapter(1)
        appListAdapter.onItemClickListener = object : OnItemClickListener<AppData> {
            override fun onItemClick(position: Int, model: AppData) {
                onItemClickListener?.let { it(position, model) }
            }

            override fun onItemLongClick(position: Int, model: AppData): Boolean {
                return false
            }
        }

        adapter = appListAdapter
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        addItemDecoration(
            SpacesHorizontalItemDecoration(
                Util.convertDpToPixel(8f, context),
                Util.convertDpToPixel(16f, context)
            )
        )
    }

    fun load(packageManager: PackageManager) {
        this.packageManager = packageManager

        GlobalScope.launch {
            adapter?.let {
                it.clear()

                val applist = Util.appList(packageManager)
                applist.forEachIndexed { index, appData ->
                    it.add(appData)
                    if (index % 10 == 0) {
                        post {
                            it.notifyDataSetChanged()
                        }
                    }
                }
                post {
                    it.notifyDataSetChanged()
                }
            }
        }
    }

    override fun getAdapter(): AppListAdapter? {
        return super.getAdapter() as AppListAdapter
    }
}

