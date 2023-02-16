package com.example.demo.helper

import androidx.lifecycle.LiveData
import com.example.demo.database.TaskDao
import com.example.demo.model.TaskModel

object DBHelper {
    fun insertTaskModel(taskDao: TaskDao, movieModel: TaskModel) {
        taskDao.insertTask(movieModel)
    }

    fun getTaskLiveList(taskDao: TaskDao): LiveData<List<TaskModel>> {
        return taskDao.getAllTaskListLive()
    }

    fun getTaskList(taskDao: TaskDao): List<TaskModel> {
        return taskDao.getAllTaskList()
    }

    fun deleteTaskFromList(taskDao: TaskDao,taskModel:TaskModel){
        taskDao.delete(taskModel)
    }

    fun updateTaskInList(taskDao: TaskDao,taskModel:TaskModel){
        taskDao.updateTask(taskModel)
    }

    fun getMonthFilerTaskList(taskDao: TaskDao, month:Int): List<TaskModel> {
        return taskDao.getMonthFilterTaskList(month)
    }
}