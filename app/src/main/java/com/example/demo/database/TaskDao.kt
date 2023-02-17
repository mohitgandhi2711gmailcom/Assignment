package com.example.demo.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.demo.model.TaskModel

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(taskModel: TaskModel)

    @Update
    fun updateTask(taskModel: TaskModel)

    @Delete
    fun delete(taskDate: TaskModel)

    @Query("SELECT * FROM task ORDER BY day ASC")
    fun getAllTaskListLive(): LiveData<List<TaskModel>>

    @Query("SELECT * FROM task ORDER BY day ASC")
    fun getAllTaskList(): List<TaskModel>

    @Query("SELECT * FROM task WHERE month LIKE :month ORDER BY day ASC")
    fun getMonthFilterTaskList(month: Int): List<TaskModel>
}