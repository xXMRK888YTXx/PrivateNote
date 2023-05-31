package com.xxmrk888ytxx.privatenote.Widgets.TodoWidget

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.glance.*
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.*
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.lazy.itemsIndexed
import androidx.glance.layout.*
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Utils.ifNotNull
import com.xxmrk888ytxx.privatenote.Utils.themeColors
import com.xxmrk888ytxx.privatenote.Widgets.Actions.TodoWidgetActions.MarkCompletedAction
import com.xxmrk888ytxx.privatenote.Widgets.Actions.TodoWidgetActions.OpenAppAction
import com.xxmrk888ytxx.privatenote.Widgets.Actions.TodoWidgetActions.OpenTodoInAppAction
import com.xxmrk888ytxx.privatenote.data.Database.Entity.TodoItem
import com.xxmrk888ytxx.privatenote.presentation.theme.AppTheme
import com.xxmrk888ytxx.privatenote.presentation.theme.ThemeType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.io.File

class TodoWidget (
    private val todoItems:Flow<List<TodoItem>>
) : GlanceAppWidget() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    override val sizeMode: SizeMode
        get() = SizeMode.Exact

    private var isCollect = false

    private val widgetState: MutableState<WidgetState> = mutableStateOf(WidgetState.EmptyTodoList)

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        startCollect(context,id)
        provideContent {
            Content()
        }
    }

    private fun startCollect(context: Context, id: GlanceId) {
        if(isCollect) return

        isCollect = true

        scope.launch {
            todoItems.collect() { todoList ->
                if(todoList.isEmpty()) {
                    widgetState.value = WidgetState.EmptyTodoList
                } else {
                    widgetState.value = WidgetState.ShowTodo(todoList)
                }
            }

            update(context, id)
        }
    }

    override suspend fun onDelete(context: Context, glanceId: GlanceId) {
        super.onDelete(context, glanceId)
        scope.cancel()
        widgetState.value = WidgetState.EmptyTodoList
    }

    @Composable
    fun Content() {
        val context = LocalContext.current

        WidgetTheme(themeType = ThemeType.Black) {
            Column(
                modifier =
                GlanceModifier.fillMaxSize()
                    .background(themeColors.cardColor)
                    .cornerRadius(20.dp)
            ) {
                Row(
                    modifier = GlanceModifier.padding(10.dp)
                        .clickable(actionRunCallback<OpenAppAction>()),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        context.getString(R.string.ToDo),
                        style = TextStyle(
                            color = ColorProvider(themeColors.primaryFontColor),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        modifier = GlanceModifier.clickable(actionRunCallback<OpenAppAction>())
                    )
                    Box(
                        modifier = GlanceModifier.fillMaxWidth()
                            .clickable(actionRunCallback<OpenTodoInAppAction>()),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Image(
                            provider = ImageProvider(R.drawable.ic_plus_for_todo_widget),
                            contentDescription = "",
                            modifier = GlanceModifier.clickable(actionRunCallback<OpenTodoInAppAction>())
                        )
                    }

                }
                Diver()
                when (widgetState.value) {
                    is WidgetState.ShowTodo -> {
                        val model = (widgetState.value as WidgetState.ShowTodo).todoList
                        CreateTodoList(model)
                    }
                    WidgetState.EmptyTodoList -> {
                        EmptyTodoStub()
                    }
                    WidgetState.Error -> {
                        Text(text = "Error")
                    }
                }
            }
        }

    }

    @Composable
    private fun EmptyTodoStub() {
        val context = LocalContext.current
        Column(
            modifier = GlanceModifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                provider = ImageProvider(R.drawable.ic_todo_icon_for_widget),
                contentDescription = "",
                modifier = GlanceModifier.size(40.dp)
            )
            Text(
                context.getString(R.string.All_task_complite),
                style = TextStyle(
                    color = ColorProvider(themeColors.primaryFontColor),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            )

        }
    }

    @Composable
    fun CreateTodoList(list:List<TodoItem>) {
        LazyColumn() {
            itemsIndexed(list, itemId = { _,it -> it.id.toLong() }) { index,it ->
                Column() {
                    TodoListItem(it)

                    if(index != list.lastIndex)
                        Diver()
                }
            }
        }
    }

    @Composable
    fun TodoListItem(it:TodoItem) {
        Row(
            modifier = GlanceModifier.fillMaxWidth().padding(10.dp)
                .clickable(
                    actionRunCallback<OpenTodoInAppAction>(
                        parameters = actionParametersOf(
                            OpenTodoInAppAction.TODO_KEY to it
                        )
                    )
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CheckBox(
                checked = it.isCompleted,
                onCheckedChange = null,
                modifier = GlanceModifier
                    .padding(end = 10.dp)
                    .clickable(
                        actionRunCallback<MarkCompletedAction>(
                            parameters = actionParametersOf(
                                MarkCompletedAction.actionWidgetKey to it
                            )
                        )
                    ),
                colors = CheckboxDefaults.colors(
                    checkedColor = ColorProvider(themeColors.secondaryColor),
                    uncheckedColor =  ColorProvider(themeColors.secondaryColor)
                ),
            )
            Text(
                text = it.todoText,
                style = TextStyle(
                    color = ColorProvider(themeColors.primaryFontColor),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                ),
                maxLines = 1,
                modifier = GlanceModifier.clickable(
                    actionRunCallback<OpenTodoInAppAction>(
                        parameters = actionParametersOf(
                            OpenTodoInAppAction.TODO_KEY to it
                        )
                    )
                )
            )
        }
    }

    @Composable
    private fun Diver() {
        Box(
            modifier = GlanceModifier.fillMaxWidth().height(1.dp)
                .background(ColorProvider(themeColors.primaryFontColor))
        ) {}
    }
}