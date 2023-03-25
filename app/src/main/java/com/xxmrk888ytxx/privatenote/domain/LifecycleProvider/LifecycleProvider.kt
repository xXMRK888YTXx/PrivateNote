package com.xxmrk888ytxx.privatenote.domain.LifecycleProvider

import com.xxmrk888ytxx.privatenote.Utils.LifeCycleState
import kotlinx.coroutines.flow.Flow

interface LifecycleProvider {
    val currentState: Flow<LifeCycleState>
}