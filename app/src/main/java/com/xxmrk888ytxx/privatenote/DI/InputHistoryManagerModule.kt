package com.xxmrk888ytxx.privatenote.DI

import com.xxmrk888ytxx.privatenote.domain.InputHistoryManager.InputHistoryManager
import com.xxmrk888ytxx.privatenote.domain.InputHistoryManager.InputHistoryManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class InputHistoryManagerModule {
    @Provides
    fun getInputHistoryManager() : InputHistoryManager {
        return InputHistoryManagerImpl()
    }
}