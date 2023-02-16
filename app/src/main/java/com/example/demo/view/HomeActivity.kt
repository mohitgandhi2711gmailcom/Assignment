package com.example.demo.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.demo.ItemClickListener
import com.example.demo.R
import com.example.demo.database.TaskDatabase
import com.example.demo.databinding.ActivityHomeBinding
import com.example.demo.helper.DBHelper
import com.example.demo.model.TaskModel
import com.example.demo.view.adapters.TaskAdapter
import com.example.demo.viewmodel.TaskViewModel
import io.mahendra.calendarview.widget.CalendarView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.joda.time.DateTimeComparator
import java.time.LocalDate
import java.time.ZoneId
import java.util.*


class HomeActivity : AppCompatActivity(), CalendarView.OnDateClickListener,
    CalendarView.OnMonthChangedListener, ItemClickListener {

    private lateinit var binding: ActivityHomeBinding
    private val viewModel: TaskViewModel by viewModels()
    private lateinit var movieAdapter: TaskAdapter
    private var selectedMonth: Int = 0
    private val taskList = ArrayList<TaskModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        doInitialSetup()
        prepareRecyclerView()
        observeChangesInDB()
        setUpClickListeners()
    }

    private fun doInitialSetup() {
        binding.calenderView.visibility = View.GONE
        binding.calenderView.setIsOverflowDateVisible(true);
    }


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
            showAlertPopup(null)
        }

        binding.calenderView.setOnDateClickListener(this)
        binding.calenderView.setOnMonthChangedListener(this)
    }

    private fun showAlertPopup(date: Date?) {
        val builder = AlertDialog.Builder(this)
            .create()
        val view = layoutInflater.inflate(R.layout.task_popup, null)
        builder.setView(view)

        val taskEt = view.findViewById<AppCompatEditText>(R.id.task_et)
        val selectDateBtn = view.findViewById<AppCompatButton>(R.id.select_date_btn)
        val selectDateTV = view.findViewById<AppCompatTextView>(R.id.date_selected_tv)
        val submitBtn = view.findViewById<AppCompatButton>(R.id.submit_btn)

        if (date != null) {
            val localDate: LocalDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            val year: Int = localDate.year
            val month: Int = localDate.monthValue
            val day: Int = localDate.dayOfMonth
            val tempDate = "$day-$month-$year"
            selectDateTV.text = tempDate
            for (elements in taskList) {
                if (elements.date == tempDate) {
                    taskEt.setText(elements.task)
                }
            }
        }

        submitBtn.setOnClickListener {
            val taskData: String = taskEt.text.toString()
            val dateData = selectDateTV.text.toString()
            if (taskData.isNotEmpty() && dateData.isNotEmpty()) {
                saveDataInDB(taskData, dateData)
            } else {
                Toast.makeText(this, "Please Select Task & Date Both", Toast.LENGTH_SHORT)
                    .show()
            }
            builder.dismiss()
        }

        selectDateBtn.setOnClickListener {
            val c = Calendar.getInstance()

            // on below line we are getting
            // our day, month and year.
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            // on below line we are creating a
            // variable for date picker dialog.
            val datePickerDialog = DatePickerDialog(
                // on below line we are passing context.
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    // on below line we are setting
                    // date to our text view.
                    val dateText = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                    selectDateTV.text = dateText
                    for (elements in taskList) {
                        if (elements.date == dateText) {
                            taskEt.setText(elements.task)
                        }
                    }
                },
                // on below line we are passing year, month
                // and day for the selected date in our date picker.
                year,
                month,
                day
            )
            // at last we are calling show
            // to display our date picker dialog.
            datePickerDialog.show()
        }
        builder.setCanceledOnTouchOutside(true)
        builder.show()
    }

    /**
     * Set up Recycler View for Task List
     * */
    private fun prepareRecyclerView() {
        movieAdapter = TaskAdapter(this)
        binding.rvTasks.apply {
            layoutManager = GridLayoutManager(applicationContext, 1)
            adapter = movieAdapter
        }
    }

    /**
     * Observer Changes in DB for Task List
     * */
    private fun observeChangesInDB() {
        DBHelper.getTaskLiveList(TaskDatabase.getDatabase(this@HomeActivity).taskDao())
            .observe(this) { movieList ->
                taskList.clear()
                taskList.addAll(movieList)
                movieAdapter.setTaskList(movieList)
                highlightTheCalender()
            }
    }

    private fun saveDataInDB(taskData: String, dateData: String) {
        val split = dateData.split("-")
        val taskModel = TaskModel(
            dateData,
            split[0].toInt(),
            split[1].toInt(),
            split[2].toInt(),
            taskData
        )

        /*if (alreadyDataInDB(dateData)) {
            lifecycleScope.launch(
                Dispatchers.IO
            ) {
                DBHelper.updateTaskInList(
                    TaskDatabase.getDatabase(this@HomeActivity).taskDao(),
                    taskModel
                )
            }
        } else {*/
        lifecycleScope.launch(
            Dispatchers.IO
        ) {
            DBHelper.insertTaskModel(
                TaskDatabase.getDatabase(this@HomeActivity).taskDao(),
                taskModel
            )
        }
        /* }*/
    }

    private fun alreadyDataInDB(dateData: String): Boolean {
        for (element in taskList) {
            if (element.date == dateData) {
                return true
            }
        }
        return false
    }

    private suspend fun getAllDataFromDB(): List<TaskModel> {
        val async = lifecycleScope.async(Dispatchers.IO) {
            DBHelper.getMonthFilerTaskList(
                TaskDatabase.getDatabase(this@HomeActivity).taskDao(),
                selectedMonth
            )
        }
        return async.await()
    }

    override fun onDateClick(selectedDate: Date) {
        val currentDate = Date()
        val dateOnlyInstance = DateTimeComparator.getDateOnlyInstance()
        val compare = dateOnlyInstance.compare(currentDate, selectedDate)
        when {
            compare > 0 -> {
                Toast.makeText(this, "Please select Future Dates", Toast.LENGTH_LONG).show()
            }
            compare < 0 -> {
                showAlertPopup(selectedDate)
            }
            else -> {
                showAlertPopup(selectedDate)
            }
        }
    }

    override fun onMonthChanged(monthDate: Date) {
        val localDate: LocalDate =
            monthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        selectedMonth = localDate.monthValue
        lifecycleScope.launch { updateList() }
    }

    private suspend fun updateList() {
        val async = lifecycleScope.async(Dispatchers.IO) {
            DBHelper.getMonthFilerTaskList(
                TaskDatabase.getDatabase(this@HomeActivity).taskDao(),
                selectedMonth
            )
        }
        val dataList = async.await()
        taskList.clear()
        taskList.addAll(dataList)
        highlightTheCalender()
        movieAdapter.setTaskList(dataList)
    }

    override fun onItemClicked(taskModel: TaskModel) {
        lifecycleScope.launch(Dispatchers.IO) {
            deleteItemFromList(taskModel)
        }
    }

    private fun deleteItemFromList(taskModel: TaskModel) {
        DBHelper.deleteTaskFromList(
            TaskDatabase.getDatabase(this@HomeActivity).taskDao(),
            taskModel = taskModel
        )
    }

    private fun highlightTheCalender() {
        for (element in taskList) {
            val month = if (element.month < 10) {
                "0${element.month}"
            } else {
                element.month
            }
            val localDate = LocalDate.parse("${element.year}-$month-${element.day}")
            val date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
            binding.calenderView.setDateAsSelected(date)
        }
    }
}