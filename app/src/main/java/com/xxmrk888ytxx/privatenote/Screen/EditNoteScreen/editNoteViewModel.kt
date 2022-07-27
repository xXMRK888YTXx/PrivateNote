package com.xxmrk888ytxx.privatenote.Screen.EditNoteScreen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.xxmrk888ytxx.privatenote.DB.Entity.Note
import com.xxmrk888ytxx.privatenote.Repositories.NoteRepository
import com.xxmrk888ytxx.privatenote.Screen.EditNoteScreen.States.SaveNoteState
import com.xxmrk888ytxx.privatenote.SecurityUtils.SecurityUtils
import com.xxmrk888ytxx.privatenote.Utils.getData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class editNoteViewModel @Inject constructor(
   val noteRepository: NoteRepository,
   val securityUtils: SecurityUtils
) : ViewModel() {

    val titleTextField = mutableStateOf("")

    val textField = mutableStateOf("")

    val currentTime = mutableStateOf(0L)

    private var note: Note = Note(title = "", text = "")

    private val saveNoteState = mutableStateOf<SaveNoteState>(SaveNoteState.None)

    val isDropDownMenuShow = mutableStateOf(false)

    val isShowCryptDialog = mutableStateOf(false)

    private var notePassword:String? = null

    fun backToMainScreen(navController: NavController) {
        navController.navigateUp()
    }

    fun getNote(id:Int,navController: NavController) {
        Log.d("MyLog",id.toString())
        if(id != 0) {
            note = noteRepository.getNoteById(id).getData()
            if(!note.isEncrypted) {
                titleTextField.value = note.title
                textField.value = note.text
                saveNoteState.value = SaveNoteState.DefaultSaveNote
            }
            currentTime.value = note.created_at

        }
        else {
            note = Note(title = "", text = "")
            saveNoteState.value = SaveNoteState.DefaultSaveNote
        }
    }

    fun saveNote() {
        viewModelScope.launch {
            when(saveNoteState.value) {
                is SaveNoteState.DefaultSaveNote -> {
                    if(textField.value == note.text&&titleTextField.value == note.title) return@launch
                    noteRepository.insertNote(note.copy(created_at = System.currentTimeMillis(),
                        title = titleTextField.value,
                        text = textField.value
                    ))
                }
                is SaveNoteState.RemoveNote -> {
                    if(note.id != 0) {
                        noteRepository.removeNote(note.id)
                    }
                }
                is SaveNoteState.CryptSaveNote -> {

                }

                is SaveNoteState.None -> return@launch
            }
        }
    }

    fun removeNote(navController: NavController) {
        isDropDownMenuShow.value = false
        saveNoteState.value = SaveNoteState.RemoveNote
        navController.navigateUp()
    }

    fun changeStateToEncryptNote(password:String) {
        saveNoteState.value = SaveNoteState.CryptSaveNote
        note.isEncrypted = true
        notePassword = securityUtils.getPasswordToHash(password)
    }
}