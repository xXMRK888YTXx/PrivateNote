package com.xxmrk888ytxx.privatenote.Screen.MainScreen.ScreenState.ToDoScreen

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.xxmrk888ytxx.privatenote.Screen.MainScreen.MainScreenController
import com.xxmrk888ytxx.privatenote.Screen.MainScreen.ScreenState.NoteState.NoteScreenMode
import com.xxmrk888ytxx.privatenote.Screen.MultiUse.FloatButton.FloatButtonController
import com.xxmrk888ytxx.privatenote.Utils.ShowToast
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ToDoViewModel @Inject constructor(
    private val showToast: ShowToast
) : ViewModel() {
    private var mainScreenController: MainScreenController? = null

    fun setMainScreenController(mainScreenController: MainScreenController?) {
        if(mainScreenController == null) return
        this.mainScreenController = mainScreenController
        SetupFloatButtonOptions()
    }

    private fun SetupFloatButtonOptions() {
        mainScreenController?.setFloatButtonOnClickListener {
            showToast.showToast("TODO")
            mainScreenController?.changeEnableFloatButtonStatus(false)
        }
    }
}