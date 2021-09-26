package com.example.pokemon.repo

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.pokemon.db.PokemonDB
import com.example.pokemon.model.responce.Pokemon

private const val DATABASE_NAME = "pokemon-database"
class Repo private constructor(context: Context){
    private val db: PokemonDB = Room.databaseBuilder(
        context.applicationContext,
        PokemonDB::class.java,
        DATABASE_NAME
    ).build()
    private val pokemonDAO = db.pokemonDAO()

    fun getListPokemons(): List<Pokemon> = pokemonDAO.getListPokemons()
    fun getPokemons(): LiveData<List<Pokemon>> = pokemonDAO.getPokemons()
    suspend fun addPokemon(pokemon: Pokemon) = pokemonDAO.addPokemon(pokemon)
    suspend fun deletePokemon(pokemon: Pokemon) = pokemonDAO.deletePokemon(pokemon)

    companion object {
        private var INSTANCE: Repo? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = Repo(context)
            }
        }
        fun get() : Repo {
            return INSTANCE ?: throw IllegalStateException("Repository must be initialize")
        }
    }
}