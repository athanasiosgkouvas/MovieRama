package com.example.movierama.ui.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveMovie(vararg movie: Movie)

    @Query("SELECT * FROM Movie")
    fun getMovies(): Flow<List<Movie>>

    @Delete
    suspend fun removeMovie(vararg movie : Movie)
}