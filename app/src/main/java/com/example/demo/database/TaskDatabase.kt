package com.example.demo.database

import android.content.Context
import androidx.room.*
import com.example.demo.model.TaskModel

@Database(
    entities = [TaskModel::class, MovieResult::class], version = 1,
    exportSchema = true
)

abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object {

        /**
         * Singleton prevents multiple instances of database opening at the same time.
         * */
        @Volatile
        private var INSTANCE: TaskDatabase? = null

        fun getDatabase(context: Context): TaskDatabase {
            /**
             * If the INSTANCE is not null, Then return it, If it is, Then create the database
             * */
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    "task.db"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}