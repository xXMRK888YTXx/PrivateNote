package com.xxmrk888ytxx.privatenote.Utils.AnalyticsManager

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

class AnalyticsManagerImpl constructor(
    private val analytics: FirebaseAnalytics
) : AnalyticsManager
{
    override fun sendEvent(eventName: String, params: Bundle?) {
        analytics.logEvent(eventName,params)
    }

}