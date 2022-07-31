package com.xxmrk888ytxx.privatenote.Screen.MainScreen.ScreenState.NoteState

import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.xxmrk888ytxx.privatenote.DB.Entity.Note
import com.xxmrk888ytxx.privatenote.Repositories.NoteRepository
import com.xxmrk888ytxx.privatenote.Screen.Screen
import com.xxmrk888ytxx.privatenote.Utils.Const.getNoteId
import com.xxmrk888ytxx.privatenote.Utils.NavArguments
import com.xxmrk888ytxx.privatenote.Utils.ShowToast
import com.xxmrk888ytxx.privatenote.Utils.getData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteStateViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    val showToast: ShowToast
) : ViewModel() {
    val searchFieldText = mutableStateOf("")
        get() = field

    private val currentNoteModeMode = mutableStateOf<NoteScreenMode>(NoteScreenMode.Default)

    fun getCurrentMode() = currentNoteModeMode

    fun getNoteList() : Flow<List<Note>> {
        return noteRepository.getAllNote()
    }

    fun toEditNoteScreen(navController: NavController, id:Int) {
        NavArguments.bundle.putInt(getNoteId,id)
        navController.navigate(Screen.EditNoteScreen.route) {launchSingleTop = true}
    }
    fun toSelectionMode() {
        currentNoteModeMode.value = NoteScreenMode.SelectionScreenMode
    }

    fun toDefaultMode() {
        currentNoteModeMode.value = NoteScreenMode.Default
        selectedNoteList.clear()
    }

    private val selectedNoteList = mutableSetOf<Int>()

    fun isSelected(noteId:Int) : Boolean {
        return selectedNoteList.any{it == noteId}
    }

    fun changeSelectedState(noteId: Int,checkState:Boolean) {
        if(!checkState) {
            selectedNoteList.remove(noteId)
        }
        else {
            selectedNoteList.add(noteId)
        }
        isSelectedItemNotEmpty.value = isSelectedNotEmpty()
        Log.d("MyLog",selectedNoteList.toString())
        selectionItemCount.value = selectedNoteList.size
    }
    var isSelectedItemNotEmpty = mutableStateOf(false)

    var selectionItemCount = mutableStateOf(0)
    get() = field

    fun selectAll() {
        currentNoteModeMode.value = NoteScreenMode.Default
        getNoteList().getData().forEach {
            changeSelectedState(it.id,true)
        }
        currentNoteModeMode.value = NoteScreenMode.SelectionScreenMode
    }

    fun isSelectedNotEmpty() = selectedNoteList.isNotEmpty()

     fun removeSelected() {
         viewModelScope.launch {
             val selectedItem = selectedNoteList
             currentNoteModeMode.value = NoteScreenMode.Default
             selectedItem.forEach {
                 noteRepository.removeNote(it)
             }
         }
    }

    fun toSearchMode() {
        currentNoteModeMode.value = NoteScreenMode.SearchScreenMode
    }
}
fun search(subString: String, note: Note) : Boolean {
    if(note.isEncrypted) return false
    if(subString.toLowerCase() in note.text.toLowerCase()) return true
    if(subString.toLowerCase() in note.title.toLowerCase()) return true
    return false
}

fun List<Note>.searchFilter(enable:Boolean,subString: String) : List<Note> {
    if(!enable) return this
    return this.filter { search(subString,it) }
}