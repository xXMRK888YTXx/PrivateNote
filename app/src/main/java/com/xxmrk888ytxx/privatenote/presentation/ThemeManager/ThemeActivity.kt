package com.xxmrk888ytxx.privatenote.presentation.ThemeManager

import android.app.Activity
import android.content.res.Configuration

interface ThemeActivity {
    fun notifyAppThemeChanged(activity: Activity,themeId:Int) {
        activity.apply {
            if(themeId == ThemeManager.SYSTEM_THEME) {
                when (resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_YES -> {
                        ThemeManager.setupTheme(ThemeManager.BLACK_THEME)
                        setTheme(ThemeManager.systemThemeId)
                    }
                    Configuration.UI_MODE_NIGHT_NO -> {
                        ThemeManager.setupTheme(ThemeManager.WHITE_THEME)
                        setTheme(ThemeManager.systemThemeId)
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        ThemeManager.setupTheme(ThemeManager.BLACK_THEME)
                        setTheme(ThemeManager.systemThemeId)
                    }
                }
            }else {
                ThemeManager.setupTheme(themeId)
                setTheme(ThemeManager.systemThemeId)
            }
        }
    }
}