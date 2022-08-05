package com.xxmrk888ytxx.privatenote.Screen.MainScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
) : ViewModel() {

    val screenState:MutableState<MainScreenState> = mutableStateOf(MainScreenState.NoteScreen)
    get() = field

    fun changeScreenState(state: MainScreenState) {
        screenState.value = state
    }
}