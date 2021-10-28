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
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction0
import kotlin.reflect.KFunction2


class AppListView : RecyclerView {
    var onItemClickListener : KFunction2<Int, AppData?, Unit>? = null
    var onEmptyListener : KFunction0<Unit>? = null
    lateinit var packageManager : PackageManager
    var isLoading : Boolean =false

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
        var adapter = AppListAdapter()
        adapter.onItemClickListener = OnItemClickListener { position, model ->

            val launchIntent = context.packageManager
                .getLaunchIntentForPackage(model.packageName)
            context.startActivity(launchIntent)
            Toast.makeText(context, model.packageName, Toast.LENGTH_LONG)
                .show()
            Log.e("dddddd",model.packageName)

            if(adapter.itemCount==0){
                onEmptyListener?.let { it() }
            }

            onItemClickListener?.let { it(position,model) }
        }

//        adapter.add(MainActivity.preAppData)
        this.adapter = adapter



        layoutManager = GridLayoutManager(context, 6);

        addItemDecoration(SpacesGridItemDecoration(Util.convertDpToPixel(14f,context)))


    }

    public fun load(packageManager: PackageManager){
        this.packageManager = packageManager

        if(isLoading)return
        isLoading=true
        var preAppData : ArrayList<AppData> = ArrayList()
        if(MainActivity.preAppData!=null){
            preAppData = MainActivity.preAppData?:ArrayList()
            MainActivity.preAppData = null;
        }else {

            var models = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            for (i in 0 until models.size) {
//        for(i in 0..30){
                var model = models[i]
                if (context.getPackageManager()
                        .getLaunchIntentForPackage(model.packageName) != null
                ) {
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

        adapter?.clear()
        adapter?.add(preAppData)
        adapter?.notifyDataSetChanged()

        isLoading=false

    }

    public fun reload(packageManager: PackageManager){
        this.packageManager = packageManager

        if(isLoading)return
        isLoading=true
        var preAppData : ArrayList<AppData> = ArrayList()

        var models = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        for(i in 0 until models.size){
//        for(i in 0..30){
            var model = models[i]
            if(context.getPackageManager().getLaunchIntentForPackage(model.packageName) != null) {
                preAppData?.add(
                    AppData(
                        model.loadLabel(packageManager),
                        model.packageName,
                        model.loadIcon(packageManager)
                    )
                )
            }

        }
//
        adapter?.clear()
        adapter?.add(preAppData)
        adapter?.notifyDataSetChanged()


        isLoading=false

    }



    override fun getAdapter(): AppListAdapter? {
        return super.getAdapter() as AppListAdapter
    }
}

