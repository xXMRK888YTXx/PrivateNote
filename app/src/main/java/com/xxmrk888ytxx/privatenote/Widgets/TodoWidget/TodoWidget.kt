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
import java.io.File

class TodoWidget : GlanceAppWidget() {
    override val stateDefinition: GlanceStateDefinition<*>
        get() = CustomGlanceStateDefinition



    override val sizeMode: SizeMode
        get() = SizeMode.Exact

    private val widgetState: MutableState<WidgetState> = mutableStateOf(WidgetState.EmptyTodoList)

    private fun updateState(preferences: Preferences) {
        try {
            val moshi: Moshi = Moshi.Builder().build()
            val jsonAdapter: JsonAdapter<TodoWidgetDataModel> =
                moshi.adapter(TodoWidgetDataModel::class.java)
            var jsonString = preferences[widgetDataKey] ?: ""
            if (jsonString.isEmpty()) {
                widgetState.value = WidgetState.EmptyTodoList
                return
            }
            val model = jsonAdapter.fromJson(jsonString)
            if (model == null) {
                widgetState.value = WidgetState.EmptyTodoList
                return
            }
            model.ifNotNull {
                if (it.todoList.isEmpty()) {
                    widgetState.value = WidgetState.EmptyTodoList
                } else {
                    widgetState.value = WidgetState.ShowTodo(it)
                }
            }
        } catch (e: Exception) {
            widgetState.value = WidgetState.Error
        }
    }

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            Content()
        }
    }

    @Composable
    fun Content() {
        val context = LocalContext.current
        val pref = currentState<Preferences>()
        LaunchedEffect(key1 = Unit, block = {
            updateState(pref)
        })

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
                        val model = (widgetState.value as WidgetState.ShowTodo).data
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
    fun CreateTodoList(model: TodoWidgetDataModel) {
        LazyColumn() {
            itemsIndexed(model.todoList, itemId = { _,it -> it.id.toLong() }) { index,it ->
                Column() {
                    TodoListItem(it)

                    if(index != model.todoList.lastIndex)
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

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = WIDGET_DATA_PREFERENCE_NAME)

    private object CustomGlanceStateDefinition : GlanceStateDefinition<Preferences> {
        override suspend fun getDataStore(
            context: Context,
            fileKey: String,
        ): DataStore<Preferences> {
            return context.dataStore
        }

        override fun getLocation(context: Context, fileKey: String): File {
            return File(
                context.applicationContext.filesDir,
                "datastore/$WIDGET_DATA_PREFERENCE_NAME"
            )
        }

        private val Context.dataStore: DataStore<Preferences>
                by preferencesDataStore(name = WIDGET_DATA_PREFERENCE_NAME)

    }

    companion object {
        const val WIDGET_DATA_PREFERENCE_NAME = "widget_data"
        val widgetDataKey = stringPreferencesKey("WidgetData")
    }
}