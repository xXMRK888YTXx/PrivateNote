package com.xxmrk888ytxx.privatenote.domain.WorkerObserver

import androidx.work.Operation
import kotlinx.coroutines.flow.StateFlow

interface WorkerObserver {
    suspend fun registerObserver(observerId:Int) : StateFlow<WorkerState?>
    suspend fun changeWorkerState(observerId: Int,state:WorkerState)
    suspend fun unRegisterObserver(observerId: Int)
    suspend fun unRegisterAll()
    companion object {
        sealed class WorkerState {
            object IN_PROGRESS : WorkerState()
            object SUCCESS : WorkerState()
            object FAILURE : WorkerState()
        }
    }
}