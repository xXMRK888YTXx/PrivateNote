package com.xxmrk888ytxx.privatenote.presentation

import androidx.compose.runtime.compositionLocalOf
import com.xxmrk888ytxx.privatenote.presentation.Activity.MainActivity.OrientationLockManager

val LocalOrientationLockManager = compositionLocalOf<OrientationLockManager> {
    error("OrientationLockManager not provided")
}