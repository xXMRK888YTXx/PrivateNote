package com.xxmrk888ytxx.privatenote.Screen.MainScreen.ScreenState.ToDoScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.xxmrk888ytxx.privatenote.DB.Entity.ToDoItem
import com.xxmrk888ytxx.privatenote.Repositories.ToDoRepository.ToDoRepository
import com.xxmrk888ytxx.privatenote.Screen.MainScreen.MainScreenController
import com.xxmrk888ytxx.privatenote.Screen.MainScreen.ScreenState.NoteState.NoteScreenMode
import com.xxmrk888ytxx.privatenote.Screen.MultiUse.FloatButton.FloatButtonController
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

    fun setMainScreenController(mainScreenController: MainScreenController?) {
        if(mainScreenController == null) return
        this.mainScreenController = mainScreenController
        SetupFloatButtonOptions()
    }

    private fun SetupFloatButtonOptions() {
        mainScreenController?.setFloatButtonOnClickListener {
            viewModelScope.launch {
                toDoRepository.insertToDo(
                    ToDoItem(
                        id = 0,
                        todoText = "test",
                        isImportant = false
                    )
                )
            }
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
}