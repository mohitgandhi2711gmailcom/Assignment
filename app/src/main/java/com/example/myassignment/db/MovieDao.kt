package com.example.myassignment.db

import androidx.room.Dao

@Dao
interface MovieDao {

    /*@Insert(onConflict =  OnConflictStrategy.REPLACE)
    fun insertMovieResult(movieResult: MovieResult)

    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    fun insertMovieModel(movieModel: MovieModel)

    @Update
    fun update(movieResult: MovieResult)

    @Delete
    fun delete(movieResult: MovieResult)

    @Query("SELECT * FROM movie_results")
    fun getAllMoviesList(): LiveData<List<MovieResult>>

    @Query("SELECT * FROM main_movie")
    fun getMovieModel(): LiveData<MovieModel>*/

}