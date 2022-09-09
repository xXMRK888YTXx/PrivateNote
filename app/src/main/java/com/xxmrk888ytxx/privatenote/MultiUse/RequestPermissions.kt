package com.xxmrk888ytxx.privatenote.MultiUse

import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.*
import com.google.android.gms.tasks.OnSuccessListener

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