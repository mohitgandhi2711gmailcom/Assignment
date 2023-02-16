package com.example.demo.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.demo.model.TaskModel
import java.time.Month

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(taskModel: TaskModel)

    @Update
    fun updateTask(taskModel: TaskModel)

    @Delete
    fun delete(taskDate: TaskModel)

    @Query("SELECT * FROM task")
    fun getAllTaskListLive(): LiveData<List<TaskModel>>

    @Query("SELECT * FROM task")
    fun getAllTaskList(): List<TaskModel>

    @Query("SELECT * FROM task WHERE month LIKE :month")
    fun getMonthFilterTaskList(month: Int): List<TaskModel>
}