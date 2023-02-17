package com.example.demo.helper

import android.app.DatePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import com.example.demo.R
import com.example.demo.listeners.AlertPopupListener
import com.example.demo.model.TaskModel
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

object AlertPopup {

    fun showPopup(context: Context, date: Date?, taskList: ArrayList<TaskModel>) {
        val builder = AlertDialog.Builder(context).create()
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.task_popup, null)
        builder.setView(view)

        val taskEt = view.findViewById<AppCompatEditText>(R.id.task_et)
        val selectDateBtn = view.findViewById<AppCompatButton>(R.id.select_date_btn)
        val selectDateTV = view.findViewById<AppCompatTextView>(R.id.date_selected_tv)
        val submitBtn = view.findViewById<AppCompatButton>(R.id.submit_btn)

        if (date != null) {
            val localDate: LocalDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            val year: Int = localDate.year
            val month: Int = localDate.monthValue
            val day: Int = localDate.dayOfMonth
            val tempDate = "$day-$month-$year"
            selectDateTV.text = tempDate
            for (elements in taskList) {
                if (elements.date == tempDate) {
                    taskEt.setText(elements.task)
                }
            }
        }

        submitBtn.setOnClickListener {
            val taskData: String = taskEt.text.toString()
            val dateData = selectDateTV.text.toString()
            if (taskData.isNotEmpty() && dateData.isNotEmpty()) {
                val popupListener=context as AlertPopupListener
                popupListener.doSaveDataInDB(taskData, dateData)
                builder.dismiss()
            } else {
                Toast.makeText(context, "Please Select Task & Date Both", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        selectDateBtn.setOnClickListener {
            val c = Calendar.getInstance()

            // on below line we are getting
            // our day, month and year.
            val currentYear = c.get(Calendar.YEAR)
            val currentMonth = c.get(Calendar.MONTH)
            val currentDay = c.get(Calendar.DAY_OF_MONTH)

            // on below line we are creating a
            // variable for date picker dialog.
            val datePickerDialog = DatePickerDialog(
                // on below line we are passing context.
                context,
                { view, year, monthOfYear, dayOfMonth ->
                    // on below line we are setting
                    // date to our text view.
                    val dateText = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                    selectDateTV.text = dateText
                    for (elements in taskList) {
                        if (elements.date == dateText) {
                            taskEt.setText(elements.task)
                        }
                    }
                },
                // on below line we are passing year, month
                // and day for the selected date in our date picker.
                currentYear,
                currentMonth,
                currentDay
            )
            // at last we are calling show
            // to display our date picker dialog.
            datePickerDialog.datePicker.minDate=(Date().time)
            datePickerDialog.show()
        }
        builder.setCanceledOnTouchOutside(true)
        builder.show()
    }
}