package com.xxmrk888ytxx.privatenote.DI

import androidx.compose.ui.tooling.preview.Preview
import com.xxmrk888ytxx.privatenote.SecurityUtils.SecurityUtils
import com.xxmrk888ytxx.privatenote.SecurityUtils.SecurityUtilsImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SecurityModule {
    @Provides
    fun getSecurityUtilsImpl() = SecurityUtilsImpl()

    @Provides
    @Singleton
    fun getSecurityUtils(securityUtilsImpl:SecurityUtilsImpl) : SecurityUtils {
        return securityUtilsImpl
    }
}