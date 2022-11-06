package com.xxmrk888ytxx.privatenote.domain.BillingManager

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.android.billingclient.api.*
import com.xxmrk888ytxx.privatenote.Utils.CoroutineScopes.ApplicationScope
import com.xxmrk888ytxx.privatenote.Utils.toState
import com.xxmrk888ytxx.privatenote.domain.AdManager.AdManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject


class BillingManager @Inject constructor(
    @ApplicationContext context: Context,
    private val adManager: AdManager,
) {
    private var removeAdProduct:ProductDetails? = null

    private val purchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            verifyPurchase(purchases)
        }

    private fun verifyPurchase(purchases: List<Purchase>?) {
        purchases?.forEach {
            val acknowledgePurchaseParams = AcknowledgePurchaseParams
                .newBuilder()
                .setPurchaseToken(it.purchaseToken)
                .build()

            billingClient.acknowledgePurchase(acknowledgePurchaseParams
            ) { billingResult: BillingResult ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    adManager.disableAd()
                }
            }
        }
    }

    private val billingClient by lazy {
        BillingClient.newBuilder(context)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()
    }

    fun connectToGooglePlay() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode ==  BillingClient.BillingResponseCode.OK) {
                    ApplicationScope.launch { getProducts() }
                }
            }
            override fun onBillingServiceDisconnected() {
                connectToGooglePlay()
            }
        })
    }

    private suspend fun getProducts() {
        val products = listOf(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(removeAdsId)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        )
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(products)
            .build()
        billingClient.queryProductDetailsAsync(params
        ) {billingResult, prodDetailsList ->
            if(prodDetailsList.isEmpty()) return@queryProductDetailsAsync
            prodDetailsList.forEach {
                when(it.productId) {
                    removeAdsId -> {
                        removeAdProduct = it
                    }
                    else -> {
                        Log.d("MyLog","Not Handler for picchered ${it.productId}")
                    }
                }
            }
        }
    }

    fun handlingPendingTransactions() {
        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.INAPP).build()
        ) { billingResult: BillingResult, list: List<Purchase> ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                for (purchase in list) {
                    if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                        verifyPurchase(listOf(purchase))
                    }
                }
            }
        }
    }

    fun bueDisableAds(activity: Activity) {
        val product = removeAdProduct ?: return
        val productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                // retrieve a value for "productDetails" by calling queryProductDetailsAsync()
                .setProductDetails(product)
                .build()
        )

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()

        val billingResult = billingClient.launchBillingFlow(activity, billingFlowParams)

    }

    val isDisableAdsAvailable: Boolean
        get() {
            return removeAdProduct != null
        }

    private val removeAdsId = "disable_ads"

}