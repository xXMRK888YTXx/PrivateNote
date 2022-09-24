package com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.ScreenState.ToDoScreen

import android.content.Context
import android.os.Build
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.xxmrk888ytxx.privatenote.presentation.Activity.MainActivity.ActivityController
import com.xxmrk888ytxx.privatenote.data.Database.Entity.NotifyTask
import com.xxmrk888ytxx.privatenote.data.Database.Entity.ToDoItem
import com.xxmrk888ytxx.privatenote.domain.NotifyTaskManager.NotifyTaskManager
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsRepository.SettingsRepository
import com.xxmrk888ytxx.privatenote.domain.Repositories.ToDoRepository.ToDoRepository
import com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.MainScreenController
import com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.MainScreenState
import com.xxmrk888ytxx.privatenote.presentation.MultiUse.DataPicker.DataTimePicker
import com.xxmrk888ytxx.privatenote.presentation.MultiUse.DataPicker.DataTimePickerController
import com.xxmrk888ytxx.privatenote.domain.ToastManager.ToastManager
import com.xxmrk888ytxx.privatenote.Utils.getData
import com.xxmrk888ytxx.privatenote.Utils.ifNotNull
import com.xxmrk888ytxx.privatenote.Utils.secondToData
import com.xxmrk888ytxx.privatenote.domain.DeepLinkController.DeepLink
import com.xxmrk888ytxx.privatenote.domain.DeepLinkController.DeepLinkController
import com.xxmrk888ytxx.privatenote.domain.NotificationManager.NotificationAppManager
import com.xxmrk888ytxx.privatenote.presentation.MultiUse.requestPermission
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToDoViewModel @Inject constructor(
    private val toastManager: ToastManager,
    private val toDoRepository: ToDoRepository,
    private val notifyTaskManager: NotifyTaskManager,
    private val settingsRepository: SettingsRepository,
    private val notificationAppManager: NotificationAppManager,
    private val deepLinkController: DeepLinkController
) : ViewModel() {
    private var mainScreenController: MainScreenController? = null

    private val currentState:MutableState<ToDoScreenState> = mutableStateOf(ToDoScreenState.Default)

    fun getScreenState() = currentState

    val dialogTextField = mutableStateOf("")

    private var currentEditableToDoId:Int = 0

    private val isCurrentEditableToDoImportant = mutableStateOf(false)

    private val currentToDoIsCompleted = mutableStateOf(false)

    private val selectDataTimeState = mutableStateOf(false)

    private val currentToDoTime:MutableState<Long?> = mutableStateOf(null)

    private val isNotifyDialogShow = mutableStateOf(false)

    private val isShowNotifyDropDown = mutableStateOf(false)

    private val isPickerNotifyTimeShow = mutableStateOf(false)

    private val currentNotifyTime:MutableState<Long?> = mutableStateOf(null)

    private val isCurrentNotifyEnable:MutableState<Boolean> = mutableStateOf(false)

    private val isCurrentNotifyPriority = mutableStateOf(true)

    fun isCurrentNotifyPriority() = isCurrentNotifyPriority

    private var savedNotifyTime:Long? = null

    private var savedIsCurrentNotifyEnable:Boolean = false

    private var savedIsCurrentNotifyPriority = true

    private val removeDialogState = mutableStateOf(Pair<Boolean,Int?>(false,null))

    var cachedToDoList:List<ToDoItem> = listOf()

    private val requestPermissionSendAlarmsDialog = mutableStateOf(false)

    fun getRequestPermissionSendAlarmsDialog() = requestPermissionSendAlarmsDialog

    fun showRequestPermissionSendAlarmsDialog() {
        requestPermissionSendAlarmsDialog.value = true
    }

    fun hideRequestPermissionSendAlarmsDialog() {
        requestPermissionSendAlarmsDialog.value = false
    }

    fun isRemoveDialogShow() = removeDialogState

    fun showRemoveDialog(id:Int) {
        removeDialogState.value = Pair(true,id)
    }

    fun hideRemoveDialog() {
        removeDialogState.value = Pair(false,null)
    }

    fun isCompletedToDoVisible() = settingsRepository.getCompletedToDoVisible()

    fun changeCompletedToDoVisible() {
        viewModelScope.launch {
            settingsRepository.changeCompletedToDoVisible()
        }
    }

    fun isToDoWithDateVisible() = settingsRepository.getToDoWithDateVisible()

    fun changeToDoWithDateVisible() {
        viewModelScope.launch {
            settingsRepository.changeToDoWithDateVisible()
        }
    }

    fun isToDoWithoutDateVisible() = settingsRepository.getToDoWithoutDateVisible()

    fun changeToDoWithoutDateVisible() {
        viewModelScope.launch {
            settingsRepository.changeToDoWithoutDateVisible()
        }
    }

    fun isMissedToDoVisible() = settingsRepository.getMissedToDoVisible()

    fun changeMissedToDoVisible() {
        viewModelScope.launch {
            settingsRepository.changeMissedToDoVisible()
        }
    }

    fun getNotifyEnableStatus() = isCurrentNotifyEnable

    fun getTask(todoId:Int) : Flow<NotifyTask?> {
        return notifyTaskManager.getNotifyTaskByTodoId(todoId)
    }

    fun getCurrentNotifyTime() = currentNotifyTime


    fun getNotifyDropDownState() = isShowNotifyDropDown



    fun getNotifyDialogState() = isNotifyDialogShow

    @OptIn(ExperimentalPermissionsApi::class)
    fun showNotifyDialog(permission: PermissionState?) {
        if(!isHaveNotificationPostPermission()) {
            requestPostNotificationPermission(permission)
            return
        }
        if(!isCanSendAlarms()) {
            showRequestPermissionSendAlarmsDialog()
            return
        }
        isNotifyDialogShow.value = true
        savedNotifyTime = currentNotifyTime.value
        savedIsCurrentNotifyEnable = isCurrentNotifyEnable.value
        savedIsCurrentNotifyPriority = isCurrentNotifyPriority.value
    }

    @OptIn(ExperimentalPermissionsApi::class)
    private fun requestPostNotificationPermission(permission: PermissionState?) {
        permission.ifNotNull {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermission(
                    permission = it,
                    onGranted = {
                        showNotifyDialog(it)
                    },
                    onDeny = {
                        toastManager.showToast(R.string.Request_post_notification_permission)
                    }
                )
            }
        }
    }

    private fun isHaveNotificationPostPermission(): Boolean = notificationAppManager.isHavePostNotificationPermission()

    fun hideNotifyDialog() {
        isNotifyDialogShow.value = false
    }

    fun cancelNotifyDialog() {
        currentNotifyTime.value = savedNotifyTime
        isNotifyDialogShow.value = savedIsCurrentNotifyEnable
        isCurrentNotifyPriority.value = savedIsCurrentNotifyPriority
        hideNotifyDialog()
    }

    fun confirmNotifyDialog() {
        if(!isCurrentNotifyEnable.value) {
            currentNotifyTime.value = null
        }
        hideNotifyDialog()
    }

    fun getCurrentToDoTime() = currentToDoTime

    fun getIsCurrentEditableToDoImportantStatus() = isCurrentEditableToDoImportant

    fun checkPickers() {
        if(selectDataTimeState.value)
        cancelDataTimePicker()
        if(isPickerNotifyTimeShow.value) {
            cancelNotifyTimePicker()
        }
    }

    fun changeImpotentStatus() {
        isCurrentEditableToDoImportant.value = !isCurrentEditableToDoImportant.value
    }

    fun toDefaultMode() {
        currentState.value = ToDoScreenState.Default
        clearEditableToDoInfo()
    }

    private fun clearEditableToDoInfo() {
        currentEditableToDoId = 0
        dialogTextField.value = ""
        isCurrentEditableToDoImportant.value = false
        currentToDoIsCompleted.value = false
        currentToDoTime.value = null
        currentNotifyTime.value = null
        isCurrentNotifyEnable.value = false
        isCurrentNotifyPriority.value = true
    }

    fun toEditToDoState(currentEditToDo:ToDoItem? = null) {
        if(currentEditToDo != null) {
            currentEditableToDoId = currentEditToDo.id
            dialogTextField.value = currentEditToDo.todoText
            isCurrentEditableToDoImportant.value = currentEditToDo.isImportant
            currentToDoIsCompleted.value = currentEditToDo.isCompleted
            currentToDoTime.value = currentEditToDo.todoTime
            viewModelScope.launch {
                val task = notifyTaskManager.getNotifyTaskByTodoId(currentEditToDo.id).getData()
                currentNotifyTime.value = task?.time
                isCurrentNotifyPriority.value = task?.isPriority ?: true
                isCurrentNotifyEnable.value = currentNotifyTime.value != null
            }
        }
        else{
            currentEditableToDoId = 0
        }
        currentState.value = ToDoScreenState.EditToDoDialog
    }

    fun setMainScreenController(mainScreenController: MainScreenController?) {
        if(mainScreenController == null) return
        this.mainScreenController = mainScreenController
        SetupFloatButtonOptions()
    }

    private fun SetupFloatButtonOptions() {
        mainScreenController?.setFloatButtonOnClickListener(SCREEN_ID) {
            toEditToDoState()
        }
    }

    fun getToDoList() : Flow<List<ToDoItem>> {
        return toDoRepository.getAllToDo()
    }

    fun changeMarkStatus(status:Boolean,id:Int) {
        viewModelScope.launch {
            toDoRepository.changeMarkStatus(id,status)
            notifyTaskManager.cancelTask(id)
        }
    }

    fun saveToDo() {
        val currentId = currentEditableToDoId
        val currentText = dialogTextField.value
        val currentImportantState = isCurrentEditableToDoImportant.value
        val currentIsComplited = currentToDoIsCompleted.value
        val currentToDoTime = currentToDoTime.value
        val currentNotifyTime = currentNotifyTime.value
        val currentNotifyPriority = isCurrentNotifyPriority.value
        viewModelScope.launch {
            toDoRepository.insertToDo(
                toDoItem = ToDoItem(
                    id = currentId,
                    todoText = currentText,
                    isImportant = currentImportantState,
                    isCompleted = currentIsComplited,
                    todoTime = currentToDoTime
                )
            )
            val realId = if(currentId == 0) toDoRepository.getAllToDo().getData().last().id
            else currentId
           if(currentNotifyTime != null) {
               notifyTaskManager.newTask(NotifyTask(
                   taskId = 0,
                   todoId = realId,
                   enable = true,
                   time = currentNotifyTime,
                   isPriority = currentNotifyPriority
               ))
           }
            else {
              if(notifyTaskManager.getNotifyTaskByTodoId(realId).getData() != null) {
                  notifyTaskManager.cancelTask(realId)
              }
           }
        }
        toDefaultMode()
    }

    fun showDataPickerDialog(context: Context) {
        selectDataTimeState.value = true
        DataTimePicker().createDataPickerDialog(context,
            object : DataTimePickerController {
                override fun onComplete(time: Long) {
                    currentToDoTime.value = time
                    toastManager.showToast(context.getString(R.string.move_time_set) + " "
                            + time.secondToData(context))
                }

                override fun onCancel() {
                    cancelDataTimePicker()
                }

                override fun onError() {

                }

            },
            dropLattesDays = false
        )
    }

    private fun cancelDataTimePicker() {
        selectDataTimeState.value = false
    }

    private fun cancelNotifyTimePicker() {
        isPickerNotifyTimeShow.value = false
    }

    fun removeToDo(id: Int) {
        viewModelScope.launch {
            toDoRepository.removeToDo(id)
            notifyTaskManager.cancelTask(id)
        }
    }

    fun removeCurrentToDoTime() {
        currentToDoTime.value = null
        toastManager.showToast(R.string.move_time_delete)
    }

    private fun isCanSendAlarms() : Boolean = notifyTaskManager.isCanSendAlarms()


    fun showPickerNotifyTimeDialog(context: Context) {
        isPickerNotifyTimeShow.value = true
        DataTimePicker().createDataPickerDialog(context,
            object : DataTimePickerController {
                override fun onComplete(time: Long) {
                    isPickerNotifyTimeShow.value = false
                    currentNotifyTime.value = time
                }

                override fun onCancel() {
                    cancelNotifyTimePicker()
                }

                override fun onError() {
                    toastManager.showToast(context.getString(R.string.data_is_passed))
                    cancelNotifyTimePicker()
                }

            },
            validator = {
                System.currentTimeMillis() < it
            }
        )
    }

    fun openAlarmSettings(activityController: ActivityController) {
        activityController.openAlarmSettings()
    }

    suspend fun checkDeepLinks() {
        val deepLink = validateDeepLink(deepLinkController.getDeepLink()) ?: return
        val todo = deepLink.todo
        if(todo != null) {
            if(!toDoRepository.getAllToDo().getData().any{ it == todo }) {
                deepLinkController.markInvalidDeepLink(deepLink.idDeepLink)
                return
            }
        }
        toEditToDoState(todo)
        deepLinkController.markInvalidDeepLink(deepLink.idDeepLink)
    }

    private fun validateDeepLink(deepLink: DeepLink?) : DeepLink.TodoDeepLink? {
        if(deepLink == null) return null
        if(deepLink !is DeepLink.TodoDeepLink) return null
        if(!deepLink.isActiveDeepLink) return null
        return deepLink
    }

    companion object {
         val SCREEN_ID = MainScreenState.ToDoScreen.id
    }
}
