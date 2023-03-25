package com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.ScreenState.ToDoScreen

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.Context
import android.os.Build
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.xxmrk888ytxx.privatenote.data.Database.Entity.TodoItem
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Utils.LazySpacer
import com.xxmrk888ytxx.privatenote.Utils.Remember
import com.xxmrk888ytxx.privatenote.presentation.Screen.MainScreen.MainScreenController
import com.xxmrk888ytxx.privatenote.presentation.MultiUse.YesNoButtons.YesNoButton
import com.xxmrk888ytxx.privatenote.presentation.MultiUse.YesNoDialog.YesNoDialog
import com.xxmrk888ytxx.privatenote.Utils.secondToData
import com.xxmrk888ytxx.privatenote.Utils.themeColors
import com.xxmrk888ytxx.privatenote.presentation.MultiUse.DontKillMyAppDialog.DontKillMyAppDialog
import kotlinx.coroutines.launch
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@Composable
fun ToDoScreen(
    toDoViewModel: ToDoViewModel = hiltViewModel(),
    mainScreenController: MainScreenController,
) {
    val state = remember {
        toDoViewModel.getScreenState()
    }
    val notifyDialogState = remember {
        toDoViewModel.getNotifyDialogState()
    }
    val isRemoveDialogShow = remember {
        toDoViewModel.isRemoveDialogShow()
    }
    val dontKillMyAppState = toDoViewModel.getDontKillMyAppDialogState().Remember()
    val requestPermissionSendAlarmsDialog = toDoViewModel
        .getRequestPermissionSendAlarmsDialog()
        .Remember()
    LaunchedEffect(key1 = Unit, block = {
        toDoViewModel.setMainScreenController(mainScreenController)
        toDoViewModel.checkPickers()
    })
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(themeColors.mainBackGroundColor)
    ) {
        TopLabel(toDoViewModel)
        ToDoList(toDoViewModel)
    }
    if (state.value == ToDoScreenState.EditToDoDialog) {
        EditToDoDialog(toDoViewModel)
        if (notifyDialogState.value) {
            NotifyDialog(toDoViewModel)
        }
    }
    if (isRemoveDialogShow.value.first) {
        YesNoDialog(title = stringResource(R.string.Remove_this_todo),
            onCancel = { toDoViewModel.hideRemoveDialog() }) {
            if (isRemoveDialogShow.value.second == null) return@YesNoDialog
            toDoViewModel.removeToDo(isRemoveDialogShow.value.second!!)
            toDoViewModel.hideRemoveDialog()
        }
    }
    if (requestPermissionSendAlarmsDialog.value) {
        YesNoDialog(
            title = stringResource(R.string.Request_user_for_send_alarms),
            confirmButtonText = stringResource(R.string.Yes),
            onCancel = {
                toDoViewModel.hideRequestPermissionSendAlarmsDialog()
            },
            onConfirm = {
                toDoViewModel.hideRequestPermissionSendAlarmsDialog()
                toDoViewModel.openAlarmSettings()
            }
        )
    }
    if (dontKillMyAppState.value.first) {
        DontKillMyAppDialog(
            onHideDialogForever = {
                toDoViewModel.hideDontKillMyAppDialogForever()
            },
            onDismissRequest = {
                toDoViewModel.hideDontKillMyAppDialog()
            },
            onExecuteAfterCloseDialog = {
                dontKillMyAppState.value.second?.invoke()
            }
        )
    }
    LaunchedEffect(key1 = Unit, block = {
        launch {
            toDoViewModel.checkDeepLinks()
        }
    })
}

@OptIn(ExperimentalPermissionsApi::class)
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
    val notificationPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(
            POST_NOTIFICATIONS
        )
    } else {
        null
    }
    val context = LocalContext.current
    val toDoEditItems = listOf(
        ToDoEditItem(
            icon = R.drawable.ic_priority_high,
            activate = currentToDoImpotent.value,
            activateColor = Color.Red.copy(0.9f),
            deActivateColor = themeColors.primaryFontColor
        ) {
            toDoViewModel.changeImpotentStatus()
        },
        ToDoEditItem(
            icon = R.drawable.ic_calendar,
            activate = currentToDoTime.value != null,
            activateColor = themeColors.secondaryColor,
            deActivateColor = themeColors.primaryFontColor
        ) {
            if (currentToDoTime.value == null)
                toDoViewModel.showDataPickerDialog(context)
            else toDoViewModel.removeCurrentToDoTime()
        },
        ToDoEditItem(
            icon = R.drawable.ic_notifications,
            activate = currentNotifyTime.value != null,
            activateColor = themeColors.yellow,
            deActivateColor = themeColors.primaryFontColor
        ) {
            toDoViewModel.showDontKillMyAppDialog {
                toDoViewModel.showNotifyDialog(permission = notificationPermissionState)
            }
        },
    )
    val textField = remember {
        toDoViewModel.dialogTextField
    }
    Dialog(
        onDismissRequest = { toDoViewModel.toDefaultMode() },

        ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            backgroundColor = themeColors.cardColor
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = textField.value,
                    onValueChange = {
                        if (it.length > 90) return@OutlinedTextField
                        textField.value = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    singleLine = true,
                    label = {
                        Text(
                            text = stringResource(R.string.Task),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = themeColors.primaryFontColor
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = themeColors.primaryFontColor,
                        backgroundColor = themeColors.searchColor,
                        placeholderColor = themeColors.primaryFontColor.copy(0.7f),
                        focusedBorderColor = themeColors.primaryFontColor,
                        focusedLabelColor = themeColors.primaryFontColor,
                        cursorColor = themeColors.primaryFontColor,
                        unfocusedLabelColor = themeColors.primaryFontColor.copy(0.6f)
                    ),
                    textStyle = TextStyle(
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp
                    ),
                )
                Row(modifier = Modifier.fillMaxWidth()) {
                    LazyRow(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        items(toDoEditItems) {
                            val tint = if (it.activate) it.activateColor else it.deActivateColor
                            Row(verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable {
                                    it.onClick()
                                }
                            ) {
                                Icon(
                                    painter = painterResource(it.icon),
                                    contentDescription = "",
                                    tint = tint,
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .size(30.dp)
                                )
                            }
                        }
                    }
                    Box(
                        contentAlignment = Alignment.CenterEnd,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedButton(
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = themeColors.secondaryColor,
                                disabledBackgroundColor = themeColors.secondaryColor.copy(0.3f)
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
                            Text(
                                text = stringResource(R.string.Save),
                                color = themeColors.primaryFontColor
                            )
                        }
                    }
                }

            }
        }
    }
    if (showDataPickerDialog.value) {

    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun ToDoList(toDoViewModel: ToDoViewModel) {
    val toDoList = toDoViewModel.getToDoList().collectAsState(toDoViewModel.cachedToDoList)
    val isCompletedToDoVisible = toDoViewModel.isCompletedToDoVisible().collectAsState(true)
    val isToDoWithDateVisible = toDoViewModel.isToDoWithDateVisible().collectAsState(true)
    val isToDoWithoutDateVisible = toDoViewModel.isToDoWithoutDateVisible().collectAsState(true)
    val isMissedToDoVisible = toDoViewModel.isMissedToDoVisible().collectAsState(true)
    val categoryList = listOf<TodoCategory>(
        TodoCategory(
            categoryName = stringResource(R.string.Overdue),
            toDoList.value,
            isMissedToDoVisible.value,
            onVisibleChange = {
                toDoViewModel.changeMissedToDoVisible()
            },
            validator = {
                return@TodoCategory it.filter {
                    it.todoTime != null &&
                            !it.isCompleted && it.todoTime < System.currentTimeMillis()
                }
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
                return@TodoCategory it.filter {
                    it.todoTime != null
                            && !it.isCompleted && it.todoTime > System.currentTimeMillis()
                }
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
                return@TodoCategory it.filter { it.todoTime == null && !it.isCompleted }
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
                tint = themeColors.secondaryFontColor,
                modifier = Modifier.padding(top = 12.dp)
            )
        },
        "invisible" to InlineTextContent(
            Placeholder(50.sp, 50.sp, PlaceholderVerticalAlign.TextCenter)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_drop_up),
                contentDescription = "",
                tint = themeColors.secondaryFontColor,
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    )
    AnimatedVisibility(
        visible = toDoList.value.isNotEmpty(),
        enter = slideInVertically(),
        exit = slideOutVertically()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            categoryList.forEach { category ->
                val sortedList =
                    category.validator(category.items).sortedByDescending { it.isImportant }
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
                            color = themeColors.primaryFontColor,
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
                                    tint = themeColors.primaryFontColor,
                                    modifier = Modifier.padding(start = 50.dp)
                                )
                            },
                            background = themeColors.deleteOverSwapColor,
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
                            backgroundColor = themeColors.cardColor
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
            item {
                LazySpacer(120)
            }
        }
    }
    AnimatedVisibility(
        visible = toDoList.value.isEmpty(),
        enter = scaleIn(),
        exit = scaleOut()
    ) {
        ToDoStub()
    }
    SideEffect {
        toDoViewModel.cachedToDoList = toDoList.value
    }

}

@Composable
fun ToDoStub() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 100.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_todo_icon),
            contentDescription = "",
            tint = themeColors.primaryFontColor,
            modifier = Modifier.size(100.dp)
        )
        Text(
            text = stringResource(R.string.Todo_stub_text),
            fontSize = 20.sp,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Medium,
            color = themeColors.secondaryFontColor,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
fun ToDoItem(todo: TodoItem, toDoViewModel: ToDoViewModel) {
    val fontColor =
        if (todo.isCompleted) themeColors.primaryFontColor.copy(0.3f) else themeColors.primaryFontColor
    val todoTimeText = remember {
        mutableStateOf("")
    }

    val theme = themeColors

    val subTextColor: MutableState<Color> = remember {
        mutableStateOf(theme.secondaryFontColor)
    }
    val task = toDoViewModel.getTask(todo.id).collectAsState(null)
    todoTimeText.value = getTodoSubText(todo, LocalContext.current)
    subTextColor.value = getTodoSubTextColor(todo)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp, bottom = 5.dp)
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = todo.isCompleted,
                onCheckedChange = { toDoViewModel.changeMarkStatus(it, todo.id) },
                modifier = Modifier.padding(start = 0.dp),
                colors = CheckboxDefaults.colors(
                    checkedColor = themeColors.secondaryColor,
                    checkmarkColor = themeColors.primaryFontColor,
                    uncheckedColor = themeColors.secondaryColor
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
                    .padding(start = 5.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = fontColor,
                fontStyle = FontStyle.Italic,
            )
            if (task.value != null && todo.todoTime == null && !todo.isCompleted) {
                Box(
                    Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_notifications),
                        contentDescription = "",
                        tint = themeColors.yellow,
                        modifier = Modifier
                            .padding(end = 5.dp)
                            .size(20.dp)
                    )
                }
            }
        }
        if (!todo.isCompleted && todo.todoTime != null) {
            Row(modifier = Modifier.padding(start = 10.dp)) {
                Text(
                    text = todoTimeText.value,
                    fontSize = 12.sp,
                    color = subTextColor.value,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 5.dp)
                )
                if (task.value != null) {
                    Icon(
                        painter = painterResource(R.drawable.ic_notifications),
                        contentDescription = "",
                        tint = themeColors.yellow,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun getTodoSubText(todo: TodoItem, context: Context): String {
    if (!todo.isCompleted && todo.todoTime != null) return "${todo.todoTime.secondToData(context)}"
    return ""
}

@Composable
fun getTodoSubTextColor(todo: TodoItem): Color {
    if (!todo.isCompleted && todo.todoTime != null) {
        if (todo.todoTime in 0..System.currentTimeMillis()) return Color.Red.copy(0.9f)
        //  else return Color.Cyan.copy(0.7f)
    }
    return themeColors.secondaryFontColor
}

@Composable
fun TopLabel(toDoViewModel: ToDoViewModel) {
    val todo = toDoViewModel.getToDoList().collectAsState(listOf()).value.filter {
        !it.isCompleted
    }
    val subtext = if (todo.isEmpty()) stringResource(R.string.all_task_complited) else
        "${todo.size} ${stringResource(R.string.Tasks_left)}"
    Column(modifier = Modifier.padding(start = 25.dp, bottom = 0.dp, top = 20.dp)) {
        Text(
            text = stringResource(R.string.My_tasks),
            fontWeight = FontWeight.W800,
            fontSize = 30.sp,
            color = themeColors.primaryFontColor,
        )
        Text(
            text = subtext,
            fontStyle = FontStyle.Italic,
            fontSize = 18.sp,
            color = themeColors.secondaryFontColor,
            modifier = Modifier.padding(top = 5.dp)
        )
    }
}

@Composable
fun NotifyDialog(toDoViewModel: ToDoViewModel) {
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
    val textAlpha: Float by animateFloatAsState(
        if (notifyEnabled.value) 1f else 0.3f
    )
    val context = LocalContext.current
    val timeText =
        if (currentNotifyTime.value != null) currentNotifyTime.value!!.secondToData(context)
        else stringResource(R.string.Select_time)
    Dialog(onDismissRequest = { toDoViewModel.hideNotifyDialog() }) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10),
            backgroundColor = themeColors.cardColor
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 0.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.Reminder_on),
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold,
                        color = themeColors.primaryFontColor
                    )
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Switch(
                            checked = notifyEnabled.value,
                            onCheckedChange = {
                                notifyEnabled.value = it
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = themeColors.secondaryColor,
                                uncheckedThumbColor = themeColors.secondaryFontColor
                            ),
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.Reminder_in),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = themeColors.primaryFontColor.copy(textAlpha)
                    )
                    Box(
                        Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(text = timeText,
                            fontSize = 14.sp,
                            color = themeColors.primaryFontColor.copy(textAlpha),
                            modifier = Modifier.clickable(
                                enabled = notifyEnabled.value
                            ) {
                                toDoViewModel.showPickerNotifyTimeDialog(context)
                            }
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.Priority),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = themeColors.primaryFontColor.copy(textAlpha)
                    )
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Switch(
                            checked = isCurrentNotifyPriority.value,
                            onCheckedChange = {
                                isCurrentNotifyPriority.value = it
                            },
                            enabled = notifyEnabled.value,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = themeColors.secondaryColor,
                                uncheckedThumbColor = themeColors.secondaryFontColor
                            ),
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    }
                }
                YesNoButton(
                    onCancel = { toDoViewModel.cancelNotifyDialog() },
                    modifier = Modifier.padding(top = 20.dp),
                    isOkButtonEnable = currentNotifyTime.value != null
                ) {
                    toDoViewModel.confirmNotifyDialog()
                }
            }
        }
    }
}