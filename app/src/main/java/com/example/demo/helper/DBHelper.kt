package com.example.demo.helper

import androidx.lifecycle.LiveData
import com.example.demo.database.TaskDao
import com.example.demo.model.TaskModel

object DBHelper {

    fun insertTaskList(taskDao: TaskDao, taskList: List<TaskModel>) {
        for (element in taskList) {
            taskDao.insertTask(element)
        }
    }

    fun insertTaskModel(taskDao: TaskDao, movieModel: TaskModel) {
        taskDao.insertTask(movieModel)
    }

    fun getTaskList(taskDao: TaskDao): LiveData<List<TaskModel>> {
        return taskDao.getAllTaskList()
    }
}