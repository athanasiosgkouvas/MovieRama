package com.example.movierama.ui.screens.movieDetailsScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.movierama.R
import com.example.movierama.ui.Loading
import com.example.movierama.ui.StarRating
import com.example.movierama.ui.Utils
import com.example.movierama.ui.data.models.Review
import com.example.movierama.ui.theme.Typography
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun MovieDetailsScreen(viewModel: MovieDetailsScreenViewModel = koinViewModel(), movieId: Int, navHostController: NavHostController) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest {
            when (it) {
                MovieDetailsContract.Effect.Init -> viewModel.setEvent(
                    MovieDetailsContract.Event.GetMovieDetails(
                        movieId
                    )
                )
            }
        }
    }

    if(state.isLoading){
        Loading()
    }else{
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                ) {
                    AsyncImage(
                        model = Utils.createPosterUrl(state.moviePoster),
                        contentDescription = state.movieTitle,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxSize(),
                        alignment = Alignment.TopCenter
                    )
                    IconButton(onClick = { navHostController.popBackStack() }, modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(15.dp)) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = stringResource(
                            id = R.string.details_screen_go_back_content_description
                        ), tint = Color.White)
                    }
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(15.dp)
                    ) {
                        Text(
                            text = state.movieTitle,
                            style = Typography.titleLarge.copy(color = Color.White),
                        )
                        Text(
                            text = state.movieGenres.toString(),
                            style = Typography.titleSmall.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Light
                            )
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = state.movieDate,
                                style = Typography.titleMedium.copy(color = Color.Gray)
                            )
                            StarRating(rating = state.movieRating)
                        }
                        IconButton(
                            onClick = {
                                if (state.isFavorite)
                                    viewModel.setEvent(MovieDetailsContract.Event.OnRemoveFromFavoritesClicked)
                                else
                                    viewModel.setEvent(MovieDetailsContract.Event.OnAddToFavoritesClicked)
                            },
                            modifier = Modifier.align(Alignment.CenterVertically)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = stringResource(id = R.string.favorite_icon_content_description),
                                tint = if (state.isFavorite) Color.Red else Color.LightGray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.padding(10.dp))

                    Text(
                        text = stringResource(id = R.string.details_screen_title_description), style = Typography.titleMedium.copy(
                            color = Color.Blue,
                        )
                    )
                    Text(text = state.movieOverview)

                    Spacer(modifier = Modifier.padding(10.dp))

                    Text(
                        text = stringResource(id = R.string.details_screen_title_director), style = Typography.titleMedium.copy(
                            color = Color.Blue,
                        )
                    )
                    Text(text = state.movieDirector)

                    Spacer(modifier = Modifier.padding(10.dp))

                    Text(
                        text = stringResource(id = R.string.details_screen_title_cast), style = Typography.titleMedium.copy(
                            color = Color.Blue,
                        )
                    )

                    Text(text = state.movieCast.toString())

                    Spacer(modifier = Modifier.padding(10.dp))

                    Text(
                        text = stringResource(id = R.string.details_screen_title_similar_movies), style = Typography.titleMedium.copy(
                            color = Color.Blue,
                        )
                    )

                    LazyRow(modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        items(state.similarMoviesPosters) {
                            ElevatedCard(modifier = Modifier.fillMaxHeight(), shape = RoundedCornerShape(10.dp)) {
                                AsyncImage(
                                    model = Utils.createPosterUrl(it),
                                    contentDescription = stringResource(
                                        id = R.string.details_screen_similar_movies_content_description
                                    ),
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    alignment = Alignment.Center
                                )
                            }

                        }
                    }

                    Spacer(modifier = Modifier.padding(10.dp))

                    Text(
                        text = stringResource(id = R.string.details_screen_title_reviews), style = Typography.titleMedium.copy(
                            color = Color.Blue,
                        )
                    )

                    state.reviews.forEach {
                        Spacer(modifier = Modifier.padding(5.dp))
                        Review(it)
                    }
                }
            }
        }
    }
}

@Composable
fun Review(review: Review){
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = review.author, style = Typography.titleMedium.copy(color = Color.Gray))
        Text(text = review.content)
    }
}