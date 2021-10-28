package com.nv.nvluncher

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import java.text.SimpleDateFormat
import java.util.*

/**
 * Implementation of App Widget functionality.
 */
class NVClock : AppWidgetProvider() {

    private val WIDGET_UPDATE_INTERVAL = 1000
    private var mSender: PendingIntent? = null
    private var mManager: AlarmManager? = null
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetManager.getAppWidgetIds(ComponentName(context, javaClass))) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        val action = intent!!.action
        // 위젯 업데이트 인텐트를 수신했을 때
        // 위젯 업데이트 인텐트를 수신했을 때
        if (action == "android.appwidget.action.APPWIDGET_UPDATE") {
            removePreviousAlarm()
            val firstTime =
                System.currentTimeMillis() + WIDGET_UPDATE_INTERVAL
            mSender = PendingIntent.getBroadcast(context, 0, intent, 0)
            mManager = context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            mManager!![AlarmManager.RTC, firstTime] = mSender
        } else if (action == "android.appwidget.action.APPWIDGET_DISABLED") {
            removePreviousAlarm()
        }
    }


    fun removePreviousAlarm() {
        if (mManager != null && mSender != null) {
            mSender!!.cancel()
            mManager!!.cancel(mSender)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created

    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    var c: Date = Calendar.getInstance().getTime()

    var df = SimpleDateFormat("aa hh:mm", Locale.getDefault())
    var formattedDate: String = df.format(c)
    // Construct the RemoteViews object
    var views = RemoteViews(context.packageName, R.layout.n_v_clock)
    views.setTextViewText(R.id.clock_tv_time, formattedDate)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}