package com.xxmrk888ytxx.privatenote.presentation.Screen.DrawScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.xxmrk888ytxx.privatenote.presentation.MultiUse.YesNoButtons.YesNoButton
import com.xxmrk888ytxx.privatenote.presentation.MultiUse.YesNoDialog.YesNoDialog
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Utils.BackPressController
import com.xxmrk888ytxx.privatenote.Utils.Const
import com.xxmrk888ytxx.privatenote.Utils.NavArguments
import com.xxmrk888ytxx.privatenote.Utils.themeColors
import com.xxmrk888ytxx.privatenote.presentation.LocalOrientationLockManager
import io.ak1.drawbox.DrawBox
import io.ak1.drawbox.rememberDrawController

@Composable
fun DrawScreen(
    drawViewModel: DrawViewModel = hiltViewModel(),
    navController: NavController,
) {
    val newController = rememberDrawController()

    val orientationLockManager = LocalOrientationLockManager.current

    val loadSaveDialogState = remember {
        drawViewModel.getSaveLoadDialogState()
    }
    val isStrokeWidthSliderShow = remember {
        drawViewModel.isStrokeWidthSliderShow()
    }
    val isSelectColorListShow = remember {
        drawViewModel.isSelectColorListShow()
    }
    val exitDialogState = remember {
        drawViewModel.getExitDialogState()
    }
    LaunchedEffect(key1 = orientationLockManager, block = {
        orientationLockManager.changeOrientationLockState(true)
    })
    DisposableEffect(key1 = orientationLockManager, effect = {
        this.onDispose {
            orientationLockManager.changeOrientationLockState(false)
        }
    })
    val selectColorDialogState = remember {
        drawViewModel.getSelectColorDialogState()
    }
    LaunchedEffect(key1 = drawViewModel, block = {
        drawViewModel.saveNoteId(NavArguments.bundle.getInt(Const.getNoteId))
    })
    val controller = remember {
        drawViewModel.getController(newController)
    }
    if (controller.value == null) navController.navigateUp()
    val drawBoxHeight = animateFloatAsState(
        targetValue = if (isSelectColorListShow.value || isStrokeWidthSliderShow.value) 0.85f else 0.9f
    )
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        DrawBox(
            controller.value!!,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(drawBoxHeight.value)
        )
        DrawToolBar(drawViewModel, navController)
        if (loadSaveDialogState.value) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                SaveLoadDialog()
            }
        }
        if (selectColorDialogState.value) {
            SelectColorDialog(drawViewModel)
        }
    }
    BackPressController.setHandler {
        drawViewModel.showExitDialog()
    }
    if (exitDialogState.value) {
        YesNoDialog(title = stringResource(R.string.Save_Image),
            confirmButtonText = stringResource(R.string.Save),
            cancelButtonText = stringResource(R.string.Not_save),
            onCancel = {
                drawViewModel.hideExitDialog()
                navController.navigateUp()
            },
            onCancelDialog = {
                drawViewModel.hideExitDialog()
            }
        ) {
            drawViewModel.hideExitDialog()
            drawViewModel.saveDraw(navController)
        }
    }
}

@Composable
fun SelectColorDialog(drawViewModel: DrawViewModel) {
    val currentSelectedColor = remember {
        drawViewModel.getCurrentSelectedColor()
    }
    Dialog(onDismissRequest = { drawViewModel.hideSelectColorDialog() }) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = themeColors.cardColor,
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(Modifier.fillMaxWidth()) {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_circle),
                        contentDescription = "",
                        tint = currentSelectedColor.value,
                        modifier = Modifier.size(30.dp)
                    )
                }
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    HsvColorPicker(modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .height(240.dp),
                        controller = rememberColorPickerController(), onColorChanged = {
                            drawViewModel.changeCurrentSelectedColor(it.color)
                        })
                }
                YesNoButton(onCancel = { drawViewModel.hideSelectColorDialog() }) {
                    drawViewModel.hideSelectColorDialog()
                    drawViewModel.changeBrushColor(currentSelectedColor.value)
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SaveLoadDialog() {
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = true
        )
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20),
            backgroundColor = themeColors.cardColor
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.padding(end = 20.dp))
                Text(
                    text = stringResource(R.string.Saving),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = themeColors.primaryFontColor
                )
            }
        }
    }
}

@Composable
fun DrawToolBar(drawViewModel: DrawViewModel, navController: NavController) {
    val isStrokeWidthSliderShow = remember {
        drawViewModel.isStrokeWidthSliderShow()
    }
    val isSelectColorListShow = remember {
        drawViewModel.isSelectColorListShow()
    }
    val currentStrokeWidth = remember {
        drawViewModel.getCurrentStrokeWidth()
    }
    val currentBrushColor = remember {
        drawViewModel.getCurrentBrushColor()
    }

    val listOptions = listOf(
        DrawOptionItem(
            R.drawable.ic_save,
            iconColor = themeColors.primaryFontColor
        ) {
            drawViewModel.saveDraw(navController)
        },
        DrawOptionItem(
            R.drawable.ic_undo_up,
            iconColor = themeColors.primaryFontColor
        ) {
            drawViewModel.undo()
        },
        DrawOptionItem(
            R.drawable.ic_redo_up,
            iconColor = themeColors.primaryFontColor
        ) {
            drawViewModel.redo()
        },
        DrawOptionItem(
            R.drawable.ic_clear,
            iconColor = themeColors.primaryFontColor
        ) {
            drawViewModel.clearDrawPlace()
        },
        DrawOptionItem(
            R.drawable.ic_circle,
            iconColor = currentBrushColor.value
        ) {
            drawViewModel.changeSelectColorListShow()
        },
        DrawOptionItem(
            R.drawable.ic_thickness,
            iconColor = themeColors.primaryFontColor
        ) {
            drawViewModel.changeStrokeWidthSliderState()
        },
    )
    val defColors = listOf(
        Color.Black,
        Color.DarkGray,
        Color.Gray,
        Color.LightGray,
        Color.White,
        Color.Red,
        Color.Blue,
        Color.Green,
        Color.Cyan,
        Color.Yellow,
        Color.Magenta
    )
    Column(Modifier.fillMaxWidth()) {
        AnimatedVisibility(visible = isStrokeWidthSliderShow.value) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Slider(
                    value = currentStrokeWidth.value, onValueChange = {
                        drawViewModel.changeCurrentStrokeWidth(it)
                    },
                    valueRange = 1f..100f,
                    colors = SliderDefaults.colors(
                        thumbColor = themeColors.secondaryColor,
                        activeTrackColor = themeColors.secondaryColor
                    )
                )
            }
        }
        AnimatedVisibility(visible = isSelectColorListShow.value) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                itemsIndexed(defColors) { index, it ->
                    IconButton(onClick = {
                        drawViewModel.changeBrushColor(it)
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_circle),
                            contentDescription = "",
                            tint = it,
                            modifier = Modifier.padding(
                                start = 10.dp,
                                end = 10.dp
                            )
                        )
                    }
                    if (index == defColors.lastIndex) {
                        IconButton(onClick = {
                            drawViewModel.showSelectColorDialog()
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_add_circle),
                                contentDescription = "",
                                tint = themeColors.primaryFontColor,
                                modifier = Modifier.padding(
                                    start = 10.dp,
                                    end = 10.dp
                                )
                            )
                        }
                    }
                }
            }
        }
        AnimatedVisibility(visible = true) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                listOptions.forEach {
                    IconButton(onClick = { it.onClick() }) {
                        Icon(
                            painter = painterResource(it.icon),
                            contentDescription = "",
                            tint = it.iconColor,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .height(100.dp)
                            .width(10.dp)
                    )
                }
            }
        }
    }
}
