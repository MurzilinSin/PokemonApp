package com.example.pokemon.app

import android.app.Application
import com.example.pokemon.di.retrofitModule
import com.example.pokemon.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Application: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@Application)
            modules(listOf(
                viewModelModule, retrofitModule
            ))
        }
    }
}