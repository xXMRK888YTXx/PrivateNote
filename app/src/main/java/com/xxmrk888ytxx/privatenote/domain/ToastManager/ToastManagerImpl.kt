package com.xxmrk888ytxx.privatenote.domain.ToastManager

import android.content.Context
import android.content.res.Configuration
import android.widget.Toast
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.ShowToast_Event
import com.xxmrk888ytxx.privatenote.domain.AnalyticsManager.AnalyticsManager
import com.xxmrk888ytxx.privatenote.Utils.SendAnalytics
import com.xxmrk888ytxx.privatenote.Utils.getData
import com.xxmrk888ytxx.privatenote.Utils.runOnMainThread
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsRepository.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


@SendAnalytics
class ToastManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val analytics: AnalyticsManager
) : ToastManager {

    override fun showToast(text:String) {
        analytics.sendEvent(ShowToast_Event,null)
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show()
    }
    override fun showToast(resourceId:Int) {
        analytics.sendEvent(ShowToast_Event,null)
        Toast.makeText(context,context.getString(resourceId),Toast.LENGTH_SHORT).show()
    }

    override fun showToast(stringBuilder: (context: Context) -> String) {
        analytics.sendEvent(ShowToast_Event,null)
        showToast(stringBuilder(context))
    }
}