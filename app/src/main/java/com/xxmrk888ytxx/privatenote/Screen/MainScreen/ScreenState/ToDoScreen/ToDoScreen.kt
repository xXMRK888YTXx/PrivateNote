package com.xxmrk888ytxx.privatenote.Screen.MainScreen.ScreenState.ToDoScreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.xxmrk888ytxx.privatenote.DB.Entity.ToDoItem
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Screen.MainScreen.MainScreenController
import com.xxmrk888ytxx.privatenote.Utils.sortedToDo
import com.xxmrk888ytxx.privatenote.ui.theme.*

@Composable
fun ToDoScreen(toDoViewModel: ToDoViewModel = hiltViewModel(),mainScreenController: MainScreenController) {
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
                    .padding(10.dp)
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
    val textDecorate = if(todo.isCompleted) TextDecoration.LineThrough else TextDecoration.None
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
            textDecoration = textDecorate,
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
