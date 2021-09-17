package com.example.pokemon.model.api

import com.example.pokemon.model.responce.Pokemon
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApi {
    @GET("pokemon/{name}")
    suspend fun getPokemon(@Path("name") pokemonName: String) : Response<Pokemon>
}