package com.xxmrk888ytxx.privatenote.Screen.MultiUse.DataPicker

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.util.Log
import android.widget.DatePicker
import android.widget.TimePicker
import com.xxmrk888ytxx.privatenote.R
import java.util.*

class DataTimePicker{
    fun createDataPickerDialog(context:Context,controller: DataTimePickerController) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        calendar.time = Date()

        val datePicker = DatePickerDialog(
            context, R.style.DialogTheme,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                controller.onComplete(getSecond(year, month, dayOfMonth))
            }, year, month, day
        )
        datePicker.setCanceledOnTouchOutside(true)
        datePicker.setOnCancelListener{
            Log.d("MyLog","cancel")
        }
        datePicker.show()
    }

    private fun getSecond(year: Int, month: Int, dayOfMonth: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR,year)
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth)
        calendar.set(Calendar.MONTH,month)
        return calendar.timeInMillis / 1000
    }
}
