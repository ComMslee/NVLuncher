package com.nv.customview.applist

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nv.lutil.listener.OnItemClickListener
import com.nv.nvluncher.R
import gg.op.agro.decoration.SpacesHorizontalItemDecoration
import gg.op.agro.util.Util
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction0
import kotlin.reflect.KFunction2


class LogoListView : HorizontalScrollView {
    var onItemClickListener : KFunction2<Int, Drawable?, Unit>? = null
    var onEmptyListener : KFunction0<Unit>? = null
    lateinit var packageManager : PackageManager
    lateinit var linearLayout : LinearLayout

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
        linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.HORIZONTAL
        linearLayout.minimumHeight=Util.convertDpToPixel(100f,context)

        addView(linearLayout,LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT))

        load()
    }

    private fun load(){
        linearLayout.removeAllViews()

        var drawables = context.resources.obtainTypedArray(R.array.logos)
        for ( i in 0 .. drawables.length()){
            var drawable = drawables.getDrawable(i)
            if(drawable!=null){
                var imageView = ImageView(context)
                imageView.setImageDrawable(drawable)
                imageView.setOnClickListener {
                    onItemClickListener?.let { it(i,drawable) }
                }
                linearLayout.addView(imageView,
                    LayoutParams(LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT)
                )

            }
        }


    }

}

