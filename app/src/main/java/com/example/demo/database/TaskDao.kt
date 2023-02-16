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
    fun delete(taskModel: TaskModel)

    @Query("SELECT * FROM task")
    fun getAllTaskList(): LiveData<List<TaskModel>>
}