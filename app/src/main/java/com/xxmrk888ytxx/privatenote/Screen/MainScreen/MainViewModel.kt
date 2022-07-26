package com.xxmrk888ytxx.privatenote.Screen.MainScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.xxmrk888ytxx.privatenote.DB.Entity.Note
import com.xxmrk888ytxx.privatenote.Repositories.NoteRepository
import com.xxmrk888ytxx.privatenote.Screen.Screen
import com.xxmrk888ytxx.privatenote.Utils.NavArguments
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val noteRepository: NoteRepository
) : ViewModel() {

    val screenState:MutableState<MainScreenState> = mutableStateOf(MainScreenState.NoteScreen)
    get() = field

    val searchFieldText = mutableStateOf("")
    get() = field

    fun changeScreenState(state: MainScreenState) {
        screenState.value = state
    }

    fun getNoteList() : Flow<List<Note>> {
        return noteRepository.getAllNote()
    }

    fun toEditNoteScreen(navController: NavController,id:Int) {
        NavArguments.bundle.putInt("getNoteId",id)
        navController.navigate(Screen.EditNoteScreen.route) {launchSingleTop = true}
    }
}