package com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.xxmrk888ytxx.privatenote.presentation.Activity.MainActivity.ActivityController
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.ScreenState.NoteState.NoteScreenState
import com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.ScreenState.ToDoScreen.ToDoScreen
import com.xxmrk888ytxx.privatenote.presentation.MultiUse.FloatButton.FloatButton
import com.xxmrk888ytxx.privatenote.presentation.theme.MainBackGroundColor
import com.xxmrk888ytxx.privatenote.presentation.theme.PrimaryFontColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(
    mainViewModel: MainViewModel = hiltViewModel(),
    navController: NavController,
    activityController: ActivityController
) {
    val state = remember {
        mainViewModel.screenState
    }
    val bottomBarState = remember {
        mainViewModel.getShowBottomBarStatus()
    }
    val getScrollBetweenScreenEnabled = remember {
        mainViewModel.getScrollBetweenScreenEnabled()
    }
    val navigationSwipeState = mainViewModel.getNavigationSwipeState().collectAsState(true)
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = { FloatButton(mainViewModel,navController) },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            if(bottomBarState.value) {
                BottomBar(
                    mainViewModel = mainViewModel,
                    pageState = state.value,
                    navController = navController)

            }
        },
        scaffoldState = rememberScaffoldState()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            HorizontalPager(count = 2,
                state = state.value,
                userScrollEnabled = navigationSwipeState.value && getScrollBetweenScreenEnabled.value
            ) {
                when(it) {
                     MainScreenState.NoteScreen.id -> {
                         NoteScreenState(navController = navController, mainScreenController = mainViewModel)
                     }
                    MainScreenState.ToDoScreen.id -> {
                        ToDoScreen(mainScreenController = mainViewModel, activityController = activityController)
                    }
                }

            }
        }
    }
    LaunchedEffect(key1 = Unit, block = {
        launch {
            mainViewModel.checkDeepLinks()
        }
    })
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun BottomBar(
    mainViewModel: MainViewModel,
    pageState: PagerState,
    navController: NavController
) {
    val scope = rememberCoroutineScope()
    val items = listOf<BottomMenuItems>(
        BottomMenuItems(
            name = stringResource(R.string.Note),
            icon = R.drawable.ic_notes,
            id = MainScreenState.NoteScreen.id,
            onClick = {
                scope.launch {
                    mainViewModel.changeScreenState(MainScreenState.NoteScreen)
                }
            }
        ),
        BottomMenuItems(
            name = stringResource(R.string.ToDo),
            icon = R.drawable.ic_todo_icon,
            id = MainScreenState.ToDoScreen.id,
            onClick = {
                scope.launch {
                    mainViewModel.changeScreenState(MainScreenState.ToDoScreen)
                }
            }
        ),
        BottomMenuItems(
            name = stringResource(R.string.Settings),
            icon = R.drawable.ic_settings,
            id = -1,
            onClick = {
                mainViewModel.toSettingsScreen(navController)
            }
        ),
    )
    BottomNavigation(
        backgroundColor = MainBackGroundColor,
        contentColor = PrimaryFontColor
    ) {
        items.forEach {
            BottomNavigationItem(
                icon =  { Icon(painterResource(id = it.icon),
                    contentDescription = it.name,
                    modifier = Modifier.size(20.dp)
                )},
                label = { Text(text = it.name)},
                selectedContentColor = Color.White,
                unselectedContentColor = Color.White.copy(0.4f),
                alwaysShowLabel = true,
                selected = it.id == pageState.currentPage,
                onClick = {it.onClick()}
            )
        }
    }
}
