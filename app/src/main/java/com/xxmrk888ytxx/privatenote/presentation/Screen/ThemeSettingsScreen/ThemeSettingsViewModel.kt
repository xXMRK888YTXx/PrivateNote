package com.xxmrk888ytxx.privatenote.presentation.Screen.ThemeSettingsScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xxmrk888ytxx.privatenote.Utils.getData
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsRepository.SettingsRepository
import com.xxmrk888ytxx.privatenote.presentation.Activity.MainActivity.ActivityController
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager.SYSTEM_THEME
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeSettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private var activityController:ActivityController? = null
    fun getCurrentApplicationThemeId() = settingsRepository.getApplicationThemeId()

    fun updateApplicationTheme(themeId:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.setApplicationThemeId(themeId)
            activityController?.notifyAppThemeChanged()
        }
    }

    fun initActivityController(activityController: ActivityController) {
        this.activityController = activityController
    }
}