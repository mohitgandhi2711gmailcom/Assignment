package com.example.demo.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.demo.databinding.MovieLayoutBinding
import com.example.demo.model.TaskModel

class TaskAdapter : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    private var taskList: ArrayList<TaskModel>? = ArrayList()

    fun setTaskList(taskList: List<TaskModel>?) {
        if (taskList != null) {
            this.taskList = taskList as ArrayList<TaskModel>
            notifyDataSetChanged()
        }
    }

    class ViewHolder(val binding: MovieLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            MovieLayoutBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.movieName.text = taskList!![position].date
    }

    override fun getItemCount(): Int {
        return taskList?.size ?: 0
    }
}