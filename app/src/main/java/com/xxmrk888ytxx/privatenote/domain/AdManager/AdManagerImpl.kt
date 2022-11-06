package com.xxmrk888ytxx.privatenote.domain.AdManager

import com.xxmrk888ytxx.privatenote.Utils.CoroutineScopes.ApplicationScope
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsRepository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


class AdManagerImpl @Inject constructor(
    private val settingsRepository: SettingsRepository
) : AdManager {
    override fun isNeedShowAds(): Flow<Boolean> = settingsRepository.getAdState()


    override fun disableAd() {
        ApplicationScope.launch {
            settingsRepository.setAdState(false)
        }
    }

    override fun enableAd() {
        ApplicationScope.launch {
            settingsRepository.setAdState(true)
        }
    }

}