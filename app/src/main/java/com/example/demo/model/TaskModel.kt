package com.example.demo.model

import androidx.room.*

@Entity(tableName = "task")
data class TaskModel(

    @PrimaryKey(autoGenerate = true)
    val taskModelId: Int,

    val date: String? = null,

    val task: String? = null,
)