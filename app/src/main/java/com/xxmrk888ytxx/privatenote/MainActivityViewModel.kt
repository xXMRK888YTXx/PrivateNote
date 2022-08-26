package com.xxmrk888ytxx.privatenote

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor() : ViewModel() {
     var isFirstStart:Boolean = true
    get() = field

    fun markStart() {
        isFirstStart = false
    }

    fun CompletedAuthCallBack(): (navigate:() -> Unit) -> Unit {
        return {
            it()
            markStart()
        }
    }

}