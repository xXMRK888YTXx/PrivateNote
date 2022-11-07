package com.xxmrk888ytxx.privatenote.DI

import com.xxmrk888ytxx.privatenote.domain.SecurityUtils.SecurityUtils
import com.xxmrk888ytxx.privatenote.domain.SecurityUtils.SecurityUtilsImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface SecurityModule {
    @Binds
    fun bindsSecurityUtils(securityUtilsImpl:SecurityUtilsImpl) : SecurityUtils
}