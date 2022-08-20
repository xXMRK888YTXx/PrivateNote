package com.xxmrk888ytxx.privatenote.Screen.MainScreen.ScreenState.ToDoScreen

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
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
import com.xxmrk888ytxx.privatenote.Screen.MultiUse.YesNoDialog.YesNoDialog
import com.xxmrk888ytxx.privatenote.Utils.secondToData
import com.xxmrk888ytxx.privatenote.ui.theme.*
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import me.saket.swipe.rememberSwipeableActionsState

@Composable
fun ToDoScreen(toDoViewModel: ToDoViewModel = hiltViewModel(),mainScreenController: MainScreenController) {
    val state = remember {
        toDoViewModel.getScreenState()
    }
    val notifyDialogState = remember {
        toDoViewModel.getNotifyDialogState()
    }
    val isRemoveDialogShow = remember {
        toDoViewModel.isRemoveDialogShow()
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
        TopLabel(toDoViewModel)
        ToDoList(toDoViewModel)
    }
    if(state.value == ToDoScreenState.EditToDoDialog) {
        EditToDoDialog(toDoViewModel)
        if(notifyDialogState.value) {
           NotifyDialog(toDoViewModel)
        }
    }
    if(isRemoveDialogShow.value.first) {
        YesNoDialog(title = stringResource(R.string.Remove_this_todo),
            onCancel = { toDoViewModel.hideRemoveDialog() }) {
            if(isRemoveDialogShow.value.second == null) return@YesNoDialog
            toDoViewModel.removeToDo(isRemoveDialogShow.value.second!!)
            toDoViewModel.hideRemoveDialog()
        }
    }
}

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
            activateColor = Yellow
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
    val isCompletedToDoVisible = toDoViewModel.isCompletedToDoVisible().collectAsState(true)
    val isToDoWithDateVisible = toDoViewModel.isToDoWithDateVisible().collectAsState(true)
    val isToDoWithoutDateVisible = toDoViewModel.isToDoWithoutDateVisible().collectAsState(true)
    val isMissedToDoVisible =  toDoViewModel.isMissedToDoVisible().collectAsState(true)
    val categoryList = listOf<TodoCategory>(
        TodoCategory(
            categoryName = stringResource(R.string.Overdue),
            toDoList.value,
            isMissedToDoVisible.value,
            onVisibleChange = {
                toDoViewModel.changeMissedToDoVisible()
            },
            validator = {
                return@TodoCategory it.filter { it.todoTime != null&&
                        !it.isCompleted&&it.todoTime < System.currentTimeMillis() }
            }
        ),
        TodoCategory(
            categoryName = stringResource(R.string.Later),
            toDoList.value,
            isToDoWithDateVisible.value,
            onVisibleChange = {
                toDoViewModel.changeToDoWithDateVisible()
            },
            validator = {
                return@TodoCategory it.filter { it.todoTime != null
                        &&!it.isCompleted&&it.todoTime > System.currentTimeMillis() }
            }
        ),
        TodoCategory(
            categoryName = stringResource(R.string.No_date),
            toDoList.value,
            isToDoWithoutDateVisible.value,
            onVisibleChange = {
                toDoViewModel.changeToDoWithoutDateVisible()
            },
            validator = {
                return@TodoCategory it.filter { it.todoTime == null&&!it.isCompleted }
            }
        ),
        TodoCategory(
            categoryName = stringResource(R.string.Completed),
            toDoList.value,
            isCompletedToDoVisible.value,
            onVisibleChange = {
                toDoViewModel.changeCompletedToDoVisible()
            },
            validator = {
                return@TodoCategory it.filter { it.isCompleted }
            }
        ),
    )
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
        categoryList.forEach { category ->
            val sortedList = category.validator(category.items).sortedByDescending { it.isImportant }
            itemsIndexed(sortedList, key = { _, it ->
                it.id
            }) { index, it ->
                val annotatedLabelString = buildAnnotatedString {
                    append(category.categoryName)
                    if (category.visible)
                        appendInlineContent("visible")
                    else appendInlineContent("invisible")
                }
                if (index == 0) {
                    Text(
                        text = annotatedLabelString,
                        inlineContent = inlineContentMap,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryFontColor,
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItemPlacement()
                            .clickable {
                                category.onVisibleChange()
                            }
                            .padding(start = 15.dp, top = 0.dp, bottom = 0.dp)
                    )
                }
                if (category.visible) {
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
                            toDoViewModel.showRemoveDialog(it.id)
                        },
                        isUndo = true
                    )
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .clickable(
                                enabled = !it.isCompleted
                            ) {
                                toDoViewModel.toEditToDoState(it)
                            }
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
    val task = toDoViewModel.getTask(todo.id).collectAsState(null)
    todoTimeText.value = getTodoSubText(todo, LocalContext.current)
    subTextColor.value = getTodoSubTextColor(todo)
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 5.dp, bottom = 5.dp)) {
        Row(
            modifier = Modifier,
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
                        ,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = fontColor,
                    fontStyle = FontStyle.Italic,
                )
            if(task.value != null&&todo.todoTime == null&&!todo.isCompleted) {
                Box(Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd) {
                    Icon(painter = painterResource(R.drawable.ic_notifications),
                        contentDescription = "",
                        tint = Yellow,
                        modifier = Modifier
                            .padding(end = 5.dp)
                            .size(20.dp)
                    )
                }
            }
        }
        if(!todo.isCompleted&&todo.todoTime != null) {
            Row(modifier = Modifier.padding(start = 10.dp)) {
                Text(text = todoTimeText.value,
                    fontSize = 12.sp,
                    color = subTextColor.value,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 5.dp)
                )
                if(task.value != null) {
                    Icon(painter = painterResource(R.drawable.ic_notifications),
                        contentDescription = "",
                        tint = Yellow,
                        modifier = Modifier.size(15.dp)
                    )
                }
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
fun TopLabel(toDoViewModel: ToDoViewModel) {
    val todo = toDoViewModel.getToDoList().collectAsState(listOf()).value.filter {
        !it.isCompleted
    }
    val subtext = if(todo.isEmpty()) stringResource(R.string.all_task_complited) else
          "${todo.size} ${stringResource(R.string.Tasks_left)}"
    Column(modifier = Modifier.padding(start = 25.dp, bottom = 0.dp, top = 20.dp)) {
        Text(text = "Все задачи",
            fontWeight = FontWeight.W800,
            fontSize = 30.sp,
            color = PrimaryFontColor,
            modifier = Modifier.clickable {
            }
        )
        Text(
            text = subtext,
            fontStyle = FontStyle.Italic,
            fontSize = 18.sp,
            color = SecondoryFontColor,
            modifier = Modifier.padding(top = 5.dp)
        )
    }
}

@Composable
fun NotifyDialog(toDoViewModel: ToDoViewModel) {
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
    val isCurrentNotifyPriority = remember {
        toDoViewModel.isCurrentNotifyPriority()
    }
    LaunchedEffect(key1 = Unit, block = {
        notifyEnabled.value = currentNotifyTime.value != null
    })
    val textAlpha:Float by animateFloatAsState(
        if(notifyEnabled.value) 1f else 0.3f
    )
    val context = LocalContext.current
    val timeText = if(currentNotifyTime.value != null) currentNotifyTime.value!!.secondToData(context)
    else stringResource(R.string.Select_time)
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
                    Text(text = stringResource(R.string.Reminder_on),
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
                    Text(text = stringResource(R.string.Reminder_in),
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
                            modifier = Modifier.clickable(
                                enabled = notifyEnabled.value
                            ) {
                                toDoViewModel.showPickerNotifyTimeDialog(context)
                            }
                        )
                    }
                }
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = stringResource(R.string.Priority),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = PrimaryFontColor.copy(textAlpha)
                    )
                    Box(modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Switch(
                            checked = isCurrentNotifyPriority.value,
                            onCheckedChange = {
                                isCurrentNotifyPriority.value = it
                            },
                            enabled = notifyEnabled.value,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = FloatingButtonColor,
                                uncheckedThumbColor = SecondoryFontColor
                            ),
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    }
                }
                YesNoButton(onCancel = { toDoViewModel.cancelNotifyDialog() },
                modifier = Modifier.padding(top = 20.dp),
                    isOkButtonEnable = currentNotifyTime.value != null
                    ) {
                    toDoViewModel.confirmNotifyDialog()
                }
            }
        }
    }
}