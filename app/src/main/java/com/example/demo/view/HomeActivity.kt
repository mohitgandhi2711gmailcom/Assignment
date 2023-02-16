package com.example.demo.view

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.demo.database.TaskDatabase
import com.example.demo.databinding.ActivityHomeBinding
import com.example.demo.helper.DBHelper
import com.example.demo.model.TaskModel
import com.example.demo.view.adapters.TaskAdapter
import com.example.demo.viewmodel.MovieViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val viewModel: MovieViewModel by viewModels()
    private lateinit var movieAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareRecyclerView()
        observeChangesInDB()
    }

    /**
     * Set up Recycler View for Task List
     * */
    private fun prepareRecyclerView() {
        movieAdapter = TaskAdapter()
        binding.rvMovies.apply {
            layoutManager = GridLayoutManager(applicationContext, 2)
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
}