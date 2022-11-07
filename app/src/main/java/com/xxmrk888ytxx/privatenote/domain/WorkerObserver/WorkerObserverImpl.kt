package com.xxmrk888ytxx.privatenote.domain.WorkerObserver

import androidx.work.Operation
import com.xxmrk888ytxx.privatenote.Utils.asyncIfNotNull
import com.xxmrk888ytxx.privatenote.Utils.ifNotNull
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkerObserverImpl @Inject constructor(

) : WorkerObserver {
    private val observers:MutableMap<Int,MutableStateFlow<WorkerObserver.Companion.WorkerState?>?> = mutableMapOf()
    override suspend fun registerObserver(observerId: Int) : StateFlow<WorkerObserver.Companion.WorkerState?>  {
        unRegisterObserver(observerId)
        val observer:MutableStateFlow<WorkerObserver.Companion.WorkerState?>
        = MutableStateFlow(WorkerObserver.Companion.WorkerState.IN_PROGRESS)
        observers[observerId] = observer
        return observer
    }

    override suspend fun changeWorkerState(observerId: Int, state:WorkerObserver.Companion.WorkerState) {
        observers[observerId].asyncIfNotNull {
            it.emit(state)
        }
    }


    override suspend fun unRegisterObserver(observerId: Int) {
        if(observers[observerId] == null) return
        observers[observerId]?.emit(WorkerObserver.Companion.WorkerState.FAILURE)
        observers[observerId] = null
    }

    override suspend fun unRegisterAll() {
        observers.forEach {
            unRegisterObserver(it.key)
        }
    }
}