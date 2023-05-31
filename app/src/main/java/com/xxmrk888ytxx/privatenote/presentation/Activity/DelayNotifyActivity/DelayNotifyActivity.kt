@file:Suppress("DEPRECATION")

package com.xxmrk888ytxx.privatenote.presentation.Activity.DelayNotifyActivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.xxmrk888ytxx.privatenote.R
import com.xxmrk888ytxx.privatenote.Utils.ifNotNull
import com.xxmrk888ytxx.privatenote.Utils.themeColors
import com.xxmrk888ytxx.privatenote.domain.NotifyTaskManager.IntentNotifyTask
import com.xxmrk888ytxx.privatenote.presentation.MultiUse.YesNoButtons.YesNoButton
import com.xxmrk888ytxx.privatenote.presentation.theme.AppTheme
import com.xxmrk888ytxx.privatenote.presentation.theme.ThemeType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@AndroidEntryPoint
class DelayNotifyActivity : ComponentActivity(),DelayDialogController {
    private val delayNotifyViewModel:DelayNotifyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(intent.action != DELAY_TASK_ACTION) this.finish()

        val currentTask = intent.getParcelableExtra<IntentNotifyTask>(DELAY_TASK_DATA_KEY)
        if(currentTask == null) this.finish()
        val notificationId = intent.getIntExtra(NOTIFICATION_ID_KEY,-1)
        currentTask.ifNotNull {
            delayNotifyViewModel.initDialog(it,notificationId)
        }
        setContent {
            val themeId = delayNotifyViewModel.getThemeId().collectAsState(initial = ThemeType.System.id)

            AppTheme(themeId = themeId.value) {
                DelayDialog(this)
            }
        }
    }


    companion object {
        const val DELAY_TASK_ACTION = "DELAY_TASK_ACTION"
        const val DELAY_TASK_DATA_KEY = "DELAY_TASK_DATA_KEY"
        const val NOTIFICATION_ID_KEY = "NOTIFICATION_ID_KEY"

    }

    override fun onDismissRequest() {
        this.finish()
    }

    override fun getDelayTimeList(): ImmutableList<DelayTime> {
       return delayNotifyViewModel.getTimeList()
    }

    override fun getCurrentSelectedTime(): State<DelayTime> {
        return delayNotifyViewModel.currentSelectedTime
    }

    override fun changeCurrentSelectedTime(delayTime: DelayTime) {
        delayNotifyViewModel.changeCurrentSelectedTime(delayTime)
    }

    override fun onConfirmDelay() {
        delayNotifyViewModel.delayTask(this)
    }
}

@Composable
fun DelayDialog(delayDialogController: DelayDialogController) {
    val timeList = delayDialogController.getDelayTimeList()
    Dialog(onDismissRequest = { delayDialogController.onDismissRequest() }) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = themeColors.cardColor,
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(timeList) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    delayDialogController.changeCurrentSelectedTime(it)
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = it == delayDialogController.getCurrentSelectedTime().value,
                                onClick = {
                                    delayDialogController.changeCurrentSelectedTime(it)
                                },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor =  themeColors.secondaryColor,
                                    unselectedColor = themeColors.secondaryColor
                                ),
                                modifier = Modifier.padding(end = 10.dp)
                            )
                            Text(
                                text = it.title,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = themeColors.primaryFontColor,
                                maxLines = 1,
                            )
                        }
                    }
                }
                YesNoButton(
                    onConfirm = {
                         delayDialogController.onConfirmDelay()
                    },
                    onCancel = {
                       delayDialogController.onDismissRequest()
                    },
                    confirmButtonText = stringResource(R.string.Postpone)
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    val controller = object : DelayDialogController {
        override fun onDismissRequest() {

        }

        override fun getDelayTimeList(): ImmutableList<DelayTime> {
            return persistentListOf(DelayTime("1",1000),DelayTime("2",1000),DelayTime("3",1000))
        }

        override fun getCurrentSelectedTime(): State<DelayTime> {
            return mutableStateOf(DelayTime("1",1000))
        }

        override fun changeCurrentSelectedTime(delayTime: DelayTime) {

        }

        override fun onConfirmDelay() {

        }

    }
    DelayDialog(controller)
}