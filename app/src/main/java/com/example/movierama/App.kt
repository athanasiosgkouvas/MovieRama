package com.example.movierama

import android.app.Application
import com.example.movierama.ui.di.cacheModule
import com.example.movierama.ui.di.networkModule
import com.example.movierama.ui.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                viewModelModule,
                networkModule,
                cacheModule
            )
        }
    }
}