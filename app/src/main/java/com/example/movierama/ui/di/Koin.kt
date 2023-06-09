package com.example.movierama.ui.di

import androidx.room.Room
import com.example.movierama.BuildConfig
import com.example.movierama.ui.data.api.ApiInterceptor
import com.example.movierama.ui.data.api.ApiService
import com.example.movierama.ui.data.local.AppDb
import com.example.movierama.ui.data.repository.Repository
import com.example.movierama.ui.data.repository.RepositoryImpl
import com.example.movierama.ui.screens.homeScreen.HomeScreenViewModel
import com.example.movierama.ui.screens.movieDetailsScreen.MovieDetailsScreenViewModel
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val viewModelModule = module {
    viewModel { HomeScreenViewModel(get()) }
    viewModel { MovieDetailsScreenViewModel(get()) }
}

val networkModule = module {
    single<Repository> { RepositoryImpl(get(), get()) }
    single {
        val apiKey = BuildConfig.API_KEY

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(ApiInterceptor(apiKey))
            .build()

        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }
    single {
        get<Retrofit>().create(ApiService::class.java)
    }
}

val cacheModule = module {
    single { Room.databaseBuilder(androidApplication(), AppDb::class.java, "MoviesDb").build() }
}