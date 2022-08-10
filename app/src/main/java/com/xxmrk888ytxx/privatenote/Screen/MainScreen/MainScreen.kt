package com.xxmrk888ytxx.privatenote.Screen.MainScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Screen.MainScreen.ScreenState.NoteState.NoteScreenState
import com.xxmrk888ytxx.privatenote.Utils.Const.SEARCH_BUTTON_KEY
import com.xxmrk888ytxx.privatenote.ui.theme.MainBackGroundColor
import com.xxmrk888ytxx.privatenote.ui.theme.PrimaryFontColor

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(mainViewModel: MainViewModel = hiltViewModel(),navController: NavController) {
   val state = remember {
       mainViewModel.screenState
   }
    val toolbarState = remember {
        mainViewModel.getShowToolBarStatus()
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if(toolbarState.value) {
            TopNavigationBar(mainViewModel)
        }
        when(state.value) {
            is MainScreenState.NoteScreen -> {
                NoteScreenState(navController = navController, topBarController = mainViewModel)
            }
            is MainScreenState.ToDoScreen -> {

            }
        }
    }
}

@Composable
fun TopNavigationBar(mainViewModel: MainViewModel) {
    val state = remember {
        mainViewModel.screenState
    }
    val navigationIcons = listOf<NavigationIconItem>(
        NavigationIconItem(R.drawable.ic_notes,
            state.value != MainScreenState.NoteScreen,
        ) {
            mainViewModel.changeScreenState(MainScreenState.NoteScreen)
        },
        NavigationIconItem(R.drawable.ic_todo_icon,state.value != MainScreenState.ToDoScreen){
            mainViewModel.changeScreenState(MainScreenState.ToDoScreen)
        },
        NavigationIconItem(R.drawable.ic_settings, isNavigation = false){},
        NavigationIconItem(R.drawable.ic_baseline_search_24,
            isNavigation = false,
            isVisible = state.value == MainScreenState.NoteScreen
        ){
            mainViewModel.getButtonListener(SEARCH_BUTTON_KEY)()
        },

    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MainBackGroundColor),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        navigationIcons.getNavigationIcons().forEach {
            val color = if(it.enable) PrimaryFontColor else PrimaryFontColor.copy(0.3f)
            Icon(painter = painterResource(it.icon),
                contentDescription = "",
                tint = color,
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 10.dp)
                    .size(25.dp)
                    .clickable {
                        if (!it.enable) return@clickable
                        it.onClick()
                    }
            )
        }
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navigationIcons.getNavigationIcons(true).forEach {
                if(it.isVisible) {
                    Icon(painter = painterResource(it.icon),
                        contentDescription = "",
                        tint = PrimaryFontColor,
                        modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp, top = 10.dp)
                            .size(25.dp)
                            .clickable {
                                if (!it.enable) return@clickable
                                it.onClick()
                            }
                    )
                }
            }
        }
    }
}
