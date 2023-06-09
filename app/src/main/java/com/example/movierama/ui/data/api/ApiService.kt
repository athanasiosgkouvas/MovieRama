package com.example.movierama.ui.data.api

import com.example.movierama.ui.data.models.GetMoviesResponse
import com.example.movierama.ui.data.models.MovieDetailsResponse
import com.example.movierama.ui.data.models.MovieReviewsResponse
import com.example.movierama.ui.data.models.SimilarMoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("movie/popular")
    suspend fun getMovies(
        @Query("page") page: Int
    ): Response<GetMoviesResponse>

    @GET("search/movie")
    suspend fun search(
        @Query("query") query: String
    ): Response<GetMoviesResponse>

    @GET("movie/{id}")
    suspend fun getMovieDetails(
        @Path("id") id: Int,
        @Query("append_to_response") appendToResponse: String = "credits"
    ): Response<MovieDetailsResponse>

    @GET("movie/{id}/reviews")
    suspend fun getMovieReviews(@Path("id") id: Int): Response<MovieReviewsResponse>

    @GET("movie/{id}/similar")
    suspend fun getSimilarMovies(@Path("id") id: Int): Response<SimilarMoviesResponse>
}