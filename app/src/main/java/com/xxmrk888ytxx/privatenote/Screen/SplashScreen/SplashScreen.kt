package com.xxmrk888ytxx.privatenote.Screen.SplashScreen

import android.util.Log
import androidx.biometric.BiometricPrompt
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.xxmrk888ytxx.privatenote.MultiUse.PasswordEditText.PasswordEditText
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Utils.BackPressController
import com.xxmrk888ytxx.privatenote.ui.theme.MainBackGroundColor
import com.xxmrk888ytxx.privatenote.ui.theme.PrimaryFontColor
import com.xxmrk888ytxx.privatenote.ui.theme.SecondoryFontColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SplashScreen(
    navController: NavController,
    splashViewModel: SplashViewModel = hiltViewModel(),
    isAppPasswordInstalled: Boolean,
    animationShowState: Boolean,
    isBiometricAuthorizationEnable: Boolean,
    onAuthorization: (callBack: BiometricPrompt.AuthenticationCallback) -> Unit,
    isFirstStart: Boolean,
    onCompletedAuth:(navigate:() -> Unit) -> Unit,
    finishApp:() -> Unit
) {
    val scope = rememberCoroutineScope()
    val startAnimation = remember {
        mutableStateOf(false)
    }
    val isShowAnimation = remember {
        splashViewModel.isShowAnimation
    }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation.value) 1f else 0f,
        animationSpec = tween(2500)
    )
    var isNextBackPressLeaveApp = false
    BackPressController.setHandler(true) {
        if(isNextBackPressLeaveApp)
        finishApp()
        else {
            splashViewModel.showToastForLeaveApp()
            isNextBackPressLeaveApp = true
            scope.launch {
                delay(3000)
                isNextBackPressLeaveApp = false
            }
        }
    }
    LaunchedEffect(key1 = true, block = {
        if(animationShowState) {
            startAnimation.value = true
            delay(2500)
        }
        if (!isAppPasswordInstalled) {
            if(isFirstStart)
            onCompletedAuth { splashViewModel.toMainScreen(navController) }
            else onCompletedAuth { navController.navigateUp() }
        }
        else isShowAnimation.value = false
    })
    AnimatedVisibility(visible = isShowAnimation.value,
        exit = shrinkHorizontally(
            animationSpec = tween(250)
        ),
    ) {
        Splash(alpha = alphaAnim.value)
    }
    AnimatedVisibility(visible = !isShowAnimation.value,
        enter = scaleIn(),
        exit =  scaleOut()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Authorization(splashViewModel, navController,onCompletedAuth)
            if (isBiometricAuthorizationEnable) {
                LaunchedEffect(key1 = true, block = {
                    onAuthorization(splashViewModel
                        .getAuthorizationCallBack(navController,isFirstStart,onCompletedAuth))
                })
                FingerPrintButton(onAuthorization,splashViewModel
                    .getAuthorizationCallBack(navController,isFirstStart,onCompletedAuth))
            }
        }
    }

}

@Composable
fun FingerPrintButton(onAuthorization: (callBack: BiometricPrompt.AuthenticationCallback) -> Unit,
                      callBack: BiometricPrompt.AuthenticationCallback
) {
    IconButton(
        onClick = { onAuthorization(callBack) },
        modifier = Modifier
            .size(60.dp)
            .clip(RoundedCornerShape(40))
            .background(SecondoryFontColor)
    ) {
        Icon(painter = painterResource(R.drawable.ic_fingerprint),
            contentDescription = "",
            tint = PrimaryFontColor,
            modifier = Modifier.size(70.dp)
        )
    }
}

@Composable
fun Authorization(splashViewModel: SplashViewModel,navController: NavController,
                  onCompletedAuth: (navigate: () -> Unit) -> Unit) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val titleText = remember {
        mutableStateOf(context.getString(R.string.Enter_password))
    }
    val password = remember {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PasswordEditText(titleText = titleText,
            password = password,
            onDoneClick = {
                coroutineScope.launch{
                    if(splashViewModel.checkPassword(password.value)) {
                        onCompletedAuth() {splashViewModel.toMainScreen(navController)}
                    }else {
                        titleText.value = context.getString(R.string.Invalid_password)
                    }
                }
            }
        )
        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(start = 15.dp, end = 15.dp, bottom = 15.dp),
            onClick =  {
                coroutineScope.launch{
                    if(splashViewModel.checkPassword(password.value)) {
                        onCompletedAuth() {splashViewModel.toMainScreen(navController)}
                    }else {
                        titleText.value = context.getString(R.string.Invalid_password)
                    }
                }
            },
            shape = RoundedCornerShape(50),
            enabled = password.value.isNotEmpty(),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = PrimaryFontColor,
                disabledContentColor = Color.Black.copy(0.3f),
                disabledBackgroundColor = PrimaryFontColor.copy(0.3f)
            )
        ){
            Text(text = stringResource(R.string.Login),
                fontSize = 18.sp,
                color = Color.Black
            )
        }
    }
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
            modifier = Modifier
                .size(175.dp)
                .alpha(alpha),
        )
    }
}