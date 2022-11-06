package com.xxmrk888ytxx.privatenote.presentation.Activity.MainActivity

import androidx.compose.runtime.State

interface BullingController {
    fun bueDisableAds()
    val isBillingAvailable: Boolean
}