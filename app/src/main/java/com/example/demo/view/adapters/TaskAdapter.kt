package com.example.demo.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.demo.ItemClickListener
import com.example.demo.databinding.TaskItemBinding
import com.example.demo.model.TaskModel

class TaskAdapter(context: Context) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    private var taskList: ArrayList<TaskModel>? = ArrayList()
    private var itemClickListener: ItemClickListener = context as ItemClickListener

    fun setTaskList(taskList: List<TaskModel>?) {
        if (taskList != null) {
            this.taskList = taskList as ArrayList<TaskModel>
            notifyDataSetChanged()
        }
    }

    class ViewHolder(val binding: TaskItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            TaskItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.dateTv.text = taskList!![position].date
        holder.binding.taskTv.text = taskList!![position].task
        holder.binding.closeImv.setOnClickListener {
            itemClickListener.onItemClicked(taskList!![position])
        }
    }

    override fun getItemCount(): Int {
        return taskList?.size ?: 0
    }
}