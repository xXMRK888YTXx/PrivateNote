package com.xxmrk888ytxx.privatenote.presentation.MultiUse.AdMobBanner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.xxmrk888ytxx.privatenote.presentation.ThemeManager.ThemeManager

@Composable
fun AdMobBanner(bannerKey:String,background: Color = ThemeManager.MainBackGroundColor) {
    AndroidView(
        modifier = Modifier.fillMaxWidth().background(background),
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                adUnitId = bannerKey
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}