package com.xxmrk888ytxx.privatenote.DI

import android.content.Context
import com.xxmrk888ytxx.privatenote.LifeCycleState
import com.xxmrk888ytxx.privatenote.Utils.ShowToast
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UtilsModule {
    @Provides
    fun getShowToast(@ApplicationContext context: Context) : ShowToast {
        return ShowToast(context)
    }

    @Provides
    @Singleton
    fun getLifeCycleState() : MutableStateFlow<LifeCycleState> {
        return MutableStateFlow(LifeCycleState.onResume)
    }
}