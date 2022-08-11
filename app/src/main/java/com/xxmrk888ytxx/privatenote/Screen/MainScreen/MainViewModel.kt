package com.xxmrk888ytxx.privatenote.Screen.MainScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.xxmrk888ytxx.privatenote.Screen.MultiUse.FloatButton.FloatButtonController
import com.xxmrk888ytxx.privatenote.Utils.Const.FLOAT_BUTTON_KEY
import com.xxmrk888ytxx.privatenote.Utils.Const.SEARCH_BUTTON_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
) : ViewModel(),MainScreenController,FloatButtonController {

    val screenState:MutableState<MainScreenState> = mutableStateOf(MainScreenState.NoteScreen)
    get() = field

    private val topBarVisibleStatus = mutableStateOf(true)

    private val isFloatButtonEnable = mutableStateOf(true)

    private var onClickFloatButton:(navController: NavController) -> Unit = {}

    fun getShowToolBarStatus() = topBarVisibleStatus

    fun getButtonListener(key:Int) = onClickHolder.getOrDefault(key,{})

    fun changeScreenState(state: MainScreenState) {
        screenState.value = state
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

    override fun setFloatButtonOnClickListener(onClick: (navController: NavController) -> Unit) {
        onClickFloatButton = onClick
    }

    override fun changeEnableFloatButtonStatus(enable: Boolean) {
        isFloatButtonEnable.value = enable
    }

    override fun setOnClickListener(navController: NavController) {
        onClickFloatButton(navController)
    }

    override fun isEnable(): MutableState<Boolean> {
        return isFloatButtonEnable
    }

}