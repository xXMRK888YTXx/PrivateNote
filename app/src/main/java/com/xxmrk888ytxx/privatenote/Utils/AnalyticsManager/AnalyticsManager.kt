package com.xxmrk888ytxx.privatenote.Utils.AnalyticsManager

import android.os.Bundle

interface AnalyticsManager {
    fun sendEvent(eventName:String,params:Bundle?)
}