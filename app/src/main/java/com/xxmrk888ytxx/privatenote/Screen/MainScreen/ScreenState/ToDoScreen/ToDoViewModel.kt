package com.xxmrk888ytxx.privatenote.Screen.MainScreen.ScreenState.ToDoScreen

import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xxmrk888ytxx.privatenote.DB.Entity.ToDoItem
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Repositories.ToDoRepository.ToDoRepository
import com.xxmrk888ytxx.privatenote.Screen.MainScreen.MainScreenController
import com.xxmrk888ytxx.privatenote.Screen.MultiUse.DataPicker.DataTimePicker
import com.xxmrk888ytxx.privatenote.Screen.MultiUse.DataPicker.DataTimePickerController
import com.xxmrk888ytxx.privatenote.Utils.ShowToast
import com.xxmrk888ytxx.privatenote.Utils.secondToData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToDoViewModel @Inject constructor(
    private val showToast: ShowToast,
    private val toDoRepository: ToDoRepository,
) : ViewModel() {
    init {

    }
    private var mainScreenController: MainScreenController? = null

    private val currentState:MutableState<ToDoScreenState> = mutableStateOf(ToDoScreenState.Default)

    fun getScreenState() = currentState

    val dialogTextField = mutableStateOf("")

    private var currentEditableToDoId:Int = 0

    private val isCurrentEditableToDoImportant = mutableStateOf(false)

    private val currentToDoIsCompleted = mutableStateOf(false)

    private val selectDataTimeState = mutableStateOf(false)

    private val currentToDoTime:MutableState<Long?> = mutableStateOf(null)

    fun getCurrentToDoTime() = currentToDoTime

    fun getIsCurrentEditableToDoImportantStatus() = isCurrentEditableToDoImportant

    fun checkPicker() {
        if(!selectDataTimeState.value) return
        cancelDataTimePicker()
    }

    fun changeImpotentStatus() {
        isCurrentEditableToDoImportant.value = !isCurrentEditableToDoImportant.value
    }

    fun toDefaultMode() {
        currentState.value = ToDoScreenState.Default
        clearEditableToDoInfo()
    }

    fun clearEditableToDoInfo() {
        currentEditableToDoId = 0
        dialogTextField.value = ""
        isCurrentEditableToDoImportant.value = false
        currentToDoIsCompleted.value = false
    }

    fun toEditToDoState(currentEditToDo:ToDoItem? = null) {
        if(currentEditToDo != null) {
            currentEditableToDoId = currentEditToDo.id
            dialogTextField.value = currentEditToDo.todoText
            isCurrentEditableToDoImportant.value = currentEditToDo.isImportant
            currentToDoIsCompleted.value = currentEditToDo.isCompleted
            currentToDoTime.value = currentEditToDo.todoTime
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
        mainScreenController?.setFloatButtonOnClickListener {
            toEditToDoState()
        }
    }

    fun getToDoList() : Flow<List<ToDoItem>> {
        return toDoRepository.getAllToDo()
    }

    fun changeMarkStatus(status:Boolean,id:Int) {
        viewModelScope.launch {
            toDoRepository.changeMarkStatus(id,status)
        }
    }

    fun saveToDo() {
        val currentId = currentEditableToDoId
        val currentText = dialogTextField.value
        val currentImportantState = isCurrentEditableToDoImportant.value
        val currentIsComplited = currentToDoIsCompleted.value
        val currentToDoTime = currentToDoTime.value
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
        }
        toDefaultMode()
    }

    fun showDataPickerDialog(context: Context) {
        selectDataTimeState.value = true
        DataTimePicker().createDataPickerDialog(context,
            object : DataTimePickerController {
                override fun onComplete(time: Long) {
                    currentToDoTime.value = time
                    showToast.showToast(context.getString(R.string.move_time_set) + " "
                            + time.secondToData(context))
                }

                override fun onCancel() {
                    cancelDataTimePicker()
                }

            }
        )
    }

    private fun cancelDataTimePicker() {
        selectDataTimeState.value = false
        Log.d("MyLog","cancel")
    }

    fun removeToDo(id: Int) {
        viewModelScope.launch {
            toDoRepository.removeToDo(id)
        }
    }

    fun removeCurrentToDoTime() {
        currentToDoTime.value = null
        showToast.showToast(R.string.move_time_delete)
    }
}
