package com.example.movierama.ui.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Review(
    val author: String,
    val content: String,
)
