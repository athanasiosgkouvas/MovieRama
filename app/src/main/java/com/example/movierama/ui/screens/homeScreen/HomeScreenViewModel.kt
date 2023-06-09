package com.example.movierama.ui.screens.homeScreen

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.movierama.ui.base.BaseViewModel
import com.example.movierama.ui.base.UiEffect
import com.example.movierama.ui.base.UiEvent
import com.example.movierama.ui.base.UiState
import com.example.movierama.ui.data.models.Movie
import com.example.movierama.ui.data.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

interface HomeScreenContract {
    sealed interface Event : UiEvent {
        object GetMovies : Event
        object GetFavorites : Event
        data class OnMovieClicked(val id: Int) : Event
        data class OnSearchTermChanged(val text: String) : Event
        data class OnAddToFavoritesClicked(val id: Int) : Event
        data class OnRemoveFavoritesClicked(val id: Int) : Event
    }

    data class State(
        val isLoading: Boolean = false,
        val text: String = "",
        val showError: Boolean = false,
        val pagingData: Flow<PagingData<Movie>> = flowOf(PagingData.empty()),
        val favorites: List<Int> = emptyList()
    ) : UiState

    sealed interface Effect : UiEffect {
        data class NavigateToMovieDetails(val id: Int) : Effect
    }
}

class HomeScreenViewModel(private val repository: Repository) :
    BaseViewModel<HomeScreenContract.Event, HomeScreenContract.State, HomeScreenContract.Effect>() {
    init {
        setEvent(HomeScreenContract.Event.GetMovies)
        setEvent(HomeScreenContract.Event.GetFavorites)
    }

    override fun createInitialState(): HomeScreenContract.State = HomeScreenContract.State()

    override fun handleEvent(event: HomeScreenContract.Event) {
        when (event) {
            is HomeScreenContract.Event.GetMovies -> {
                setState {
                    copy(
                        isLoading = true
                    )
                }
                getMovies()
            }

            is HomeScreenContract.Event.OnMovieClicked -> {
                setEffect {
                    HomeScreenContract.Effect.NavigateToMovieDetails(event.id)
                }
            }

            is HomeScreenContract.Event.OnSearchTermChanged -> {
                setState {
                    copy(
                        isLoading = true,
                        text = event.text
                    )
                }
                if(event.text.isEmpty()){
                    getMovies()
                }else{
                    search(event.text)
                }
            }

            is HomeScreenContract.Event.OnAddToFavoritesClicked -> {
                addToFavorites(event.id)
            }

            is HomeScreenContract.Event.OnRemoveFavoritesClicked -> {
                removeFromFavorites(event.id)
            }

            HomeScreenContract.Event.GetFavorites -> {
                getFavorites()
            }
        }
    }

    private fun getMovies() {
        viewModelScope.launch {
            repository.getMovies().let {
                setState { copy(pagingData = it, isLoading = false) }
            }
        }
    }

    private fun search(term: String) {
        viewModelScope.launch {
            repository.search(term).let {
                setState { copy(pagingData = it, isLoading = false) }
            }
        }
    }

    private fun addToFavorites(id: Int) {
        viewModelScope.launch {
            repository.addToFavorites(id)
        }.invokeOnCompletion { getFavorites() }
    }

    private fun removeFromFavorites(id: Int){
        viewModelScope.launch {
            repository.removeFromFavorites(id)
        }.invokeOnCompletion { getFavorites() }
    }

    private fun getFavorites(){
        viewModelScope.launch {
            repository.getFavorites().collect{
                setState {
                    copy(
                        favorites = it
                    )
                }
            }
        }
    }
}