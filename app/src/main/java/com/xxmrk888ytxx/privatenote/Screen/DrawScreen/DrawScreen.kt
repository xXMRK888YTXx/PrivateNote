package com.xxmrk888ytxx.privatenote.Screen.DrawScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Utils.Const
import com.xxmrk888ytxx.privatenote.Utils.MustBeLocalization
import com.xxmrk888ytxx.privatenote.Utils.NavArguments
import com.xxmrk888ytxx.privatenote.ui.theme.CardNoteColor
import com.xxmrk888ytxx.privatenote.ui.theme.PrimaryFontColor
import io.ak1.drawbox.DrawBox
import io.ak1.drawbox.rememberDrawController

@Composable
fun DrawScreen(drawViewModel: DrawViewModel = hiltViewModel(),navController: NavController) {
    val newController = rememberDrawController()
    val loadSaveDialogState = remember {
        drawViewModel.getSaveLoadDialogState()
    }
    LaunchedEffect(key1 = drawViewModel, block = {
        drawViewModel.saveNoteId(NavArguments.bundle.getInt(Const.getNoteId))
    })
    val controller = remember {
        drawViewModel.getController(newController)
    }
    if(controller.value == null) navController.navigateUp()
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        DrawBox(controller.value!!,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f))
        DrawToolBar(drawViewModel,navController)
        if(loadSaveDialogState.value) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                SaveLoadDialog()
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@MustBeLocalization
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
            backgroundColor = CardNoteColor
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.padding(end = 20.dp))
                Text(
                    text = "Сохранение",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryFontColor
                )
            }
        }
    }
}

@Composable
fun DrawToolBar(drawViewModel: DrawViewModel,navController: NavController) {
    val listOptions = listOf<DrawOptionItem>(
        DrawOptionItem(
            R.drawable.ic_save
        ){
         drawViewModel.saveDraw(navController)
        },
        DrawOptionItem(
            R.drawable.ic_undo_up
        ){
         drawViewModel.undo()
        },
        DrawOptionItem(
            R.drawable.ic_redo_up
        ){
         drawViewModel.redo()
        },
        DrawOptionItem(
            R.drawable.ic_clear
        ){
         drawViewModel.clearDrawPlace()
        },
        DrawOptionItem(
          R.drawable.ic_circle
        ){},
        DrawOptionItem(
            R.drawable.ic_thickness
        ){},
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        listOptions.forEach {
            IconButton(onClick = { it.onClick() }) {
                Icon(painter = painterResource(it.icon),
                    contentDescription = "",
                    tint = it.iconColor,
                    modifier = Modifier
                        .padding(
                            start = 18.dp,
                            end = 18.dp,
                            top = 10.dp,
                            bottom = 5.dp
                        )
                        .size(27.dp)
                )

            }
        }
    }
}
