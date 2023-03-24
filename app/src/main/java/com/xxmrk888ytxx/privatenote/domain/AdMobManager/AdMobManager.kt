package com.xxmrk888ytxx.privatenote.domain.AdMobManager

import android.app.Activity

interface AdMobManager {
    fun initAdmob()

    fun interstitialAds(activity: Activity)
}