package com.example.demo.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "task", indices = [Index(value = ["date"], unique = true)])
data class TaskModel(
    @PrimaryKey
    val date: String,
    val day: Int,
    val month: Int,
    val year: Int,
    val task: String,
)