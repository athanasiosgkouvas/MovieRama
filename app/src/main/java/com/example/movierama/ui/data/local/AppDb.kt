package com.example.movierama.ui.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Movie::class], version = 1)
abstract class AppDb : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}