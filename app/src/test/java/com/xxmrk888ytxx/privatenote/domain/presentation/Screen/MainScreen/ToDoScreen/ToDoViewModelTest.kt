package com.xxmrk888ytxx.privatenote.domain.presentation.Screen.MainScreen.ToDoScreen

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.xxmrk888ytxx.privatenote.data.Database.Entity.NotifyTask
import com.xxmrk888ytxx.privatenote.data.Database.Entity.TodoItem
import com.xxmrk888ytxx.privatenote.domain.DeepLinkController.DeepLinkController
import com.xxmrk888ytxx.privatenote.domain.MainDispatcherRule
import com.xxmrk888ytxx.privatenote.domain.NotificationAppManager.NotificationAppManager
import com.xxmrk888ytxx.privatenote.domain.NotifyTaskManager.NotifyTaskManager
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsRepository.SettingsRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.ToDoRepository.TodoRepository
import com.xxmrk888ytxx.privatenote.domain.ToastManager.ToastManager
import com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.MainScreenController
import com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.ScreenState.ToDoScreen.ToDoScreenState
import com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.ScreenState.ToDoScreen.ToDoViewModel
import io.mockk.*
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
    private val toDoRepository = mockk<TodoRepository>(relaxed = true)
    private val notifyTaskManager = mockk<NotifyTaskManager>(relaxed = true)
    private val settingsRepository = mockk<SettingsRepository>(relaxed = true)
    private val notificationAppManager = mockk<NotificationAppManager>(relaxed = true)
    private val deepLinkController = mockk<DeepLinkController>(relaxed = true)

    @Before
    fun init() {
        viewModel = ToDoViewModel(
            toastManager,
            toDoRepository,
            notifyTaskManager,
            settingsRepository,
            notificationAppManager,
            deepLinkController,
            mockk(relaxed = true)
        )
    }

    @Test
    fun `test setMainScreenController send not null controller expect setup floatButton settings`() {
        val mainScreenController: MainScreenController = mockk(relaxed = true)

        viewModel.setMainScreenController(mainScreenController)

        verifySequence {
            mainScreenController.setFloatButtonOnClickListener(any(), any())
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    @Test
    fun `test showNotifyDialog if app can send alarms expect property's changed`() {
        Assert.assertEquals(false, viewModel.getNotifyDialogState().value)
        every { notifyTaskManager.isCanSendAlarms() } returns true
        every { notificationAppManager.isHavePostNotificationPermission() } returns true
        viewModel.showNotifyDialog(null)

        Assert.assertEquals(true, viewModel.getNotifyDialogState().value)
    }

    @OptIn(ExperimentalPermissionsApi::class)
    @Test
    fun `test showNotifyDialog if app cant send alarms expect show RequestPermissionDialog`() {
        Assert.assertEquals(false, viewModel.getNotifyDialogState().value)
        every { notifyTaskManager.isCanSendAlarms() } returns false
        every { notificationAppManager.isHavePostNotificationPermission() } returns true
        viewModel.showNotifyDialog(null)

        Assert.assertEquals(false, viewModel.getNotifyDialogState().value)
        Assert.assertEquals(true, viewModel.getRequestPermissionSendAlarmsDialog().value)
    }

    @Test
    fun `test toDefaultMode expect mode changed`() {
        Assert.assertEquals(ToDoScreenState.Default, viewModel.getScreenState().value)

        viewModel.toEditToDoState()
        Assert.assertEquals(ToDoScreenState.EditToDoDialog, viewModel.getScreenState().value)
        viewModel.toDefaultMode()

        Assert.assertEquals(ToDoScreenState.Default, viewModel.getScreenState().value)
    }

    @Test
    fun `test toEditToDoState if send null expect mode changed`() {
        Assert.assertEquals(ToDoScreenState.Default, viewModel.getScreenState().value)

        viewModel.toEditToDoState()

        Assert.assertEquals(ToDoScreenState.EditToDoDialog, viewModel.getScreenState().value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test toEditToDoState if send not null expect mode changed`() = runTest {
        val id = 54
        Assert.assertEquals(ToDoScreenState.Default, viewModel.getScreenState().value)
        every { notifyTaskManager.getNotifyTaskByTodoId(id) } returns flowOf(
            NotifyTask(
                id,
                id,
                true,
                5,
                false
            )
        )

        viewModel.toEditToDoState(TodoItem(id = id, todoText = "test", isImportant = false))
        delay(100)

        Assert.assertEquals(ToDoScreenState.EditToDoDialog, viewModel.getScreenState().value)
        verifySequence {
            notifyTaskManager.getNotifyTaskByTodoId(id)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test changeMarkStatus call this method expect call repositories methods for set true todo state`() =
        runTest {
            val id = 4
            val state = true

            viewModel.changeMarkStatus(state, id)
            delay(100)

            verifySequence {
                toDoRepository.changeMarkStatus(id, state)
                notifyTaskManager.cancelTask(id)
            }

        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test changeMarkStatus call this method expect call repositories methods for set false todo state`() =
        runTest {
            val id = 4
            val state = false

            viewModel.changeMarkStatus(state, id)
            delay(100)

            verifySequence {
                toDoRepository.changeMarkStatus(id, state)
                notifyTaskManager.cancelTask(id)
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test saveToDo expect invoke save repository method`() = runTest {
        every { toDoRepository.getAllToDo() } returns flowOf(
            listOf(
                TodoItem(
                    todoText = "test",
                    isImportant = true
                )
            )
        )
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