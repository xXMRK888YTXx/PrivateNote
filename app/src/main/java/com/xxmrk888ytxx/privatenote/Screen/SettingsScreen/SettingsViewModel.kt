package com.xxmrk888ytxx.privatenote.Screen.SettingsScreen

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Repositories.SettingsRepository.SettingsRepository
import com.xxmrk888ytxx.privatenote.Utils.Const.DEVELOPER_EMAIL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    fun leaveFromSettingScreen(navController: NavController) {
        navController.navigateUp()
    }

    fun getNavigationSwipeState() = settingsRepository.getNavigationSwipeState()

    fun changeNavigationSwipeState(state:Boolean) {
        viewModelScope.launch {
            settingsRepository.setNavigationSwipeState(state)
        }
    }

    fun openEmailClient(context: Context) {
        val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$DEVELOPER_EMAIL"))
        context.startActivity(Intent.createChooser(emailIntent, context.getString(R.string.Chose_client)))
    }
}