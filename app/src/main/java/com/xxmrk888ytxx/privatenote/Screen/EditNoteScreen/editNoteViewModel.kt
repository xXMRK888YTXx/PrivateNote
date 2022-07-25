package com.xxmrk888ytxx.privatenote.Screen.EditNoteScreen

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class editNoteViewModel @Inject constructor() : ViewModel() {

    fun backToMainScreen(navController: NavController) {
        navController.navigateUp()
    }
}