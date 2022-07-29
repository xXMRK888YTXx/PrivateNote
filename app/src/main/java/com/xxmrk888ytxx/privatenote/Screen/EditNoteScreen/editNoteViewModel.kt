package com.xxmrk888ytxx.privatenote.Screen.EditNoteScreen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.xxmrk888ytxx.privatenote.DB.Entity.Note
import com.xxmrk888ytxx.privatenote.Exception.FailedDecryptException
import com.xxmrk888ytxx.privatenote.LifeCycleState
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Repositories.NoteRepository
import com.xxmrk888ytxx.privatenote.Screen.EditNoteScreen.States.SaveNoteState
import com.xxmrk888ytxx.privatenote.Screen.EditNoteScreen.States.ShowDialogState
import com.xxmrk888ytxx.privatenote.SecurityUtils.SecurityUtils
import com.xxmrk888ytxx.privatenote.Utils.ShowToast
import com.xxmrk888ytxx.privatenote.Utils.getData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class editNoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val securityUtils: SecurityUtils,
    private val showToast: ShowToast,
    private val lifeCycleState: MutableStateFlow<LifeCycleState>
) : ViewModel() {

    init {
        viewModelScope.launch {
            lifeCycleState.collect() {
                if(it == LifeCycleState.onPause) {
                    if(saveNoteState.value == SaveNoteState.CryptSaveNote) {
                        isHideText.value = true
                        dialogShowState.value = ShowDialogState.DecryptDialog
                    }
                    if(note.id == 0) return@collect
                    saveNote()
                }
            }
        }
    }
    val titleTextField = mutableStateOf("")

    val textField = mutableStateOf("")

    val currentTime = mutableStateOf(0L)

    private var note: Note = Note(title = "", text = "")

    private val saveNoteState = mutableStateOf<SaveNoteState>(SaveNoteState.None)

    val isDropDownMenuShow = mutableStateOf(false)

    val dialogShowState = mutableStateOf<ShowDialogState>(ShowDialogState.None)

    private var notePassword:String? = null

    val isHideText = mutableStateOf(false)

    private var primaryNoteVersion:Note? = null

    fun savePrimaryVersion(note: Note) {
        if(primaryNoteVersion != null) return
        primaryNoteVersion = note
    }

    fun backToMainScreen(navController: NavController) {
        navController.navigateUp()
    }

    fun getNote(id:Int) {
        if(id != 0) {
            note = noteRepository.getNoteById(id).getData()
            savePrimaryVersion(note.copy())
            if(!note.isEncrypted) {
                    titleTextField.value = note.title
                    textField.value = note.text
                saveNoteState.value = SaveNoteState.DefaultSaveNote
            }
            else {
                dialogShowState.value = ShowDialogState.DecryptDialog
            }
            currentTime.value = note.created_at

        }
        else {
            if(saveNoteState.value != SaveNoteState.CryptSaveNote) {
                saveNoteState.value = SaveNoteState.DefaultSaveNote
            }
            else {
                dialogShowState.value = ShowDialogState.DecryptDialog
                isHideText.value = true
            }
        }
    }


    fun saveNote() {
        GlobalScope.launch {
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
                is SaveNoteState.NotSaveChanges -> {
                    noteRepository.insertNote(primaryNoteVersion!!)
                }
                is SaveNoteState.CryptSaveNote -> {
                    try {
                        val title = securityUtils.encrypt(titleTextField.value,notePassword!!)
                        val text = securityUtils.encrypt(textField.value,notePassword!!)
                        if(text == note.text&&title == note.title) return@launch
                        noteRepository.insertNote(note.copy(created_at = System.currentTimeMillis(),
                            title = title,
                            text = text
                        ))
                    }catch (e:Exception){}

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
        notePassword = securityUtils.passwordToHash(password)
        dialogShowState.value = ShowDialogState.None
        showToast.showToast(R.string.Note_encrypted)
    }

    fun isEncryptNote() = note.isEncrypted

   fun decrypt(password: String) {
       if(notePassword != null) {
           if(notePassword == securityUtils.passwordToHash(password)) {
               dialogShowState.value = ShowDialogState.None
               isHideText.value = false
               return
           }
           else {
               throw FailedDecryptException("Invalid password")
           }
       }
        try {
            val hashPassword = securityUtils.passwordToHash(password)
            isHideText.value = false
            titleTextField.value = securityUtils.decrypt(note.title,hashPassword)
            textField.value = securityUtils.decrypt(note.text,hashPassword)
            notePassword = hashPassword
            saveNoteState.value = SaveNoteState.CryptSaveNote
            dialogShowState.value = ShowDialogState.None
        }catch (e:Exception) {
            throw FailedDecryptException("Invalid password")
        }

    }

    fun changeStateToDefaultNote() {
        saveNoteState.value = SaveNoteState.DefaultSaveNote
        note.isEncrypted = false
        notePassword = null
        showToast.showToast(R.string.Note_decrypted)
    }

    fun notSaveChanges(navController: NavController) {
        saveNoteState.value = SaveNoteState.NotSaveChanges
        navController.navigateUp()
    }

    override fun onCleared() {
        saveNote()
        super.onCleared()
    }
    fun getToast() = showToast

    fun isHavePrimaryVersion() = primaryNoteVersion != null
}