package com.xxmrk888ytxx.privatenote.presentation

import androidx.compose.runtime.compositionLocalOf
import com.xxmrk888ytxx.privatenote.presentation.Activity.MainActivity.OrientationLockManager
import com.xxmrk888ytxx.privatenote.presentation.Activity.MainActivity.WakeLockController

val LocalOrientationLockManager = compositionLocalOf<OrientationLockManager> {
    error("OrientationLockManager not provided")
}

val LocalWakeLockController = compositionLocalOf<WakeLockController> {
    error("WakeLockController not provided")
}