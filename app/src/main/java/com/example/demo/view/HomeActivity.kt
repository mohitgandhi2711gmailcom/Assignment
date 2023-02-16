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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demo.R
import com.example.demo.database.TaskDatabase
import com.example.demo.databinding.ActivityHomeBinding
import com.example.demo.helper.DBHelper
import com.example.demo.model.TaskModel
import com.example.demo.view.adapters.TaskAdapter
import com.example.demo.viewmodel.TaskViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val viewModel: TaskViewModel by viewModels()
    private lateinit var movieAdapter: TaskAdapter

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
            val builder = AlertDialog.Builder(this)
                .create()
            val view = layoutInflater.inflate(R.layout.task_popup, null)
            builder.setView(view)

            val taskEt = view.findViewById<AppCompatEditText>(R.id.task_et)
            val selectDateBtn = view.findViewById<AppCompatButton>(R.id.select_date_btn)
            val selectDateTV = view.findViewById<AppCompatTextView>(R.id.date_selected_tv)

            val submitBtn = view.findViewById<AppCompatButton>(R.id.submit_btn)
            val taskData: String = taskEt.text.toString()
            val dateData = selectDateTV.text.toString()
            submitBtn.setOnClickListener {
                if (!taskData.isEmpty() && !dateData.isEmpty()) {
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
                        selectDateTV.text =
                            (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
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
    }

    /**
     * Set up Recycler View for Task List
     * */
    private fun prepareRecyclerView() {
        movieAdapter = TaskAdapter()
        binding.rvTasks.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = movieAdapter
        }
    }

    /**
     * Observer Changes in DB for Task List
     * */
    private fun observeChangesInDB() {
        DBHelper.getTaskList(TaskDatabase.getDatabase(this@HomeActivity).taskDao())
            .observe(this) { movieList ->
                movieAdapter.setTaskList(movieList)
            }
    }

    private fun saveDataInDB(taskData: String, dateData: String) {
        val taskModel=TaskModel(Calendar.getInstance().timeInMillis, taskData, dateData)
        lifecycleScope.launch(
            Dispatchers.IO
        ){
            DBHelper.insertTaskModel(TaskDatabase.getDatabase(this@HomeActivity).taskDao(),taskModel)
        }
    }
}