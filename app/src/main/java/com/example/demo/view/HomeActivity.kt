package com.example.demo.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.example.demo.R
import com.example.demo.database.TaskDatabase
import com.example.demo.databinding.ActivityHomeBinding
import com.example.demo.helper.AlertPopup
import com.example.demo.helper.DBHelper
import com.example.demo.helper.Utils
import com.example.demo.listeners.AlertPopupListener
import com.example.demo.listeners.ItemClickListener
import com.example.demo.model.TaskModel
import com.example.demo.view.adapters.TaskAdapter
import com.example.demo.viewmodel.TaskViewModel
import com.github.sundeepk.compactcalendarview.CompactCalendarView.CompactCalendarViewListener
import org.joda.time.DateTimeComparator
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class HomeActivity : AppCompatActivity(), ItemClickListener, AlertPopupListener {

    private lateinit var binding: ActivityHomeBinding
    private val viewModel: TaskViewModel by viewModels()
    private lateinit var taskAdapter: TaskAdapter
    private val taskList = ArrayList<TaskModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        doInitialSetup()
        observeChangesInDB()
    }

    /**
     * Initial Setup
     * */
    private fun doInitialSetup() {
        binding.calenderView.visibility = View.GONE
        binding.calenderView.setUseThreeLetterAbbreviation(true)
        prepareRecyclerView()
        setUpClickListeners()
    }

    /**
     * Set up Recycler View for Task List
     * */
    private fun prepareRecyclerView() {
        taskAdapter = TaskAdapter(this)
        binding.rvTasks.apply {
            layoutManager = GridLayoutManager(applicationContext, 1)
            adapter = taskAdapter
        }
    }

    /**
     * Setting Up Click Listeners
     * */
    private fun setUpClickListeners() {
        binding.showCalenderBtn.setOnClickListener {
            if (binding.calenderView.isVisible) {
                binding.calenderView.visibility = View.GONE
                binding.showCalenderBtn.text = getString(R.string.show_calender)
            } else {
                binding.calenderView.visibility = View.VISIBLE
                binding.showCalenderBtn.text = getString(R.string.hide_calender)
            }
        }

        binding.addTaskBtn.setOnClickListener {
            AlertPopup.showPopup(this, null, taskList)
        }

        binding.calenderView.setListener(object : CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date) {
                onDateClick(dateClicked)
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date) {
                onMonthChanged(firstDayOfNewMonth)
            }
        })
    }

    /**
     * Observer Changes in DB for Task List
     * */
    private fun observeChangesInDB() {
        DBHelper.getTaskLiveList(TaskDatabase.getDatabase(this@HomeActivity).taskDao())
            .observe(this) { taskList ->
                updateListInAdapter(taskList)
            }

        viewModel.taskDataMutableList.observe(this) {
            updateListInAdapter(it)
        }
    }

    /**
     * Manage the Date & Update the List
     * */
    private fun updateListInAdapter(dataList: List<TaskModel>) {
        taskList.clear()
        taskList.addAll(dataList)
        highlightTheCalender()
        taskAdapter.setTaskList(dataList)
    }

    /**
     * Do Insertion of Data
     * */
    override fun doSaveDataInDB(taskData: String, dateData: String) {
        viewModel.insertDataInDB(
            TaskDatabase.getDatabase(this@HomeActivity).taskDao(),
            taskData,
            dateData
        )
    }

    /**
     * On Click of any Date on Calender
     * */
    fun onDateClick(selectedDate: Date) {
        val currentDate = Date()
        val dateOnlyInstance = DateTimeComparator.getDateOnlyInstance()
        val compare = dateOnlyInstance.compare(currentDate, selectedDate)
        when {
            compare > 0 -> {
                Toast.makeText(this, "Please select Future Dates", Toast.LENGTH_SHORT).show()
            }
            else -> {
                AlertPopup.showPopup(this, selectedDate, taskList)
            }
        }
    }

    /**
     * On Click of any Month Change in Calender
     * */
    fun onMonthChanged(monthDate: Date) {
        val localDate: LocalDate =
            monthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        viewModel.filterListBasedOnMonth(
            TaskDatabase.getDatabase(this@HomeActivity).taskDao(),
            localDate.monthValue
        )
    }


    /*
    *  Delete Task from DB
    * */
    override fun onItemClicked(taskModel: TaskModel) {
        viewModel.deleteTaskModel(TaskDatabase.getDatabase(this@HomeActivity).taskDao(), taskModel)
    }

    /**
     * High-Light The Event Days
     * */
    private fun highlightTheCalender() {
        binding.calenderView.removeAllEvents()
        val eventsList = Utils.fetchTaskEvents(taskList)
        binding.calenderView.addEvents(eventsList)
    }
}