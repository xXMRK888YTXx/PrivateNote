package com.xxmrk888ytxx.privatenote.presentation.Screen.BackupSettingsScreen

import android.content.Context
import com.xxmrk888ytxx.privatenote.R
import kotlinx.collections.immutable.persistentListOf

data class RepeatAutoBackupTimeItem(
    val title:Int,
    val timeAtHours:Long
) {
    companion object {
        fun getDropDownList() = persistentListOf(
            RepeatAutoBackupTimeItem(
                title = R.string.three_hours,
                timeAtHours = 3
            ),
            RepeatAutoBackupTimeItem(
                title = R.string.five_hours,
                timeAtHours = 5
            ),
            RepeatAutoBackupTimeItem(
                title = R.string.Eight_hours,
                timeAtHours = 8
            ),
            RepeatAutoBackupTimeItem(
                title = R.string.Twelve_hours,
                timeAtHours = 12
            ),
            RepeatAutoBackupTimeItem(
                title = R.string.twenty_four_hours,
                timeAtHours = 24
            ),
            RepeatAutoBackupTimeItem(
                title = R.string.Forty_eight_hours,
                timeAtHours = 48
            ))

        fun getDropDownItemByTime(time:Long) : RepeatAutoBackupTimeItem {
            return getDropDownList().first { it.timeAtHours == time }
        }
    }
}