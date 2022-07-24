package com.xxmrk888ytxx.privatenote.Screen.MainScreen

import androidx.lifecycle.ViewModel
import com.xxmrk888ytxx.privatenote.DB.Entity.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    fun getNoteList() : List<Note> {
        return listOf(Note("Любители инстосамки","Тихонович Ярослав","12 Мая"),Note("Любители инстосамки","","12 Мая"))
    }
}