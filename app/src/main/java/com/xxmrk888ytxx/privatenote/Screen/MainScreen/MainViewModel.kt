package com.xxmrk888ytxx.privatenote.Screen.MainScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.xxmrk888ytxx.privatenote.MultiUse.FloatButton.FloatButtonController
import com.xxmrk888ytxx.privatenote.Repositories.SettingsRepository.SettingsRepository
import com.xxmrk888ytxx.privatenote.Screen.Screen
import com.xxmrk888ytxx.privatenote.Utils.Const.SEARCH_BUTTON_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel(),MainScreenController,FloatButtonController {

    @OptIn(ExperimentalPagerApi::class)
    val screenState = mutableStateOf(PagerState())
    get() = field

    private val topBarVisibleStatus = mutableStateOf(true)

    private val isFloatButtonEnable = mutableStateOf(true)

    private var onClickFloatButton:MutableMap<Int,(navController: NavController) -> Unit> = mutableMapOf()

    fun getShowToolBarStatus() = topBarVisibleStatus

    fun getButtonListener(key:Int) = onClickHolder.get(key) ?: {}

    @OptIn(ExperimentalPagerApi::class)
    suspend fun changeScreenState(state: MainScreenState) {
            screenState.value.animateScrollToPage(state.id)
    }
    private val onClickHolder = mutableMapOf<Int,() -> Unit>()

    override fun changeTopBarVisibleStatus(isVisible: Boolean) {
        topBarVisibleStatus.value = isVisible
    }

    override fun setSearchButtonOnClickListener(onClick: () -> Unit) {
        onClickHolder.set(SEARCH_BUTTON_KEY,onClick)
    }

    override fun searchButtonOnClick() {
        onClickHolder.getOrDefault(SEARCH_BUTTON_KEY,{})()
    }

    override fun setFloatButtonOnClickListener(screenKey:Int,onClick: (navController: NavController) -> Unit) {
        onClickFloatButton.set(screenKey,onClick)
    }

    override fun changeEnableFloatButtonStatus(enable: Boolean) {
        isFloatButtonEnable.value = enable
    }

    @OptIn(ExperimentalPagerApi::class)
    override fun setOnClickListener(navController: NavController) {
        onClickFloatButton.get(screenState.value.currentPage)?.invoke(navController)

    }

    override fun isEnable(): MutableState<Boolean> {
        return isFloatButtonEnable
    }

    fun toSettingsScreen(navController: NavController) {
        navController.navigate(Screen.SettingsScreen.route) {
            launchSingleTop = true
        }
    }

    fun getNavigationSwipeState() = settingsRepository.getNavigationSwipeState()

}