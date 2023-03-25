package com.xxmrk888ytxx.privatenote.domain.AnalyticsManager

import android.os.Bundle

interface AnalyticsManager {
    fun sendEvent(eventName:String,params:Bundle?)
}