package com.xxmrk888ytxx.privatenote.domain.DeepLinkController

interface DeepLinkController {
    fun isDeepLinkAlreadyRegister(deepLinkId:Int) : Boolean
    fun registerDeepLink(deepLink: DeepLink)
    fun getDeepLink() : DeepLink?
    fun markInvalidDeepLink(deepLink: Int)
}