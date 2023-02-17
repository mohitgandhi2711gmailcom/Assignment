package com.example.demo.helper

import android.graphics.Color
import com.example.demo.model.TaskModel
import com.github.sundeepk.compactcalendarview.domain.Event
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

object Utils {

    /**
     * Return TaskModel Object
     * */
    fun getTaskModel(taskData: String, dateData: String): TaskModel {
        val split = dateData.split("-")
        return TaskModel(
            dateData,
            split[0].toInt(),
            split[1].toInt(),
            split[2].toInt(),
            taskData
        )
    }

    /**
     * Fetch Events List
     * */
    fun fetchTaskEvents(taskList: ArrayList<TaskModel>): List<Event> {
        val list = ArrayList<Event>()
        for (element in taskList) {
            val month = if (element.month < 10) {
                "0${element.month}"
            } else {
                element.month
            }
            val day = if (element.day < 10) {
                "0${element.day}"
            } else {
                element.day
            }
            val localDate = LocalDate.parse("${element.year}-$month-$day")
            val date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
            val event = Event(Color.GREEN, date.time)
            list.add(event)
        }
        return list
    }
}