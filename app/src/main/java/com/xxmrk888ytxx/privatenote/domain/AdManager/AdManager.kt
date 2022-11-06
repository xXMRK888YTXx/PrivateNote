package com.xxmrk888ytxx.privatenote.domain.AdManager

import kotlinx.coroutines.flow.Flow

interface AdManager {
    fun isNeedShowAds() : Flow<Boolean>
    fun disableAd()
    fun enableAd()
}