package com.xxmrk888ytxx.privatenote.presentation.MultiUse.FloatButton

import androidx.compose.animation.*
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
import com.xxmrk888ytxx.privatenote.presentation.theme.FloatingButtonColor
import com.xxmrk888ytxx.privatenote.presentation.theme.PrimaryFontColor

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FloatButton(floatButtonController: FloatButtonController,navController: NavController) {
    val isEnable = remember {
        floatButtonController.isEnable()
    }
    AnimatedVisibility(visible = isEnable.value,
        enter = scaleIn(),
        exit = scaleOut()
    ) {
        FloatingActionButton(
            onClick = { floatButtonController.setOnClickListener(navController) },
            backgroundColor = FloatingButtonColor,
            modifier = Modifier.size(65.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_plus),
                contentDescription = "plus",
                tint = PrimaryFontColor,
                modifier = Modifier.size(35.dp)
            )
        }
    }

}