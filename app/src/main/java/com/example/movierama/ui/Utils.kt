package com.example.movierama.ui

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object Utils {
    fun createPosterUrl(path: String?): String = "https://image.tmdb.org/t/p/w342$path"
    fun dateParse(date: String?): String {
        return try{
            LocalDate.parse(date, DateTimeFormatter.ISO_DATE).format(
                DateTimeFormatter.ofPattern("d MMMM yyyy"))
        }catch (e: Exception){
            ""
        }
    }
}