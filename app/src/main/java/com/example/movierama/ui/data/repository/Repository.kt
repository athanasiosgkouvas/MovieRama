package com.example.movierama.ui.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movierama.ui.data.api.ApiService
import com.example.movierama.ui.data.local.AppDb
import com.example.movierama.ui.data.models.Movie
import com.example.movierama.ui.data.models.MovieDetails
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface Repository {
    suspend fun getMovies(): Flow<PagingData<Movie>>
    suspend fun getMovieDetails(id: Int): Flow<MovieDetails?>
    suspend fun search(q: String): Flow<PagingData<Movie>>
    suspend fun addToFavorites(id: Int)
    suspend fun removeFromFavorites(id: Int)
    suspend fun getFavorites(): Flow<List<Int>>
}

class RepositoryImpl(private val apiService: ApiService, private val appDb: AppDb) : Repository {
    override suspend fun getMovies(): Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(
            pageSize = 10
        ),
        pagingSourceFactory = {
            MoviesPagingSource(apiService)
        }
    ).flow


    override suspend fun getMovieDetails(id: Int): Flow<MovieDetails?> = flow {
        try {
            val movieDetailsResponse = apiService.getMovieDetails(id)
            val movieReviewsResponse = apiService.getMovieReviews(id)
            val similarMoviesResponse = apiService.getSimilarMovies(id)

            if (movieDetailsResponse.isSuccessful && movieReviewsResponse.isSuccessful && similarMoviesResponse.isSuccessful) {
                movieDetailsResponse.body()?.let { movieDetails ->
                    movieReviewsResponse.body()?.let { movieReviews ->
                        similarMoviesResponse.body()?.let { similarMovies ->
                            emit(MovieDetails(movieDetails, movieReviews, similarMovies))
                        } ?: emit(null)
                    } ?: emit(null)
                } ?: emit(null)
            }
            emit(null)
        } catch (e: Exception) {
            emit(null)
        }
    }

    override suspend fun search(q: String): Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            initialLoadSize = 10,
        ),
        pagingSourceFactory = {
            MoviesSearchPagingSource(apiService, q)
        }
    ).flow

    override suspend fun addToFavorites(id: Int) {
        appDb.movieDao().saveMovie(com.example.movierama.ui.data.local.Movie(id))
    }

    override suspend fun removeFromFavorites(id: Int) {
        appDb.movieDao().removeMovie(com.example.movierama.ui.data.local.Movie(id))
    }

    override suspend fun getFavorites(): Flow<List<Int>> = flow {
        appDb.movieDao().getMovies().collect{
            emit(it.map { movie -> movie.id })
        }
    }
}

class MoviesPagingSource(
    private val apiService: ApiService
) : PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            delay(2000) //Unnecessary - Just to demonstrate the pagination functionality
            val page = params.key ?: 1
            val response = apiService.getMovies(page = page)

            LoadResult.Page(
                data = response.body()?.results ?: emptyList(),
                prevKey = if (page == 1) null else page.minus(1),
                nextKey = if (response.body()?.results?.isEmpty() == true) null else page.plus(1),
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}

class MoviesSearchPagingSource(
    private val apiService: ApiService,
    private val q: String
) : PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val page = params.key ?: 1
            val response = apiService.search(query = q)

            LoadResult.Page(
                data = response.body()?.results ?: emptyList(),
                prevKey = if (page == 1) null else page.minus(1),
                nextKey = if (response.body()?.results?.isEmpty() == true) null else page.plus(1),
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}