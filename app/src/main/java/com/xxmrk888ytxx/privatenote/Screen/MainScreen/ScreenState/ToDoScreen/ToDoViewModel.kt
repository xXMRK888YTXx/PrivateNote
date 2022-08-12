package com.xxmrk888ytxx.privatenote.Screen.MainScreen.ScreenState.ToDoScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xxmrk888ytxx.privatenote.DB.Entity.ToDoItem
import com.xxmrk888ytxx.privatenote.Repositories.ToDoRepository.ToDoRepository
import com.xxmrk888ytxx.privatenote.Screen.MainScreen.MainScreenController
import com.xxmrk888ytxx.privatenote.Utils.ShowToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToDoViewModel @Inject constructor(
    private val showToast: ShowToast,
    private val toDoRepository: ToDoRepository
) : ViewModel() {
    private var mainScreenController: MainScreenController? = null

    private val currentState:MutableState<ToDoScreenState> = mutableStateOf(ToDoScreenState.Default)

    fun getScreenState() = currentState

    val dialogTextField = mutableStateOf("")

    private var currentEditableToDoId:Int = 0

    private val isCurrentEditableToDoImportant = mutableStateOf(false)

    private val currentToDoIsCompleted = mutableStateOf(false)

    fun getIsCurrentEditableToDoImportantStatus() = isCurrentEditableToDoImportant

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
        viewModelScope.launch {
            toDoRepository.insertToDo(
                toDoItem = ToDoItem(
                    id = currentId,
                    todoText = currentText,
                    isImportant = currentImportantState,
                    isCompleted = currentIsComplited
                )
            )
        }
        toDefaultMode()
    }
}