package com.xxmrk888ytxx.privatenote.domain.DeepLinkController

import com.xxmrk888ytxx.privatenote.Utils.ifNotNull

class DeepLinkControllerImpl : DeepLinkController {
    private var currentDeepLink:DeepLink? = null
    override fun isDeepLinkAlreadyRegister(deepLinkId: Int): Boolean {
        if(currentDeepLink != null) {
            return currentDeepLink!!.idDeepLink == deepLinkId
        }else {
            return false
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