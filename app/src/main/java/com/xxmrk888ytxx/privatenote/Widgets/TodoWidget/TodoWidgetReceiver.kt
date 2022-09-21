package com.xxmrk888ytxx.privatenote.Widgets.TodoWidget

import android.content.Context
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.updateAll
import com.xxmrk888ytxx.privatenote.Utils.ifNotNull

class TodoWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = TodoWidget()
    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        context.ifNotNull {
            Log.d("MyLog","updated")
        }
    }


}