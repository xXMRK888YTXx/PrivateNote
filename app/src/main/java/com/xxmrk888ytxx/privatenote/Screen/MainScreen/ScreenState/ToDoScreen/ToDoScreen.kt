package com.xxmrk888ytxx.privatenote.Screen.MainScreen.ScreenState.ToDoScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.xxmrk888ytxx.privatenote.Screen.MainScreen.MainScreenController
import com.xxmrk888ytxx.privatenote.ui.theme.MainBackGroundColor

@Composable
fun ToDoScreen(toDoViewModel: ToDoViewModel = hiltViewModel(),mainScreenController: MainScreenController) {
    Column(
        modifier = Modifier.fillMaxSize()
            .background(MainBackGroundColor)
    ) {
        toDoViewModel.setMainScreenController(mainScreenController)
    }
}