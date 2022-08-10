package com.xxmrk888ytxx.privatenote.Screen.MainScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.xxmrk888ytxx.privatenote.Utils.Const.SEARCH_BUTTON_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
) : ViewModel(),TopBarController {

    val screenState:MutableState<MainScreenState> = mutableStateOf(MainScreenState.NoteScreen)
    get() = field

    private val topBarVisibleStatus = mutableStateOf(true)

    fun getShowToolBarStatus() = topBarVisibleStatus

    fun getButtonListener(key:Int) = onClickHolder.getOrDefault(key,{})

    fun changeScreenState(state: MainScreenState) {
        screenState.value = state
    }
    private val onClickHolder = mutableMapOf<Int,() -> Unit>()

    override fun changeVisibleStatus(isVisible: Boolean) {
        topBarVisibleStatus.value = isVisible
    }

    override fun setSearchButtonOnClickListener(onClick: () -> Unit) {
        onClickHolder.set(SEARCH_BUTTON_KEY,onClick)
    }

    override fun searchButtonOnClick() {
        onClickHolder.getOrDefault(SEARCH_BUTTON_KEY,{})()
    }

}