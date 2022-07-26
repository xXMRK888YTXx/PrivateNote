package com.xxmrk888ytxx.privatenote.Screen.MainScreen

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.xxmrk888ytxx.privatenote.Screen.MainScreen.ScreenState.NoteScreenState

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(mainViewModel: MainViewModel = hiltViewModel(),navController: NavController) {
   val state = remember {
       mainViewModel.screenState
   }
    when(state.value) {
      is MainScreenState.NoteScreen -> {
          NoteScreenState(mainViewModel, navController)}
    }
}