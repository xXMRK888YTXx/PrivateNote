package com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.xxmrk888ytxx.privatenote.domain.AdManager.AdShowManager
import com.xxmrk888ytxx.privatenote.domain.DeepLinkController.DeepLink
import com.xxmrk888ytxx.privatenote.domain.DeepLinkController.DeepLinkController
import com.xxmrk888ytxx.privatenote.presentation.MultiUse.FloatButton.FloatButtonController
import com.xxmrk888ytxx.privatenote.domain.Repositories.SettingsRepository.SettingsRepository
import com.xxmrk888ytxx.privatenote.presentation.Screen.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val deepLinkController: DeepLinkController,
    private val adShowManager: AdShowManager
) : ViewModel(),MainScreenController,FloatButtonController {

    @OptIn(ExperimentalPagerApi::class)
    val screenState = mutableStateOf(PagerState())
    get() = field

    private val topBarVisibleStatus = mutableStateOf(true)

    private val isFloatButtonEnable = mutableStateOf(true)

    //отвечает за то доступен ли скрол в данном состоянии приложении, имеет силу только при включённом
    // параметре NavigationSwipeState
    private val isScrollBetweenScreenEnabled = mutableStateOf(true)

    fun getScrollBetweenScreenEnabled() = isScrollBetweenScreenEnabled

    private var onClickFloatButton:MutableMap<Int,(navController: NavController) -> Unit> = mutableMapOf()

    fun getShowBottomBarStatus() = topBarVisibleStatus

    fun getButtonListener(key:Int) = onClickHolder.get(key) ?: {}

    @OptIn(ExperimentalPagerApi::class)
    suspend fun changeScreenState(state: MainScreenState) {
            screenState.value.animateScrollToPage(state.id)
    }
    private val onClickHolder = mutableMapOf<Int,() -> Unit>()

    override fun changeBottomBarVisibleStatus(isVisible: Boolean) {
        topBarVisibleStatus.value = isVisible
    }

    override fun setFloatButtonOnClickListener(screenKey:Int,onClick: (navController: NavController) -> Unit) {
        onClickFloatButton.set(screenKey,onClick)
    }

    override fun changeEnableFloatButtonStatus(enable: Boolean) {
        isFloatButtonEnable.value = enable
    }

    override fun changeScrollBetweenScreenState(state: Boolean) {
        isScrollBetweenScreenEnabled.value = state
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

    suspend fun checkDeepLinks() {
        validateDeepLink(deepLinkController.getDeepLink()) ?: return
        changeScreenState(MainScreenState.ToDoScreen)
    }

    private fun validateDeepLink(deepLink: DeepLink?) : DeepLink.TodoDeepLink? {
        if(deepLink == null) return null
        if(deepLink !is DeepLink.TodoDeepLink) return null
        if(!deepLink.isActiveDeepLink) return null
        return deepLink
    }

    fun getNavigationSwipeState() = settingsRepository.getNavigationSwipeState()

    fun isPolityAndTermsConfirmed() = settingsRepository.getPolicyAndTermsDialogState()

    fun confirmPolityAndTerms() {
        viewModelScope.launch {
            settingsRepository.disablePolicyAndTermsDialogState()
        }
    }

    fun isNeedShowAd() = adShowManager.isNeedShowAds()

}