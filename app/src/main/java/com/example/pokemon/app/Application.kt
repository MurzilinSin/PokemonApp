package com.example.pokemon.app

import android.app.Application
import com.example.pokemon.di.retrofitModule
import com.example.pokemon.di.viewModelModule
import com.example.pokemon.repo.Repo
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Application: Application() {
    override fun onCreate() {
        super.onCreate()
        Repo.initialize(this)
        startKoin {
            androidContext(this@Application)
            modules(listOf(
                viewModelModule, retrofitModule
            ))
        }
    }
}