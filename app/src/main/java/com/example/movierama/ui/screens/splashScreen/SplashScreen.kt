package com.example.movierama.ui.screens.splashScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.movierama.R
import com.example.movierama.ui.ContentScreen

@Composable
fun SplashScreen(
    navigateToHome: () -> Unit
) {
    ContentScreen {

        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splash_movie))
        val logoAnimationState =
            animateLottieCompositionAsState(composition = composition)
        LottieAnimation(
            composition = composition,
            progress = { logoAnimationState.progress },
            modifier = Modifier
                .background(Color.Transparent)
                .fillMaxSize()
                .fillMaxWidth(),
            alignment = Alignment.Center
        )
        if (logoAnimationState.isAtEnd && logoAnimationState.isPlaying) {
            navigateToHome()
        }
    }
}