package com.xxmrk888ytxx.privatenote.DI

import com.xxmrk888ytxx.privatenote.domain.BillingManager.BillingManager
import com.xxmrk888ytxx.privatenote.domain.BillingManager.BillingManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface BillingManagerModule {
    @Binds
    fun bindBillingManager(
        BillingManagerImpl:BillingManagerImpl
    ) : BillingManager
}