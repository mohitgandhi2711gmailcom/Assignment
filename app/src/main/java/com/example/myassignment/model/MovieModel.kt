package com.example.demo.model

import androidx.room.*
import com.google.gson.annotations.SerializedName

@Entity(tableName = "main_movie")
data class MovieModel(
    @PrimaryKey(autoGenerate = true)
    val movieModelId: Int,

    @SerializedName("page")
    val page: Int = 0,

    @SerializedName("total_pages")
    val totalPages: Int = 0,

    @Ignore
    @SerializedName("results")
    val results: List<MovieResult>?,

    @SerializedName("total_Results")
    val totalResults: Int = 0,
) {
    constructor(movieModelId: Int, page: Int, totalPages: Int, totalResults: Int)
            : this(movieModelId, page, totalPages, null, totalResults)
}