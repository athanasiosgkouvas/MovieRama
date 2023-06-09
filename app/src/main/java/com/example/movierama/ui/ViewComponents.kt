package com.example.movierama.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentScreen(
    isLoading: Boolean = false,
    shouldTopBarVisible: Boolean = false,
    onBack: (() -> Unit)? = null,
    content: @Composable() BoxScope.() -> Unit
) {
    if (isLoading) {
        Loading()
    } else {
        Scaffold(
            topBar = {
                if (shouldTopBarVisible) {
                    TopAppBar(
                        title = {},
                        navigationIcon = {
                            IconButton(onClick = { onBack?.invoke() }) {
                                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                            }
                        }
                    )
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(15.dp, 15.dp, 15.dp)
            ) {
                content()
            }
        }
    }

}

@Composable
fun Loading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(64.dp),
            color = Color.Black
        )
    }
}

@Composable
fun StarRating(rating: Float, maxRating: Int = 5, size: Dp = 18.dp) {
    Row {
        for (i in 0 until maxRating) {
            val icon = if (i < rating) {
                Icons.Filled.Star
            } else {
                Icons.Outlined.Star
            }

            Icon(
                imageVector = icon,
                contentDescription = null, // Provide a meaningful description if needed
                tint = if (i < (rating / 2).roundToInt()) Color.Blue else Color.Gray,
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(size)
            )
        }
    }
}
