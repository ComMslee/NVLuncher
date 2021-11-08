package com.nv.lutil.util

import android.content.Context
import gg.op.agro.util.Util

object SettingUtil {

    fun isAutoPlay(context: Context): Boolean {
        return Util.getSharedPreferences(context)
            ?.getBoolean(SharedPreferencesKeys.IS_AUTO_PLAY, false)!!
    }

    fun setAutoPlay(context: Context, isChecked: Boolean) {
        var editor = Util.getEditor(context)
        editor?.putBoolean(SharedPreferencesKeys.IS_AUTO_PLAY, isChecked)
        editor?.commit()
    }
}