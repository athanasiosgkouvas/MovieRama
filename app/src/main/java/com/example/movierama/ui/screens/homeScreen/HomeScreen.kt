package com.example.movierama.ui.screens.homeScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.example.movierama.R
import com.example.movierama.ui.ContentScreen
import com.example.movierama.ui.StarRating
import com.example.movierama.ui.Utils
import com.example.movierama.ui.data.models.Movie
import com.example.movierama.ui.theme.Typography
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(viewModel: HomeScreenViewModel = koinViewModel(), navigateToMovieDetails: (Int) -> Unit) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val lazyPagingItems = state.pagingData.collectAsLazyPagingItems()

    LaunchedEffect(Unit){
        viewModel.effect.collectLatest {
            when(it){
                is HomeScreenContract.Effect.NavigateToMovieDetails -> {
                    navigateToMovieDetails(it.id)
                }
            }
        }
    }
    Column(modifier = Modifier.fillMaxSize()) {
        TextField(
            value = state.text,
            onValueChange = { viewModel.setEvent(HomeScreenContract.Event.OnSearchTermChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = stringResource(id = R.string.home_screen_search))},
            leadingIcon = {Icon(imageVector = Icons.Default.Search, contentDescription = null)}
        )
        ContentScreen(isLoading = state.isLoading) {
            if (state.showError) {
                Text(text = stringResource(id = R.string.home_screen_error))
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                ) {
                    items(
                        count = lazyPagingItems.itemCount,
                        key = lazyPagingItems.itemKey { it.id }
                    ) { index ->
                        lazyPagingItems[index]?.let {
                            Movie(
                                movie = it,
                                onAddToFavorites = {
                                    viewModel.setEvent(
                                        HomeScreenContract.Event.OnAddToFavoritesClicked(it.id)
                                    )
                                },
                                onRemoveFromFavorites = {viewModel.setEvent(
                                    HomeScreenContract.Event.OnRemoveFavoritesClicked(it.id)
                                )},
                                isFavorite = state.favorites.contains(it.id),
                                onMovieClicked = {
                                    viewModel.setEvent(
                                        HomeScreenContract.Event.OnMovieClicked(
                                            it.id
                                        )
                                    )
                                })
                        }
                    }

                    when (lazyPagingItems.loadState.refresh) { //FIRST LOAD
                        is LoadState.Error -> {
                            //todo handle refresh state error
                        }

                        is LoadState.Loading -> { // Loading UI
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillParentMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                ) {
                                    CircularProgressIndicator(color = Color.Black)
                                }
                            }
                        }

                        else -> {}
                    }

                    when (lazyPagingItems.loadState.append) { // Pagination
                        is LoadState.Error -> {
                            //todo handle pagination state error
                        }

                        is LoadState.Loading -> { // Pagination Loading UI
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                ) {
                                    CircularProgressIndicator(color = Color.Black)
                                }
                            }
                        }

                        else -> {}
                    }
                }
            }
        }
    }


}

@Composable
fun Movie(
    movie: Movie,
    isFavorite: Boolean,
    onAddToFavorites: () -> Unit,
    onRemoveFromFavorites: () -> Unit,
    onMovieClicked: () -> Unit)
{

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = true, onClick = { onMovieClicked() }),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.elevatedCardElevation()
    ) {
        AsyncImage(
            model = Utils.createPosterUrl(movie.poster),
            contentDescription = movie.title,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            alignment = Alignment.TopCenter
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f)
                    .padding(5.dp)
            ) {
                Text(
                    text = movie.title,
                    style = Typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(15.dp)) {
                    StarRating(rating = movie.rating.toFloat())
                    Text(
                        text = Utils.dateParse(movie.date)
                    )
                }
            }
            IconButton(
                onClick = { if(isFavorite) onRemoveFromFavorites() else onAddToFavorites() },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Favorite,
                    contentDescription = stringResource(id = R.string.favorite_icon_content_description),
                    tint = if (isFavorite) Color.Red else Color.White
                )
            }

        }
    }
}