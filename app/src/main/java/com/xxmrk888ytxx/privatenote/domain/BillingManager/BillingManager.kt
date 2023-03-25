package com.xxmrk888ytxx.privatenote.domain.BillingManager

import android.app.Activity

interface BillingManager {

    fun connectToGooglePlay()

    fun handlingPendingTransactions()

    fun bueDisableAds(activity: Activity)

    val isDisableAdsAvailable:Boolean
}