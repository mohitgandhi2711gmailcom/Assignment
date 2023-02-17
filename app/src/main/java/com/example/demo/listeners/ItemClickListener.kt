package com.example.demo.listeners

import com.example.demo.model.TaskModel

/**
 * Used for Getting the Click Event of task List
 * */
interface ItemClickListener {

    fun onItemClicked(taskModel:TaskModel)

}