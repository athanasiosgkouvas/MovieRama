package com.example.movierama.ui.data.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    @SerializedName("genre_ids") val genres: List<Int>,
    val id: Int,
    val overview: String,
    @SerializedName("poster_path") val poster: String,
    @SerializedName("release_date") val date: String,
    val title: String,
    @SerializedName("vote_average") val rating: Double,
    @SerializedName("vote_count") val votes: Int,
)

@Serializable
data class GetMoviesResponse(
    val page: Int,
    val results: List<Movie>,
    @SerializedName("total_pages") val pages: Int
)