package com.xxmrk888ytxx.privatenote.Screen.MultiUse.FloatButton

import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Screen.MainScreen.ScreenState.NoteState.NoteScreenMode
import com.xxmrk888ytxx.privatenote.Screen.MainScreen.ScreenState.NoteState.NoteStateViewModel
import com.xxmrk888ytxx.privatenote.ui.theme.FloatingButtonColor
import com.xxmrk888ytxx.privatenote.ui.theme.PrimaryFontColor

@Composable
fun FloatButton(floatButtonController: FloatButtonController,navController: NavController) {
    val isEnable = remember {
        floatButtonController.isEnable()
    }
    if(!isEnable.value) return
    FloatingActionButton(
        onClick = { floatButtonController.setOnClickListener(navController) },
        backgroundColor = FloatingButtonColor,
        modifier = Modifier.size(65.dp)
    ){
        Icon(painter = painterResource(id = R.drawable.ic_plus),
            contentDescription = "plus",
            tint = PrimaryFontColor,
            modifier = Modifier.size(35.dp)
        )
    }
}