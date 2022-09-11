package com.xxmrk888ytxx.privatenote.Utils

import android.content.Context
import android.content.res.Configuration
import android.widget.Toast
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsRepository.SettingsRepository
import java.util.*
import javax.inject.Singleton

@Singleton
class ShowToastImpl(private val context: Context,
    settingsRepository: SettingsRepository
) : ShowToast {
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
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show()
    }
    override fun showToast(resourceId:Int) {
        Toast.makeText(context,context.getString(resourceId),Toast.LENGTH_SHORT).show()
    }

    override fun showToast(getString: (context: Context) -> String) {
        showToast(getString(context))
    }
}