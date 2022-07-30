package com.xxmrk888ytxx.privatenote.Screen.MainScreen.ScreenState.NoteState

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.xxmrk888ytxx.privatenote.DB.Entity.Note
import com.xxmrk888ytxx.privatenote.Repositories.NoteRepository
import com.xxmrk888ytxx.privatenote.Screen.Screen
import com.xxmrk888ytxx.privatenote.Utils.Const.getNoteId
import com.xxmrk888ytxx.privatenote.Utils.NavArguments
import com.xxmrk888ytxx.privatenote.Utils.ShowToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
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
    }

}