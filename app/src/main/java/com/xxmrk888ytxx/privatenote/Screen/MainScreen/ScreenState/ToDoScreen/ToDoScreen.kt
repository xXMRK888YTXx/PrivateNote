package com.xxmrk888ytxx.privatenote.Screen.MainScreen.ScreenState.ToDoScreen

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.xxmrk888ytxx.privatenote.DB.Entity.ToDoItem
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Screen.MainScreen.MainScreenController
import com.xxmrk888ytxx.privatenote.Screen.MultiUse.YesNoButtons.YesNoButton
import com.xxmrk888ytxx.privatenote.Utils.secondToData
import com.xxmrk888ytxx.privatenote.Utils.sortedToDo
import com.xxmrk888ytxx.privatenote.ui.theme.*
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@Composable
fun ToDoScreen(toDoViewModel: ToDoViewModel = hiltViewModel(),mainScreenController: MainScreenController) {
    val state = remember {
        toDoViewModel.getScreenState()
    }
    val notifyDialogState = remember {
        toDoViewModel.getNotifyDialogState()
    }
    LaunchedEffect(key1 = Unit, block = {
        toDoViewModel.setMainScreenController(mainScreenController)
        toDoViewModel.checkPickers()
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
        if(notifyDialogState.value) {
           NotifyDialog(toDoViewModel)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditToDoDialog(toDoViewModel: ToDoViewModel) {
    val currentToDoImpotent = remember {
        toDoViewModel.getIsCurrentEditableToDoImportantStatus()
    }
    val showDataPickerDialog = remember {
        mutableStateOf(false)
    }
    val currentToDoTime = remember {
        toDoViewModel.getCurrentToDoTime()
    }
    val currentNotifyTime = remember {
        toDoViewModel.getCurrentNotifyTime()
    }
    val context = LocalContext.current
    val toDoEditItems = listOf(
        ToDoEditItem(
            icon = R.drawable.ic_priority_high,
            activate = currentToDoImpotent.value,
            activateColor = Color.Red.copy(0.9f)
        ) {
          toDoViewModel.changeImpotentStatus()
        },
        ToDoEditItem(
            icon = R.drawable.ic_calendar,
            activate = currentToDoTime.value != null,
            activateColor = FloatingButtonColor
        ) {
            if(currentToDoTime.value == null)
            toDoViewModel.showDataPickerDialog(context)
            else toDoViewModel.removeCurrentToDoTime()
        },
        ToDoEditItem(
            icon = R.drawable.ic_notifications,
            activate = currentNotifyTime.value != null,
            activateColor = Color.Yellow.copy(0.9f)
        ) {
          toDoViewModel.showNotifyDialog()
        },
    )
    val textField = remember {
        toDoViewModel.dialogTextField
    }
    Dialog(onDismissRequest = { toDoViewModel.toDefaultMode()},

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
                        Text(text = stringResource(R.string.Task),
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
                            Row(verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable {
                                    it.onClick()
                                }
                            ) {
                                Icon(painter = painterResource(it.icon),
                                    contentDescription = "",
                                    tint = tint,
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .size(30.dp)
                                )
                            }
                        }
                    }
                    Box(contentAlignment = Alignment.CenterEnd, modifier = Modifier.fillMaxWidth()) {
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
                                .fillMaxWidth(0.65f)
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
    if(showDataPickerDialog.value) {

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ToDoList(toDoViewModel: ToDoViewModel) {
    val toDoList = toDoViewModel.getToDoList().collectAsState(listOf())
    val isCompletedToDoVisible = remember {
        toDoViewModel.isCompletedToDoVisible()
    }
    val annotatedLabelString = buildAnnotatedString {
        append("Завершено")
        if (isCompletedToDoVisible.value)
            appendInlineContent("visible")
        else appendInlineContent("invisible")
    }
    val complited = toDoList.value.sortedToDo(false)
    val uncomplited = toDoList.value.sortedToDo(true)
    val inlineContentMap = mapOf(
        "visible" to InlineTextContent(
            Placeholder(50.sp, 50.sp, PlaceholderVerticalAlign.TextCenter)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_drop_down_triangle),
                contentDescription = "",
                tint = SecondoryFontColor,
                modifier = Modifier.padding(top = 12.dp)
            )
        },
        "invisible" to InlineTextContent(
            Placeholder(50.sp, 50.sp, PlaceholderVerticalAlign.TextCenter)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_drop_up),
                contentDescription = "",
                tint = SecondoryFontColor,
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    )
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        itemsIndexed(complited, key = { _, it ->
            it.id
        }) { index, it ->
            val removeSwipeAction = SwipeAction(
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_backet),
                        contentDescription = "",
                        tint = PrimaryFontColor,
                        modifier = Modifier.padding(end = 50.dp)
                    )
                },
                background = DeleteOverSwapColor,
                onSwipe = {
                    toDoViewModel.removeToDo(it.id)
                },
                isUndo = true,
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .combinedClickable(
                        onClick = {
                            toDoViewModel.toEditToDoState(it)
                        }
                    )
                    .animateItemPlacement(),
                shape = RoundedCornerShape(15),
                backgroundColor = CardNoteColor
            ) {
                SwipeableActionsBox(
                    startActions = listOf(removeSwipeAction),
                    endActions = listOf(removeSwipeAction),
                    backgroundUntilSwipeThreshold = Color.Transparent,
                    swipeThreshold = 90.dp
                ) {
                    ToDoItem(it, toDoViewModel)
                }

            }
        }
        itemsIndexed(uncomplited, key = { _, it ->
            it.id
        }) { index, it ->
            if (index == 0) {
                Text(
                    text = annotatedLabelString,
                    inlineContent = inlineContentMap,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = SecondoryFontColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItemPlacement()
                        .clickable {
                            toDoViewModel.changeCompletedToDoVisible()
                        }
                        .padding(15.dp)
                )
            }
            if (isCompletedToDoVisible.value) {
                val removeSwipeAction = SwipeAction(
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_backet),
                            contentDescription = "",
                            tint = PrimaryFontColor,
                            modifier = Modifier.padding(start = 50.dp)
                        )
                    },
                    background = DeleteOverSwapColor,
                    onSwipe = {
                        toDoViewModel.removeToDo(it.id)
                    },
                    isUndo = true
                )
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .combinedClickable(
                            onClick = {
                                toDoViewModel.toEditToDoState(it)
                            }
                        )
                        .animateItemPlacement(),
                    shape = RoundedCornerShape(15),
                    backgroundColor = CardNoteColor
                ) {
                    SwipeableActionsBox(
                        startActions = listOf(removeSwipeAction),
                        endActions = listOf(removeSwipeAction),
                        backgroundUntilSwipeThreshold = Color.Transparent,
                        swipeThreshold = 90.dp
                    ) {
                        ToDoItem(it, toDoViewModel)
                    }

                }
            }
        }
    }
}

@Composable
fun ToDoItem(todo: ToDoItem, toDoViewModel: ToDoViewModel) {
    val fontColor = if(todo.isCompleted) PrimaryFontColor.copy(0.3f) else PrimaryFontColor
    val todoTimeText = remember {
        mutableStateOf("")
    }
    val subTextColor:MutableState<Color> = remember {
        mutableStateOf(SecondoryFontColor)
    }
    todoTimeText.value = getTodoSubText(todo, LocalContext.current)
    subTextColor.value = getTodoSubTextColor(todo)
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 5.dp, bottom = 5.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = todo.isCompleted,
                onCheckedChange = { toDoViewModel.changeMarkStatus(it, todo.id) },
                modifier = Modifier.padding(start = 0.dp),
                colors = CheckboxDefaults.colors(
                    checkedColor = FloatingButtonColor,
                    checkmarkColor = PrimaryFontColor,
                    uncheckedColor = FloatingButtonColor
                )
            )
            if (todo.isImportant) {
                Icon(
                    painter = painterResource(R.drawable.ic_priority_high),
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
        if(!todo.isCompleted&&todo.todoTime != null) {
            Row(modifier = Modifier.padding(start = 10.dp)) {
                Text(text = todoTimeText.value,
                    fontSize = 12.sp,
                    color = subTextColor.value,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}
@Composable
fun getTodoSubText(todo: ToDoItem, context: Context): String {
    if(!todo.isCompleted&&todo.todoTime != null) return "${todo.todoTime.secondToData(context)}"
    return ""
}
@Composable
fun getTodoSubTextColor(todo: ToDoItem) : Color {
    if(!todo.isCompleted&&todo.todoTime != null) {
        if(todo.todoTime in 0..System.currentTimeMillis()) return Color.Red.copy(0.9f)
      //  else return Color.Cyan.copy(0.7f)
    }
    return SecondoryFontColor
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

@Composable
fun NotifyDialog(toDoViewModel: ToDoViewModel) {
    val temp = remember {
        mutableStateOf(false)
    }
    val dropDownState = remember {
        toDoViewModel.getNotifyDropDownState()
    }
    val toDoTime = remember {
        toDoViewModel.getCurrentToDoTime()
    }
    val currentNotifyTime = remember {
        toDoViewModel.getCurrentNotifyTime()
    }
    val notifyEnabled = remember {
        toDoViewModel.getNotifyEnableStatus()
    }
    val textAlpha = if(notifyEnabled.value) 1f else 0.3f
    val context = LocalContext.current
    val timeText = if(currentNotifyTime.value != null) currentNotifyTime.value!!.secondToData(context)
    else "Выберете время"
    Dialog(onDismissRequest = { toDoViewModel.hideNotifyDialog() }) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10),
            backgroundColor = CardNoteColor
        ) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 0.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Напоминание включено",
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryFontColor
                    )
                    Box(modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Switch(
                            checked = notifyEnabled.value,
                            onCheckedChange = {
                                notifyEnabled.value = it
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = FloatingButtonColor,
                                uncheckedThumbColor = SecondoryFontColor
                            ),
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    }
                }
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Напомнить в",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = PrimaryFontColor.copy(textAlpha)
                    )
                    Box(Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                        ) {
                        Text(text = timeText,
                            fontSize = 14.sp,
                            color = PrimaryFontColor.copy(textAlpha),
                            modifier = Modifier.clickable {
                                if(!notifyEnabled.value) return@clickable
                                toDoViewModel.showPickerNotifyTimeDialog(context)
                            }
                        )
                    }
                }
                YesNoButton(onCancel = { toDoViewModel.hideNotifyDialog() },
                modifier = Modifier.padding(top = 20.dp)
                    ) {

                }
            }
        }
    }
}

//@Composable
//fun NotifyDropDown(toDoViewModel: ToDoViewModel,haveToDoTime:Boolean) {
//    val dropDownState = remember {
//        toDoViewModel.getNotifyDropDownState()
//    }
//    val customItems = remember {
//        toDoViewModel.getCustomTimeList()
//    }
//    val context = LocalContext.current
//    val items = listOf(
//        NotifyDropDownItem(
//            id = 1,
//            title = "За 5 минут",
//            isVisible = haveToDoTime
//        ),
//        NotifyDropDownItem(
//            id = 2,
//            title = "За 15 минут",
//            isVisible = haveToDoTime
//        ),
//        NotifyDropDownItem(
//            id = 3,
//            title = "За 30 минут",
//            isVisible = haveToDoTime
//        ),
//        NotifyDropDownItem(
//            id = 4,
//            title = "За 1 час",
//            isVisible = haveToDoTime
//        ),
//        NotifyDropDownItem(
//            id = 5,
//            title = "За 3 часа",
//            isVisible = haveToDoTime
//        ),
//        NotifyDropDownItem(
//            id = 6,
//            title = "За 6 часов",
//            isVisible = haveToDoTime
//        ),
//        NotifyDropDownItem(
//            id = 7,
//            title = "За 12 часов",
//            isVisible = haveToDoTime
//        ),
//        NotifyDropDownItem(
//            id = 8,
//            title = "За 1 день",
//            isVisible = haveToDoTime
//        ),
//        NotifyDropDownItem(
//            id = 9,
//            title = "За 3 дня",
//            isVisible = haveToDoTime
//        ),
//    )
//    DropdownMenu(expanded = dropDownState.value,
//        onDismissRequest = { toDoViewModel.hideNotifyDropDown() },
//        modifier = Modifier
//            .background(DropDownMenuColor)
//            .heightIn(max = 200.dp)
//    ){
//        items.forEach {
//            NotifyDropDownItem(toDoViewModel,it)
//        }
//        customItems.value.forEach{
//            NotifyDropDownItem(toDoViewModel,it)
//        }
//        Text(text = "Своё время",
//            fontSize = 15.sp,
//            color = Color.Cyan.copy(0.9f),
//            modifier = Modifier
//                .padding()
//                .clickable {
//                    toDoViewModel.hideNotifyDropDown()
//                    toDoViewModel.showCreateCustomNotifyTimeDialog(context)
//                }
//                .fillMaxWidth(),
//            textAlign = TextAlign.Center
//        )
//    }
//}
//
//@Composable
//fun NotifyDropDownItem(toDoViewModel: ToDoViewModel, item:NotifyDropDownItem) {
//    val selectedTime = remember {
//        toDoViewModel.getselectedNotifyTimes()
//    }
//    val selected = remember {
//        mutableStateOf(false)
//    }
//    SideEffect{
//        selected.value = selectedTime.value.any{item.id == it.id }
//    }
//
//    if(item.isVisible) {
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Checkbox(checked = selected.value,
//                onCheckedChange = {
//                toDoViewModel.changeNotifyTimeSelectedStatus(item)
//                    selected.value = it
//            },
//                colors = CheckboxDefaults.colors(
//                    checkedColor = FloatingButtonColor,
//                    checkmarkColor = PrimaryFontColor,
//                    uncheckedColor = FloatingButtonColor
//                )
//            )
//            Text(text = item.title,
//                fontSize = 15.sp,
//                color = PrimaryFontColor,
//                modifier = Modifier.padding(end = 10.dp)
//            )
//        }
//    }
//}
