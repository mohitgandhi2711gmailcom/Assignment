package com.example.demo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demo.database.TaskDao
import com.example.demo.helper.DBHelper
import com.example.demo.helper.Utils
import com.example.demo.model.TaskModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TaskViewModel : ViewModel() {

    val taskDataMutableList = MutableLiveData<List<TaskModel>>()

    /**
     * Insert Data in DB
     * */
    fun insertDataInDB(taskDao: TaskDao, taskData: String, dateData: String) {
        viewModelScope.launch(
            Dispatchers.IO
        ) {
            DBHelper.insertTaskModel(
                taskDao,
                Utils.getTaskModel(taskData, dateData)
            )
        }
    }

    /*
    * Filter Data Based on Month
    * */
    fun filterListBasedOnMonth(taskDao: TaskDao, monthValue: Int) {
        viewModelScope.launch(Dispatchers.IO) { fetchFilterList(taskDao, monthValue) }
    }

    /*
    * Filter Data Based on Month
    * */
    private suspend fun fetchFilterList(taskDao: TaskDao, monthValue: Int) {
        val async = viewModelScope.async(Dispatchers.IO) {
            DBHelper.getMonthFilerTaskList(
                taskDao,
                monthValue
            )
        }
        val dataList = async.await()
        taskDataMutableList.postValue(dataList)
    }

    /**
     * Delete Particular TaskModel from DB
     * */
    fun deleteTaskModel(taskDao: TaskDao, taskModel: TaskModel) {
        viewModelScope.launch(Dispatchers.IO) {
            DBHelper.deleteTaskFromList(
                taskDao,
                taskModel = taskModel
            )
        }
    }
}