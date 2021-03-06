package com.nv.lutil.util

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.util.Patterns
import com.nv.customview.applist.AppData
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


object Util {
    const val DEBUG = false

    fun getYouTubeId(youTubeUrl: String): String? {
        val pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*"
        val compiledPattern = Pattern.compile(pattern)
        val matcher = compiledPattern.matcher(youTubeUrl)
        return if (matcher.find()) {
            matcher.group()
        } else {
            "error"
        }
    }

    fun chkPw(str: String): Boolean {
        var isNum = false
        var isCh = false
        var isSp = false
        val sps = charArrayOf('!', '@', '#', '$', '%', '^', '&', '*', '_', '-')
        str.forEach { element ->
            if (element in '0'..'9') {
                isNum = true
            } else if (element in 'a'..'z' || element in 'A'..'Z') {
                isCh = true
            } else {
                isSp = true
                //                var canSp = false
                //                for (j in sps.indices) {
                //                    if (c == sps[j]) {
                //                        canSp = true
                //                    }
                //                }
                //                if (!canSp) {
                //                    return false
                //                }
            }
        }
        return isNum && isCh && isSp
    }

    fun isEmail(email: String): Boolean {
        val matcher = Patterns.EMAIL_ADDRESS.matcher(email)

        return matcher.matches()
    }

    fun getRealPathFromURI(
        context: Context,
        contentUri: Uri
    ): String? {
        var cursor: Cursor? = null
        return try {
            val proj =
                arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } catch (e: Exception) {
            ""
        } finally {
            cursor?.close()
        }
    }

    fun getPackageName(context: Context): String {
        return context.packageName
    }

    fun iso8601toDate(strDate: String): Date {
        val df1 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS")
        return df1.parse(strDate)
    }


    fun formatMoney(money: Int?): String {
        if (money == null) return "0"
        return String.format("%,d", money)
    }

    fun formatMoneyWon(money: Int): String {
        if (money == 0) return "??????"
        val r = rtn1(money.toLong(), 0)
        val arr = r.split(",".toRegex()).toTypedArray()
        val sb = StringBuilder()
        for (i in arr.indices) {
            sb.append(cal(arr[i]))
            when(arr.size - i) {
                4 -> {
                    sb.append("???")
                }
                3 -> {
                    sb.append("???")
                }
                2 -> {
                    sb.append("???")
                }
                1 -> {
                    sb.append("???")
                }
            }
        }
        return sb.toString()
    }

    fun rtn1(d: Long, b: Int): String {
        var c: String? = "##,####"
        if (b != 0) {
            c += ","
        }
        for (i in 0 until b) {
            c += "#"
        }
        val df = DecimalFormat(c)
        return df.format(d)
    }

    private fun cal(s: String): String {
        val c = s.toCharArray()
        val l = c.size
        var r = ""
        for (i in c.indices) {
            when(l - i) {
                4 -> {
                    when (c[i]) {
                        '1' -> r += "??????"
                        '2' -> r += "??????"
                        '3' -> r += "??????"
                        '4' -> r += "??????"
                        '5' -> r += "??????"
                        '6' -> r += "??????"
                        '7' -> r += "??????"
                        '8' -> r += "??????"
                        '9' -> r += "??????"
                        '0' -> r += ""
                    }
                }
                3 -> {
                    when (c[i]) {
                        '1' -> r += "??????"
                        '2' -> r += "??????"
                        '3' -> r += "??????"
                        '4' -> r += "??????"
                        '5' -> r += "??????"
                        '6' -> r += "??????"
                        '7' -> r += "??????"
                        '8' -> r += "??????"
                        '9' -> r += "??????"
                        '0' -> r += ""
                    }
                }
                2 -> {
                    when (c[i]) {
                        '1' -> r += "??????"
                        '2' -> r += "??????"
                        '3' -> r += "??????"
                        '4' -> r += "??????"
                        '5' -> r += "??????"
                        '6' -> r += "??????"
                        '7' -> r += "??????"
                        '8' -> r += "??????"
                        '9' -> r += "??????"
                        '0' -> r += ""
                    }
                }
                1 -> {
                    when (c[i]) {
                        '1' -> r += "???"
                        '2' -> r += "???"
                        '3' -> r += "???"
                        '4' -> r += "???"
                        '5' -> r += "???"
                        '6' -> r += "???"
                        '7' -> r += "???"
                        '8' -> r += "???"
                        '9' -> r += "???"
                        '0' -> r += ""
                    }
                }
            }
        }
        return r
    }

    fun convertDpToPixel(dp: Float, context: Context?): Int {
        if (context == null) return 0
        val resources = context.resources
        val metrics = resources.displayMetrics
        val px =
            dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        return px.toInt()
    }

    // ??????????????? ??????
    fun getSharedPreferences(context: Context): SharedPreferences? {
        return context.getSharedPreferences(
            SharedPreferencesKeys.PRE,
            Context.MODE_PRIVATE
        )
    }

    fun getEditor(context: Context): SharedPreferences.Editor? {
        val pref = getSharedPreferences(context)
        return pref?.edit()
    }

    fun appList(packageManager: PackageManager): ArrayList<AppData> {
        val apps: ArrayList<AppData> = ArrayList()

        val models = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        for (i in 0 until models.size) {
            val model = models[i]
            if (packageManager.getLaunchIntentForPackage(model.packageName) != null) {
                if (DEBUG) {
                    Log.e("mslee", "packageName ${model.packageName}") //mslee add log
                }
                if (model.packageName == "com.nv.nvluncher"
                    || model.packageName == "com.google.android.inputmethod.latin"
                    || model.packageName == "org.chromium.webview_shell"
                ) {
                    continue
                }
                apps.add(
                    AppData(
                        model.loadLabel(packageManager),
                        model.packageName,
                        model.loadIcon(packageManager)
                    )
                )
            }
        }

        return apps
    }
}