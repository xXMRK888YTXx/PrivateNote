package com.xxmrk888ytxx.privatenote.domain.presentation.Screen.MainScreen.ToDoScreen

import com.xxmrk888ytxx.privatenote.data.Database.Entity.NotifyTask
import com.xxmrk888ytxx.privatenote.data.Database.Entity.ToDoItem
import com.xxmrk888ytxx.privatenote.domain.MainDispatcherRule
import com.xxmrk888ytxx.privatenote.domain.NotifyTaskManager.NotifyTaskManager
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsRepository.SettingsRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.ToDoRepository.ToDoRepository
import com.xxmrk888ytxx.privatenote.domain.ToastManager.ToastManager
import com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.MainScreenController
import com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.ScreenState.ToDoScreen.ToDoScreenState
import com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.ScreenState.ToDoScreen.ToDoViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ToDoViewModelTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    lateinit var viewModel: ToDoViewModel
    private val toastManager = mockk<ToastManager>(relaxed = true)
    private val toDoRepository = mockk<ToDoRepository>(relaxed = true)
    private val notifyTaskManager = mockk<NotifyTaskManager>(relaxed = true)
    private val settingsRepository = mockk<SettingsRepository>(relaxed = true)
    @Before
    fun init() {
        viewModel = ToDoViewModel(toastManager,toDoRepository,notifyTaskManager,settingsRepository)
    }

    @Test
    fun `test setMainScreenController send not null controller expect setup floatButton settings`() {
        val mainScreenController: MainScreenController = mockk(relaxed = true)

        viewModel.setMainScreenController(mainScreenController)

        verifySequence {
            mainScreenController.setFloatButtonOnClickListener(any(),any())
        }
    }

    @Test
    fun `test showNotifyDialog if app can send alarms expect property's changed`() {
        Assert.assertEquals(false,viewModel.getNotifyDialogState().value)
        every { notifyTaskManager.isCanSendAlarms() } returns true
        viewModel.showNotifyDialog()

        Assert.assertEquals(true,viewModel.getNotifyDialogState().value)
    }

    @Test
    fun `test showNotifyDialog if app cant send alarms expect show RequestPermissionDialog`() {
        Assert.assertEquals(false,viewModel.getNotifyDialogState().value)
        every { notifyTaskManager.isCanSendAlarms() } returns false

        viewModel.showNotifyDialog()

        Assert.assertEquals(false,viewModel.getNotifyDialogState().value)
        Assert.assertEquals(true,viewModel.getRequestPermissionSendAlarmsDialog().value)
    }

    @Test
    fun `test toDefaultMode expect mode changed`() {
        Assert.assertEquals( ToDoScreenState.Default,viewModel.getScreenState().value)

        viewModel.toEditToDoState()
        Assert.assertEquals( ToDoScreenState.EditToDoDialog,viewModel.getScreenState().value)
        viewModel.toDefaultMode()

        Assert.assertEquals(ToDoScreenState.Default,viewModel.getScreenState().value)
    }

    @Test
    fun `test toEditToDoState if send null expect mode changed`() {
        Assert.assertEquals( ToDoScreenState.Default,viewModel.getScreenState().value)

        viewModel.toEditToDoState()

        Assert.assertEquals(ToDoScreenState.EditToDoDialog,viewModel.getScreenState().value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test toEditToDoState if send not null expect mode changed`() = runTest {
        val id = 54
        Assert.assertEquals( ToDoScreenState.Default,viewModel.getScreenState().value)
        every { notifyTaskManager.getNotifyTaskByTodoId(id) } returns flowOf(NotifyTask(id,id,true,5,false))

        viewModel.toEditToDoState(ToDoItem(id = id, todoText = "test", isImportant = false))
        delay(100)

        Assert.assertEquals(ToDoScreenState.EditToDoDialog,viewModel.getScreenState().value)
        verifySequence {
            notifyTaskManager.getNotifyTaskByTodoId(id)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test changeMarkStatus call this method expect call repositories methods for set true todo state`() = runTest {
        val id = 4
        val state = true

        viewModel.changeMarkStatus(state,id)
        delay(100)

        verifySequence {
            toDoRepository.changeMarkStatus(id,state)
            notifyTaskManager.cancelTask(id)
        }

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test changeMarkStatus call this method expect call repositories methods for set false todo state`() = runTest {
        val id = 4
        val state = false

        viewModel.changeMarkStatus(state,id)
        delay(100)

        verifySequence {
            toDoRepository.changeMarkStatus(id,state)
            notifyTaskManager.cancelTask(id)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test saveToDo expect invoke save repository method`() = runTest {
        every { toDoRepository.getAllToDo() } returns flowOf(listOf(ToDoItem(todoText = "test", isImportant = true)))
        every { notifyTaskManager.getNotifyTaskByTodoId(any()) } returns flowOf(null)

        viewModel.saveToDo()
        delay(100)

        verify(exactly = 1) {
            toDoRepository.insertToDo(allAny())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test removeToDo input id expect invoke repository's methods for delete`() = runTest {
        val id = 5

        viewModel.removeToDo(id)
        delay(100)

        verifySequence {
            toDoRepository.removeToDo(id)
            notifyTaskManager.cancelTask(id)
        }
    }

}