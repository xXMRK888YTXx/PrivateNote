package com.xxmrk888ytxx.privatenote.domain.WorkerObserver

import com.xxmrk888ytxx.privatenote.domain.MainDispatcherRule
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.coroutines.CoroutineContext

class WorkerObserverTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    lateinit var workerObserver:WorkerObserver
    @Before
    fun init() {
        workerObserver = WorkerObserverImpl()
    }
    private object TestObserverScope : CoroutineScope {
        override val coroutineContext: CoroutineContext = SupervisorJob()
    }
    private var testCounter = 0

    private fun upTestCounter() {
        synchronized(true) {
            testCounter += 1
        }
    }

    @After
    fun clear() {
        TestObserverScope.coroutineContext.cancelChildren()
        testCounter = 0
    }

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun test_registerObserver_expect_ObserverIs_register() = runTest {
        val id = 1

        val observer =  workerObserver.registerObserver(id)

        Assert.assertEquals(observer.value,WorkerObserver.Companion.WorkerState.IN_PROGRESS)
    }

    @Test
    fun test_change_observer_state_expect_is_complete_state() = runTest {
        val id = 1
        val observer =  workerObserver.registerObserver(id)
        Assert.assertEquals(observer.value,WorkerObserver.Companion.WorkerState.IN_PROGRESS)

        workerObserver.changeWorkerState(id,WorkerObserver.Companion.WorkerState.SUCCESS)

        Assert.assertEquals(WorkerObserver.Companion.WorkerState.SUCCESS,observer.value)
    }

    @Test
    fun test_changeWorkerState_change_observer_state_expect_is_failed_state() = runTest {
        val id = 1
        val observer =  workerObserver.registerObserver(id)
        Assert.assertEquals(observer.value,WorkerObserver.Companion.WorkerState.IN_PROGRESS)

        workerObserver.changeWorkerState(id,WorkerObserver.Companion.WorkerState.FAILURE)

        Assert.assertEquals(WorkerObserver.Companion.WorkerState.FAILURE,observer.value)
    }

    @Test
    fun tes_unRegisterObserver_unregister_observer_expect_send_failed_state() = runTest {
        val id = 1
        val observer =  workerObserver.registerObserver(id)
        Assert.assertEquals(observer.value,WorkerObserver.Companion.WorkerState.IN_PROGRESS)

        workerObserver.unRegisterObserver(id)

        Assert.assertEquals(WorkerObserver.Companion.WorkerState.FAILURE,observer.value)
    }

    @Test
    fun test_unRegisterObserver_unregister_observer_expect_change_state_not_does_exist() = runTest {
        val id = 1
        val observer =  workerObserver.registerObserver(id)
        Assert.assertEquals(observer.value,WorkerObserver.Companion.WorkerState.IN_PROGRESS)

        workerObserver.unRegisterObserver(id)
        workerObserver.changeWorkerState(id,WorkerObserver.Companion.WorkerState.SUCCESS)

        Assert.assertEquals(WorkerObserver.Companion.WorkerState.FAILURE,observer.value)
    }

    @Test
    fun test_cancelChildren_Method_By_CoroutinesScope_if_collect_expect_cancel_collect() = runTest {
        val id = 1
        val observer = workerObserver.registerObserver(id)
        var testPass = true

        TestObserverScope.launch {
            observer.collect {
                if(it is WorkerObserver.Companion.WorkerState.SUCCESS) {
                    testPass = false
                }
            }
        }
        delay(100)
        TestObserverScope.coroutineContext.cancelChildren()
        workerObserver.changeWorkerState(id,WorkerObserver.Companion.WorkerState.SUCCESS)
        delay(100)
        Assert.assertEquals(testPass,true)
    }

}