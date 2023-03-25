package com.xxmrk888ytxx.privatenote.domain.LifecycleProvider

import com.xxmrk888ytxx.privatenote.Utils.CoroutineScopes.ApplicationScope
import com.xxmrk888ytxx.privatenote.Utils.LifeCycleState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LifeCycleManager @Inject constructor() : LifecycleProvider,LifeCycleNotifier {

    private val _currentState:MutableStateFlow<LifeCycleState> = MutableStateFlow(LifeCycleState.OnResume)

    override val currentState: Flow<LifeCycleState>
        get() = _currentState.asStateFlow()

    override fun onStateChanged(state: LifeCycleState) {
        ApplicationScope.launch { _currentState.value = state }
    }
}