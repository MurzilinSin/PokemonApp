package com.example.pokemon.model

import com.example.pokemon.model.responce.Pokemon

sealed class ResponseData {
    data class Success(val pokemonResponse: Pokemon) : ResponseData()
    data class Error(val error: Throwable) : ResponseData()
    data class Loading(val progress: Int?) : ResponseData()
}