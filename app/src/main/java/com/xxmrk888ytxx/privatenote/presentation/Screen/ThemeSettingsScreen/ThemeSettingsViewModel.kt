package com.xxmrk888ytxx.privatenote.presentation.Screen.ThemeSettingsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsRepository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeSettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    fun getCurrentApplicationThemeId() = settingsRepository.getApplicationThemeId()

    fun updateApplicationTheme(themeId:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.setApplicationThemeId(themeId)
        }
    }
}