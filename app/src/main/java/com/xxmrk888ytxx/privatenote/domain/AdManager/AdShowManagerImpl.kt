package com.xxmrk888ytxx.privatenote.domain.AdManager

import com.xxmrk888ytxx.privatenote.Utils.CoroutineScopes.ApplicationScope
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsRepository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


class AdShowManagerImpl @Inject constructor(
    private val settingsRepository: SettingsRepository
) : AdShowManager {
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