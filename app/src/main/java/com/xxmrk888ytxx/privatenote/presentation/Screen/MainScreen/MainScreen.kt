package com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.FabPosition
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Utils.themeColors
import com.xxmrk888ytxx.privatenote.presentation.MultiUse.ConfirmPrivatePolicyAndTermsDialog.ConfirmPrivatePolicyAndTermsDialog
import com.xxmrk888ytxx.privatenote.presentation.MultiUse.FloatButton.FloatButton
import com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.ScreenState.NoteState.NoteScreenState
import com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.ScreenState.ToDoScreen.ToDoScreen
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(
    mainViewModel: MainViewModel = hiltViewModel(),
    navController: NavController
) {
    val state = remember { mainViewModel.screenState }
    val bottomBarState = remember {
        mainViewModel.getShowBottomBarStatus()
    }
    val getScrollBetweenScreenEnabled = remember {
        mainViewModel.getScrollBetweenScreenEnabled()
    }
    val navigationSwipeState = mainViewModel.getNavigationSwipeState().collectAsState(true)
    val isPolityAndTermsConfirmed = mainViewModel.isPolityAndTermsConfirmed().collectAsState(true)
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = { FloatButton(mainViewModel, navController) },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            if (bottomBarState.value) {
                Column() {
                    BottomBar(
                        mainViewModel = mainViewModel,
                        pageState = state.value,
                        navController = navController
                    )
                }


            }
        },
        scaffoldState = rememberScaffoldState()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            HorizontalPager(
                state = state.value,
                userScrollEnabled = navigationSwipeState.value && getScrollBetweenScreenEnabled.value
            ) {
                when (it) {
                    MainScreenState.NoteScreen.id -> {
                        NoteScreenState(
                            navController = navController,
                            mainScreenController = mainViewModel
                        )
                    }

                    MainScreenState.ToDoScreen.id -> {
                        ToDoScreen(mainScreenController = mainViewModel)
                    }
                }

            }
        }
    }
    if (!isPolityAndTermsConfirmed.value) {
        ConfirmPrivatePolicyAndTermsDialog {
            mainViewModel.confirmPolityAndTerms()
        }
    }
    LaunchedEffect(key1 = Unit, block = {
        launch {
            mainViewModel.checkDeepLinks()
        }
    })
}

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
        backgroundColor = themeColors.mainBackGroundColor,
        contentColor = themeColors.primaryFontColor
    ) {
        items.forEach {
            BottomNavigationItem(
                icon = {
                    Icon(
                        painterResource(id = it.icon),
                        contentDescription = it.name,
                        modifier = Modifier.size(20.dp)
                    )
                },
                label = { Text(text = it.name) },
                selectedContentColor = themeColors.primaryFontColor,
                unselectedContentColor = themeColors.primaryFontColor.copy(0.4f),
                alwaysShowLabel = true,
                selected = it.id == pageState.currentPage,
                onClick = { it.onClick() }
            )
        }
    }
}
