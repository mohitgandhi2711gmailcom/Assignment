package com.example.demo.helper

import androidx.lifecycle.LiveData
import com.example.demo.database.TaskDao
import com.example.demo.model.TaskModel

object DBHelper {
    fun insertTaskModel(taskDao: TaskDao, taskModel: TaskModel) {
        taskDao.insertTask(taskModel)
    }

    fun getTaskLiveList(taskDao: TaskDao): LiveData<List<TaskModel>> {
        return taskDao.getAllTaskListLive()
    }

    fun deleteTaskFromList(taskDao: TaskDao,taskModel:TaskModel){
        taskDao.delete(taskModel)
    }

    fun getMonthFilerTaskList(taskDao: TaskDao, month:Int): List<TaskModel> {
        return taskDao.getMonthFilterTaskList(month)
    }
}