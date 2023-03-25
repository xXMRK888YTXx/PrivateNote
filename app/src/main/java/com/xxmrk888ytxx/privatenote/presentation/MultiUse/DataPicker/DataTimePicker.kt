package com.xxmrk888ytxx.privatenote.presentation.MultiUse.DataPicker

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.util.Log
import android.widget.DatePicker
import com.xxmrk888ytxx.privatenote.R
import java.util.*

class DataTimePicker{
    private var context:Context? = null
    private var controller:DataTimePickerController? = null
    private var calendar:Calendar? = null
    private var validator:((time:Long) -> Boolean)? = null
    private var isDropLattesDays:Boolean? = null

    fun createDataPickerDialog(context:Context,
                               controller: DataTimePickerController,
                               dropLattesDays:Boolean = true,
                               validator:((time:Long) -> Boolean)? = null
    ) {
        try {
            this.context = context
            this.controller = controller
            calendar = Calendar.getInstance()
            this.validator = validator
            this.isDropLattesDays = dropLattesDays
            dataPicker()
        }catch (e:Exception) {
            Log.d("MyLog",e.message.toString())
            onCancel()
        }

    }

    private fun dataPicker() {
        val CurrentTime = Calendar.getInstance()
        val currentYear = CurrentTime.get(Calendar.YEAR)
        val currentMonth = CurrentTime.get(Calendar.MONTH)
        val currentDay = CurrentTime.get(Calendar.DAY_OF_MONTH)
        CurrentTime.time = Date()

        val datePicker = DatePickerDialog(
            context!!, R.style.DialogTheme,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                calendar?.set(Calendar.YEAR,year)
                calendar?.set(Calendar.DAY_OF_MONTH,dayOfMonth)
                calendar?.set(Calendar.MONTH,month)
                timePicker()
            }, currentYear, currentMonth, currentDay
        )
        datePicker.setCanceledOnTouchOutside(true)
        if(isDropLattesDays == true) {
            val dateToday = Date()
            datePicker.datePicker.minDate = dateToday.time
        }
        datePicker.setOnCancelListener{
            onCancel()
        }
        datePicker.show()
    }

    private fun timePicker() {
       val timePicker =  TimePickerDialog(
            context!!,
            R.style.DialogTheme,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                calendar?.set(Calendar.HOUR_OF_DAY,hourOfDay)
                calendar?.set(Calendar.MINUTE,minute)
                onComplete()
            }, 12, 0, true,
        )
        timePicker.setCanceledOnTouchOutside(true)
        timePicker.setOnCancelListener {
            onCancel()
        }
        timePicker.show()
    }

    private fun getTime(): Long {
        calendar?.set(Calendar.SECOND,0)
        return calendar!!.timeInMillis
    }

    private fun onClear() {
        this.context = null
        this.controller = null
        calendar = null
        validator = null
    }

    private fun onComplete() {
        val time = getTime()
        if(validator != null) {
            if(!validator!!.invoke(time)) {
                onError()
                return
            }
        }
        controller?.onComplete(time)
        onClear()
    }

    private fun onCancel() {
        controller?.onCancel()
        onClear()
    }
    private fun onError() {
        controller?.onError()
        onClear()
    }


}
