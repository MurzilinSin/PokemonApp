package com.example.pokemon.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pokemon.model.responce.Pokemon

@Database(entities = [Pokemon::class],version = 1, exportSchema = false)
@TypeConverters(PokemonTypeConverter::class)
abstract class PokemonDB : RoomDatabase() {
    abstract fun pokemonDAO(): PokemonDAO
}