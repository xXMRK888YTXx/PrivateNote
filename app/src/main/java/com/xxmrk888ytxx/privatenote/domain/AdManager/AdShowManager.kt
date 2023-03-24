package com.xxmrk888ytxx.privatenote.domain.AdManager

import kotlinx.coroutines.flow.Flow

interface AdShowManager {
    fun isNeedShowAds() : Flow<Boolean>
    fun disableAd()
    fun enableAd()
}