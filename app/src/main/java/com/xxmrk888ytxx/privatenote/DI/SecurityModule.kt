package com.xxmrk888ytxx.privatenote.DI

import androidx.compose.ui.tooling.preview.Preview
import com.xxmrk888ytxx.privatenote.SecurityUtils.SecurityUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class SecurityModule {
    @Provides
    fun getSecurityUtils() = SecurityUtils()
}