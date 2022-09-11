package com.xxmrk888ytxx.privatenote.presentation.MultiUse

import com.google.accompanist.permissions.*

@OptIn(ExperimentalPermissionsApi::class)
fun requestPermission(
    permission: PermissionState,
    onGranted:() -> Unit,
    onDeny:() -> Unit
)
{
    permission.launchPermissionRequest()
    when(permission.status) {
        PermissionStatus.Granted -> onGranted()
        else -> {
            if(!permission.status.shouldShowRationale) {
                onDeny()
            }
        }
    }
}