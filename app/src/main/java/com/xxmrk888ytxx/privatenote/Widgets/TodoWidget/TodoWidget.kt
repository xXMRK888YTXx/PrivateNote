package com.xxmrk888ytxx.privatenote.Widgets.TodoWidget

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.*
import androidx.glance.action.clickable
import androidx.glance.appwidget.CheckBox
import androidx.glance.appwidget.CheckBoxColors
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Widgets.Actions.TodoWidgetActions.OpenAppAction
import com.xxmrk888ytxx.privatenote.presentation.theme.CardNoteColor
import com.xxmrk888ytxx.privatenote.presentation.theme.FloatingButtonColor
import com.xxmrk888ytxx.privatenote.presentation.theme.PrimaryFontColor

class TodoWidget : GlanceAppWidget() {

    @Composable
    override fun Content() {
        val context = LocalContext.current
        Column(
            modifier =
            GlanceModifier.fillMaxSize()
                .background(CardNoteColor)
                .cornerRadius(20.dp)
        ) {
            Row(
                modifier = GlanceModifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(context.getString(R.string.ToDo),
                    style = TextStyle(color = ColorProvider(PrimaryFontColor),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
                Box(
                    modifier = GlanceModifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Image(provider = ImageProvider(R.drawable.ic_plus_for_todo_widget),
                        contentDescription = "",
                        modifier = GlanceModifier.clickable(actionRunCallback<OpenAppAction>())
                    )
                }

            }
            Diver()

            }
        }
    @Composable
    private fun Diver() {
        Box(
            modifier = GlanceModifier.fillMaxWidth().height(1.dp).background(ColorProvider(PrimaryFontColor))
        ) {

        }
    }
}