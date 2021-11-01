package gg.op.agro.util

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
        for (i in 0 until str.length) {
            val c = str[i]
            if (c in '0'..'9') {
                isNum = true
            } else if (c in 'a'..'z' || c in 'A'..'Z') {
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
        var matcher = Patterns.EMAIL_ADDRESS.matcher(email)

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
        if (money == 0) return "영원"
        val r = rtn1(money.toLong(), 0)
        val arr = r.split(",".toRegex()).toTypedArray()
        val sb = StringBuilder()
        for (i in arr.indices) {
            sb.append(cal(arr[i]))
            if (arr.size - i == 4) {
                sb.append("조")
            } else if (arr.size - i == 3) {
                sb.append("억")
            } else if (arr.size - i == 2) {
                sb.append("만")
            } else if (arr.size - i == 1) {
                sb.append("원")
            }
        }
        return sb.toString()
    }

    fun rtn1(d: Long, b: Int): String {
        var c: String? = "##,####"
        if (b != 0) {
            c += ","
            c = c
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
            if (l - i == 4) {
                when (c[i]) {
                    '1' -> r += "일천"
                    '2' -> r += "이천"
                    '3' -> r += "삼천"
                    '4' -> r += "사천"
                    '5' -> r += "오천"
                    '6' -> r += "육천"
                    '7' -> r += "칠천"
                    '8' -> r += "팔천"
                    '9' -> r += "구천"
                    '0' -> r += ""
                }
            } else if (l - i == 3) {
                when (c[i]) {
                    '1' -> r += "일백"
                    '2' -> r += "이백"
                    '3' -> r += "삼백"
                    '4' -> r += "사백"
                    '5' -> r += "오백"
                    '6' -> r += "육백"
                    '7' -> r += "칠백"
                    '8' -> r += "팔백"
                    '9' -> r += "구백"
                    '0' -> r += ""
                }
            } else if (l - i == 2) {
                when (c[i]) {
                    '1' -> r += "일십"
                    '2' -> r += "이십"
                    '3' -> r += "삼십"
                    '4' -> r += "사십"
                    '5' -> r += "오십"
                    '6' -> r += "육십"
                    '7' -> r += "칠십"
                    '8' -> r += "팔십"
                    '9' -> r += "구십"
                    '0' -> r += ""
                }
            } else if (l - i == 1) {
                when (c[i]) {
                    '1' -> r += "일"
                    '2' -> r += "이"
                    '3' -> r += "삼"
                    '4' -> r += "사"
                    '5' -> r += "오"
                    '6' -> r += "육"
                    '7' -> r += "칠"
                    '8' -> r += "팔"
                    '9' -> r += "구"
                    '0' -> r += ""
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

    // 프리퍼런스 접근
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
            var model = models[i]
            if (packageManager.getLaunchIntentForPackage(model.packageName) != null) {
                Log.e("mslee", "packageName ${model.packageName}"); //mslee add log
                if(model.packageName == "com.nv.nvluncher") {
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

        return apps;
    }
}