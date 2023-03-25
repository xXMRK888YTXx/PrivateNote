package com.xxmrk888ytxx.privatenote.domain.AdMobManager

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.xxmrk888ytxx.privatenote.BuildConfig
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Utils.CoroutineScopes.ApplicationScope
import com.xxmrk888ytxx.privatenote.domain.AdManager.AdShowManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AdMobManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val adShowManager: AdShowManager
) : AdMobManager {
    override fun initAdmob() {
        ApplicationScope.launch(Dispatchers.Default) {
            MobileAds.initialize(context)
        }
    }

    override fun interstitialAds(activity: Activity) {
        ApplicationScope.launch {
            if(!adShowManager.isNeedShowAds().first()) return@launch

            val adRequest = AdRequest.Builder().build()

            withContext(Dispatchers.Main) {
                InterstitialAd.load(
                    context,
                    if (BuildConfig.DEBUG) context.getString(R.string.TestInterstitialAdsKey)
                    else context.getString(R.string.InterstitialAdsKey),
                    adRequest,
                    object : InterstitialAdLoadCallback() {
                        override fun onAdFailedToLoad(error: LoadAdError) {
                            super.onAdFailedToLoad(error)
                            Log.d("MyLog","InterstitialAd load error:${error.message}")
                        }

                        override fun onAdLoaded(ad: InterstitialAd) {
                            super.onAdLoaded(ad)
                            ad.show(activity)
                        }
                    }
                )
            }
        }
    }
}