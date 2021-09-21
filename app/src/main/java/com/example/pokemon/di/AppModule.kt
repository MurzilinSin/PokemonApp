package com.example.pokemon.di

import com.example.pokemon.model.api.PokeApi
import com.example.pokemon.ui.favourite.FavouriteViewModel
import com.example.pokemon.ui.pokemon.PokemonViewModel
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val viewModelModule : Module = module {
    viewModel { PokemonViewModel(provideRetrofit()) }
    viewModel { FavouriteViewModel() }
}

val retrofitModule : Module = module {
    single { provideHttpClient() }
    single { provideGson() }
    single { provideRetrofit() }
}

fun provideHttpClient(): OkHttpClient {
    val httpClient = OkHttpClient.Builder()
    return httpClient.build()
}

fun provideGson(): GsonConverterFactory {
    return GsonConverterFactory.create(GsonBuilder().setLenient().create())
}

fun provideRetrofit(): PokeApi {
    val podRetrofit = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(provideGson())
        .client(provideHttpClient())
        .build()
    return podRetrofit.create(PokeApi::class.java)
}