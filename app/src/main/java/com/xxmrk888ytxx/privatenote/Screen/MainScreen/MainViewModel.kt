package com.xxmrk888ytxx.privatenote.Screen.MainScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.xxmrk888ytxx.privatenote.DB.Entity.Note
import com.xxmrk888ytxx.privatenote.Screen.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    val screenState:MutableState<MainScreenState> = mutableStateOf(MainScreenState.NoteScreen)
    get() = field

    val searchFieldText = mutableStateOf("")
    get() = field

    fun changeScreenState(state: MainScreenState) {
        screenState.value = state
    }

    fun getNoteList() : List<Note> {
        return listOf(Note("Любители инстосамки","Тихонович Ярослав","12 Мая"),
            Note("Любители инстосамки","","12 Мая"))
    }

    fun toEditNoteScreen(navController: NavController) {
        navController.navigate(Screen.EditNoteScreen.route) {launchSingleTop = true}
    }
}