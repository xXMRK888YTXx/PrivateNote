package com.xxmrk888ytxx.privatenote.domain.ToastManager

import android.content.Context
import android.content.res.Configuration
import android.widget.Toast
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsEvents.ShowToast_Event
import com.xxmrk888ytxx.privatenote.Utils.AnalyticsManager.AnalyticsManager
import com.xxmrk888ytxx.privatenote.Utils.LanguagesCodes
import com.xxmrk888ytxx.privatenote.Utils.SendAnalytics
import com.xxmrk888ytxx.privatenote.Utils.getData
import com.xxmrk888ytxx.privatenote.Utils.runOnMainThread
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsRepository.SettingsRepository
import java.util.*
import javax.inject.Singleton

@Singleton
@SendAnalytics
class ToastManagerImpl(
    private val context: Context,
    settingsRepository: SettingsRepository,
    private val analytics: AnalyticsManager
) : ToastManager {
    init {
        val languageCode = settingsRepository.getAppLanguage().getData()
        if(languageCode != LanguagesCodes.SYSTEM_LANGUAGE_CODE) {
            val locale = Locale(languageCode)
            Locale.setDefault(locale)
            val config = Configuration()
            config.locale = locale
            context.resources.updateConfiguration(
                config,
                context.resources.displayMetrics
            )
        }
    }

    override fun showToast(text:String) {
        analytics.sendEvent(ShowToast_Event,null)
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show()
    }
    override fun showToast(resourceId:Int) {
        analytics.sendEvent(ShowToast_Event,null)
        Toast.makeText(context,context.getString(resourceId),Toast.LENGTH_SHORT).show()
    }

    override fun showToast(stringBuild: (context: Context) -> String) {
        analytics.sendEvent(ShowToast_Event,null)
        showToast(stringBuild(context))
    }
}