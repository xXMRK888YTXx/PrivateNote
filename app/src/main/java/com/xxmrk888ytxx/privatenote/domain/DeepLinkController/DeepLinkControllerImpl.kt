package com.xxmrk888ytxx.privatenote.domain.DeepLinkController

import com.xxmrk888ytxx.privatenote.Utils.ifNotNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeepLinkControllerImpl @Inject constructor() : DeepLinkController {
    private var currentDeepLink:DeepLink? = null
    override fun isDeepLinkAlreadyRegister(deepLinkId: Int): Boolean {
        return if(currentDeepLink != null) {
            currentDeepLink!!.idDeepLink == deepLinkId
        }else {
            false
        }
    }

    override fun registerDeepLink(deepLink: DeepLink) {
        if(currentDeepLink?.idDeepLink == deepLink.idDeepLink) return
        currentDeepLink = deepLink
    }

    override fun getDeepLink(): DeepLink? {
        if(currentDeepLink?.isActiveDeepLink == false) {
            return null
        }

        return currentDeepLink
    }


    override fun markInvalidDeepLink(deepLink: Int) {
        if(currentDeepLink == null) return

        if(currentDeepLink?.idDeepLink == deepLink) {
            currentDeepLink?.isActiveDeepLink = false
        }
    }


}