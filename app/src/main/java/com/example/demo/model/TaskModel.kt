package com.example.demo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class TaskModel(

    @PrimaryKey(autoGenerate = true)
    val taskModelId: Long,
    val date: String? = null,
    val day: Int? = null,
    val month: Int? = null,
    val year: Int? = null,
    val task: String? = null,
)