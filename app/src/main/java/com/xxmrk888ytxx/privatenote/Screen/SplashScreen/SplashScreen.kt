package com.xxmrk888ytxx.privatenote.Screen.SplashScreen

import android.window.SplashScreen
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Screen.Screen
import com.xxmrk888ytxx.privatenote.ui.theme.MainBackGroundColor
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val startAnimation = remember {
        mutableStateOf(false)
    }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation.value) 1f else 0f,
        animationSpec = tween(2500)
    )

    LaunchedEffect(key1 = true, block = {
        startAnimation.value = true
        delay(2500)
        navController.popBackStack()
        navController.navigate(Screen.MainScreen.route){launchSingleTop = true}
    })
    Splash(alpha = alphaAnim.value)
}

@Composable
fun Splash(alpha:Float) {
    Box(modifier = Modifier
        .background(MainBackGroundColor)
        .fillMaxSize(),
        contentAlignment = Alignment.Center
    )
    {
        Image(
            painter = painterResource(R.drawable.ic_main_note_icon),
            contentDescription = "note",
            modifier = Modifier.size(175.dp).alpha(alpha),
        )
    }
}