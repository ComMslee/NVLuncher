package com.nv.customview.applist

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import com.nv.nvluncher.R
import gg.op.agro.util.Util
import kotlin.reflect.KFunction0
import kotlin.reflect.KFunction2

class LogoListView : HorizontalScrollView {
    var onItemClickListener: KFunction2<Int, Drawable?, Unit>? = null
    lateinit var linearLayout: LinearLayout

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
        linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.HORIZONTAL
        linearLayout.minimumHeight = Util.convertDpToPixel(100f, context)

        addView(
            linearLayout,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        )
        load()
    }

    private fun load() {
        linearLayout.removeAllViews()

        var drawables = context.resources.obtainTypedArray(R.array.logos)
        for (i in 0..drawables.length()) {
            var drawable = drawables.getDrawable(i)
            if (drawable != null) {
                var imageView = ImageView(context)
                imageView.setImageDrawable(drawable)
                imageView.setOnClickListener {
                    onItemClickListener?.let { it(i, drawable) }
                }
                linearLayout.addView(
                    imageView,
                    LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
                )
            }
        }
    }
}

