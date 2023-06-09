package com.example.movierama.ui.data.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetails(
    val movieDetails: MovieDetailsResponse,
    val movieReviews: MovieReviewsResponse,
    val similarMovies: SimilarMoviesResponse
)

@Serializable
data class MovieDetailsResponse(
    val id: Int,
    val overview: String,
    @SerializedName("release_date") val date: String,
    @SerializedName("poster_path") val poster: String,
    val genres: List<Genre>,
    val title: String,
    @SerializedName("vote_average") val rating: Double,
    val credits: Credits,
)

@Serializable
data class Credits(
    val cast: List<Cast>,
    val crew: List<Crew>
)

@Serializable
data class Cast(
    val name: String,
    val order: Int,
)

@Serializable
data class Crew(
    val name: String,
    val job: String,
)

@Serializable
data class MovieReviewsResponse(
    val results: List<Review>
)

@Serializable
data class SimilarMoviesResponse(
    val results: List<Movie>
)