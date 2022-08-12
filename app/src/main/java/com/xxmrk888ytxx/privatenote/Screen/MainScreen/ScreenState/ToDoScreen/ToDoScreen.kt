package com.xxmrk888ytxx.privatenote.Screen.MainScreen.ScreenState.ToDoScreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.xxmrk888ytxx.privatenote.DB.Entity.ToDoItem
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Screen.MainScreen.MainScreenController
import com.xxmrk888ytxx.privatenote.Utils.sortedToDo
import com.xxmrk888ytxx.privatenote.ui.theme.*

@Composable
fun ToDoScreen(toDoViewModel: ToDoViewModel = hiltViewModel(),mainScreenController: MainScreenController) {
    val state = remember {
        toDoViewModel.getScreenState()
    }
    LaunchedEffect(key1 = Unit, block = {
        toDoViewModel.setMainScreenController(mainScreenController)
    })
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MainBackGroundColor)
    ) {
        TopLabel()
        ToDoList(toDoViewModel)
    }
    if(state.value == ToDoScreenState.EditToDoDialog) {
        EditToDoDialog(toDoViewModel)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditToDoDialog(toDoViewModel: ToDoViewModel) {
    val currentToDoImpotent = remember {
        toDoViewModel.getIsCurrentEditableToDoImportantStatus()
    }
    val toDoEditItems = listOf(
        ToDoEditItem(
            icon = R.drawable.ic_priority_high,
            activate = currentToDoImpotent.value,
            activateColor = Color.Red.copy(0.9f)
        ) {
          toDoViewModel.changeImpotentStatus()
        },
        ToDoEditItem(
            icon = R.drawable.ic_timer
        ) {},
        ToDoEditItem(
            icon = R.drawable.ic_notifications
        ) {},
    )
    val textField = remember {
        toDoViewModel.dialogTextField
    }
    Dialog(onDismissRequest = { toDoViewModel.toDefaultMode()},
        //properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            backgroundColor = CardNoteColor
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(value = textField.value, onValueChange = {
                    if(it.length > 100) return@OutlinedTextField
                    textField.value = it
                },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    singleLine = true,
                    label = {
                        Text(text = "Задача",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryFontColor
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = PrimaryFontColor,
                        backgroundColor = SearchColor,
                        placeholderColor = PrimaryFontColor.copy(0.7f),
                        focusedBorderColor = PrimaryFontColor,
                        focusedLabelColor = PrimaryFontColor,
                        cursorColor = PrimaryFontColor,
                        unfocusedLabelColor = PrimaryFontColor.copy(0.6f)
                    ),
                    textStyle = TextStyle(
                        fontWeight = FontWeight.Medium,
                    ),
                )
                Row(modifier = Modifier.fillMaxWidth()) {
                    LazyRow(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        items(toDoEditItems) {
                            val tint = if(it.activate) it.activateColor else it.deActivateColor
                            Icon(painter = painterResource(it.icon),
                                contentDescription = "",
                                tint = tint,
                                modifier = Modifier
                                    .padding(5.dp)
                                    .size(30.dp)
                                    .clickable {
                                        it.onClick()
                                    }
                            )
                        }
                    }
                    OutlinedButton(
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = FloatingButtonColor,
                            disabledBackgroundColor = FloatingButtonColor.copy(0.3f)
                        ),
                        enabled = textField.value.isNotEmpty(),
                        onClick = {
                            toDoViewModel.saveToDo()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 5.dp, end = 5.dp),
                        shape = RoundedCornerShape(80),
                    ) {
                        Text(text = stringResource(R.string.Save),
                            color = PrimaryFontColor
                        )
                    }
                }

            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ToDoList(toDoViewModel: ToDoViewModel) {
    val toDoList = toDoViewModel.getToDoList().collectAsState(listOf())
    val sortedToDo = toDoList.value.sortedToDo()
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(sortedToDo) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp).combinedClickable(
                        onClick = {
                            toDoViewModel.toEditToDoState(it)
                        }
                    )
                    .animateItemPlacement(),
                shape = RoundedCornerShape(15),
                backgroundColor = CardNoteColor.copy(0.3f)
            ) {
                ToDoItem(it,toDoViewModel)
            }
        }
    }
}

@Composable
fun ToDoItem(todo: ToDoItem, toDoViewModel: ToDoViewModel) {
    val fontColor = if(todo.isCompleted) PrimaryFontColor.copy(0.3f) else PrimaryFontColor
    Row(
        modifier = Modifier
            .padding(top = 10.dp, bottom = 10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = todo.isCompleted,
            onCheckedChange = { toDoViewModel.changeMarkStatus(it,todo.id)},
            modifier = Modifier.padding(start = 0.dp),
            colors = CheckboxDefaults.colors(
                checkedColor = FloatingButtonColor,
                checkmarkColor = PrimaryFontColor,
                uncheckedColor = FloatingButtonColor
            )
        )
        if(todo.isImportant) {
            Icon(painter = painterResource(R.drawable.ic_priority_high),
                contentDescription = "",
                tint = Color.Red.copy(0.9f),
                modifier = Modifier.size(22.dp)
                )
        }
        Text(
            text = todo.todoText,
            modifier = Modifier
                .padding(start = 5.dp)
                .fillMaxWidth(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            color = fontColor,
            fontStyle = FontStyle.Italic,
        )
    }
}

@Composable
fun TopLabel() {
    Column(modifier = Modifier.padding(start = 25.dp, bottom = 0.dp, top = 20.dp)) {
        Text(text = "Всё задачи",
            fontWeight = FontWeight.W800,
            fontSize = 30.sp,
            color = PrimaryFontColor,
            modifier = Modifier.clickable {
            }
        )
        Text(
            text = "Всё задачи выполнены",
            fontStyle = FontStyle.Italic,
            fontSize = 18.sp,
            color = SecondoryFontColor,
            modifier = Modifier.padding(top = 5.dp)
        )
    }
}
