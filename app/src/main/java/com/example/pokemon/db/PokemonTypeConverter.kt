package com.example.pokemon.db

import androidx.room.TypeConverter
import com.example.pokemon.model.responce.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PokemonTypeConverter {
    @TypeConverter
    fun fromAbilitiesList(value: List<Ability>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Ability>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toAbilitiesList(value: String): List<Ability> {
        val gson = Gson()
        val type = object : TypeToken<List<Ability>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromFormList(value: List<Form>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Form>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toFormList(value: String): List<Form> {
        val gson = Gson()
        val type = object : TypeToken<List<Form>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromGameIndiceList(value: List<GameIndice>): String {
        val gson = Gson()
        val type = object : TypeToken<List<GameIndice>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toGameIndiceList(value: String): List<GameIndice> {
        val gson = Gson()
        val type = object : TypeToken<List<GameIndice>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromHeldItemsList(value: List<Int>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Int>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toHeldItemsList(value: String): List<Int> {
        val gson = Gson()
        val type = object : TypeToken<List<Int>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromMoveList(value: List<Move>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Move>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toMoveList(value: String): List<Move> {
        val gson = Gson()
        val type = object : TypeToken<List<Move>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromPastTypesList(value: List<Any>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Any>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toPastTypesList(value: String): List<Any> {
        val gson = Gson()
        val type = object : TypeToken<List<Any>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromSpeciesList(value: Species): String {
        val gson = Gson()
        val type = object : TypeToken<Species>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toSpeciesList(value: String): Species {
        val gson = Gson()
        val type = object : TypeToken<Species>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromSpritesList(value: Sprites): String {
        val gson = Gson()
        val type = object : TypeToken<Sprites>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toSpritesList(value: String): Sprites {
        val gson = Gson()
        val type = object : TypeToken<Sprites>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromStatList(value: List<Stat>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Stat>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toStatList(value: String): List<Stat> {
        val gson = Gson()
        val type = object : TypeToken<List<Stat>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromTypesList(value: List<Type>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Type>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toTypesList(value: String): List<Type> {
        val gson = Gson()
        val type = object : TypeToken<List<Type>>() {}.type
        return gson.fromJson(value, type)
    }
}