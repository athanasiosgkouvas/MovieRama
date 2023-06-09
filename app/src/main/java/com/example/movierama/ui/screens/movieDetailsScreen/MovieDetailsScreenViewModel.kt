package com.example.movierama.ui.screens.movieDetailsScreen

import androidx.lifecycle.viewModelScope
import com.example.movierama.ui.Utils
import com.example.movierama.ui.base.BaseViewModel
import com.example.movierama.ui.base.UiEffect
import com.example.movierama.ui.base.UiEvent
import com.example.movierama.ui.base.UiState
import com.example.movierama.ui.data.models.Review
import com.example.movierama.ui.data.repository.Repository
import kotlinx.coroutines.launch

sealed interface MovieDetailsContract {
    sealed interface Event : UiEvent {
        data class GetMovieDetails(val id: Int) : Event
        object GetFavorites: Event
        object OnAddToFavoritesClicked: Event
        object OnRemoveFromFavoritesClicked: Event
    }

    data class State(
        val isLoading: Boolean = false,
        val isFavorite: Boolean = false,
        val showError: Boolean = false,
        val movieId: Int = 0,
        val movieTitle: String = "",
        val movieGenres: List<String> = emptyList(),
        val movieDate: String = "",
        val moviePoster: String = "",
        val movieRating: Float = 0f,
        val movieOverview: String = "",
        val movieDirector: String = "",
        val movieCast: List<String> = emptyList(),
        val similarMoviesPosters: List<String> = emptyList(),
        val reviews: List<Review> = emptyList(),
    ) : UiState

    sealed interface Effect : UiEffect {
        object Init : Effect
    }
}

class MovieDetailsScreenViewModel(private val repository: Repository) :
    BaseViewModel<MovieDetailsContract.Event, MovieDetailsContract.State, MovieDetailsContract.Effect>() {
    init {
        setEffect { MovieDetailsContract.Effect.Init }
    }

    override fun createInitialState(): MovieDetailsContract.State = MovieDetailsContract.State()

    override fun handleEvent(event: MovieDetailsContract.Event) {
        when (event) {
            is MovieDetailsContract.Event.GetMovieDetails -> {
                setState { copy(isLoading = true, movieId = event.id) }
                getMovieDetails()
            }

            is MovieDetailsContract.Event.OnAddToFavoritesClicked -> addToFavorites()

            MovieDetailsContract.Event.GetFavorites -> getFavorites()
            is MovieDetailsContract.Event.OnRemoveFromFavoritesClicked -> removeFromFavorites()
        }
    }

    private fun getMovieDetails() {
        viewModelScope.launch {
            repository.getMovieDetails(currentState.movieId)
                .collect {
                    if (it == null) {
                        setState {
                            copy(
                                isLoading = false,
                                showError =true
                            )
                        }
                    } else {
                        setState {
                            copy(
                                isLoading = false,
                                showError = false,
                                movieTitle = it.movieDetails.title,
                                moviePoster = it.movieDetails.poster,
                                movieGenres = it.movieDetails.genres.map { genre -> genre.name },
                                movieDate = Utils.dateParse(it.movieDetails.date),
                                movieRating = it.movieDetails.rating.toFloat(),
                                movieOverview = it.movieDetails.overview,
                                movieDirector = it.movieDetails.credits.crew.firstOrNull { crew -> crew.job == "Director" }?.name.orEmpty(),
                                movieCast = it.movieDetails.credits.cast.filter { cast -> cast.order < 4 }.map { cast -> cast.name },
                                similarMoviesPosters = it.similarMovies.results.map { movie -> movie.poster },
                                reviews = it.movieReviews.results.take(2)
                            )
                        }
                    }
                }
        }.invokeOnCompletion { getFavorites() }
    }

    private fun addToFavorites() {
        viewModelScope.launch {
            repository.addToFavorites(currentState.movieId)
        }.invokeOnCompletion { getFavorites() }
    }

    private fun removeFromFavorites(){
        viewModelScope.launch {
            repository.removeFromFavorites(currentState.movieId)
        }.invokeOnCompletion { getFavorites() }
    }

    private fun getFavorites(){
        viewModelScope.launch {
            repository.getFavorites().collect{
                setState {
                    copy(
                        isFavorite = it.contains(currentState.movieId)
                    )
                }
            }
        }
    }
}