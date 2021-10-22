package com.nv.customview.applist

import android.graphics.drawable.Drawable

data class AppData (
    var name : CharSequence,
    var packageName : String,
    var thumb : Drawable
)