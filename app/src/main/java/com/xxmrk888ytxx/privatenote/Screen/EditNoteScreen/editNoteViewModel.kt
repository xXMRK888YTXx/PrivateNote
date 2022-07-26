package com.xxmrk888ytxx.privatenote.Screen.EditNoteScreen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.xxmrk888ytxx.privatenote.DB.Entity.Note
import com.xxmrk888ytxx.privatenote.Repositories.NoteRepository
import com.xxmrk888ytxx.privatenote.Utils.getData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class editNoteViewModel @Inject constructor(
   val noteRepository: NoteRepository
) : ViewModel() {

    val titleTextField = mutableStateOf("")
    val textField = mutableStateOf("")
    val currentTime = mutableStateOf(0L)
    var note: Note = Note(title = "", text = "", )
    var isSaveNote = true
    val isDropDownMenuShow = mutableStateOf(false)

    fun backToMainScreen(navController: NavController) {
        navController.navigateUp()
    }

    fun getNote(id:Int) {
        Log.d("MyLog",id.toString())
        if(id != 0) {
            note = noteRepository.getNoteById(id).getData()
            titleTextField.value = note.title
            textField.value = note.text
            currentTime.value = note.created_at

        }
        else {
            note = Note(title = "", text = "")
        }
    }

    fun saveNote() {
        if(!isSaveNote) return
        viewModelScope.launch {
            if(textField.value == note.text&&titleTextField.value == note.title) return@launch
            noteRepository.insertNote(note.copy(created_at = System.currentTimeMillis(),
            title = titleTextField.value,
                text = textField.value
                ))
        }
    }

    fun removeNote(navController: NavController) {
        isDropDownMenuShow.value = false
        if(note.id != 0) {
            noteRepository.removeNote(note.id)
        }
        isSaveNote = false
        navController.navigateUp()
    }
}