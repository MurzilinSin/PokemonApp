package com.example.pokemon.ui.favourite

import androidx.lifecycle.ViewModel
import com.example.pokemon.repo.Repo

class FavouriteViewModel: ViewModel() {
    private val pokeRepo = Repo.get()
    val pokeLiveData = pokeRepo.getPokemons()
}