package com.example.movierama

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.movierama.ui.navigation.Navigation
import com.example.movierama.ui.theme.MovieRamaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieRamaTheme {
                // A surface container using the 'background' color from the theme
                Navigation()
            }
        }
    }
}