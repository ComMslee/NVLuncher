package com.nv.customview.widget

import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetProviderInfo
import android.content.Context

class WidgetHost(context: Context?, hostId: Int) : AppWidgetHost(context, hostId) {
    override fun onCreateView(
        context: Context?,
        appWidgetId: Int,
        appWidget: AppWidgetProviderInfo?
    ): WidgetView {
        // pass back our custom AppWidgetHostView
        return WidgetView(context)
    }
}