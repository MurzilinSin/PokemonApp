package com.example.pokemon.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.pokemon.model.responce.Pokemon

@Dao
interface PokemonDAO {
    @Query("SELECT * FROM pokemon")
    fun getPokemons(): LiveData<List<Pokemon>>
    @Insert
    fun addPokemon(pokemon: Pokemon)
}