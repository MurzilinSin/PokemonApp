package com.example.pokemon.ui.pokemon

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokemon.model.ResponseData
import com.example.pokemon.model.api.PokeApi
import com.example.pokemon.model.responce.Pokemon
import com.example.pokemon.repo.Repo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PokemonViewModel(private val pokeApi: PokeApi) : ViewModel() {
    val ldPokemon: MutableLiveData<ResponseData> = MutableLiveData()
    private val pokeRepo = Repo.get()
    val pokeLiveData = pokeRepo.getPokemons()

    fun getPokemon(pokemonName : String): LiveData<ResponseData> {
        sendServerRequest(pokemonName)
        return ldPokemon
    }
    private var job: Job? = null

    private fun sendServerRequest(pokemonName: String) {
        ldPokemon.value = ResponseData.Loading(null)
        job = CoroutineScope(Dispatchers.Main).launch {
            val response = pokeApi.getPokemon(pokemonName)
            if(response.isSuccessful) {
                ldPokemon.value = ResponseData.Success(response.body()!!)
            } else {
                val message = response.message()
                if (message.isNullOrEmpty()) {
                    ldPokemon.value =
                        ResponseData.Error(Throwable("Unidentified error"))
                } else {
                    ldPokemon.value =
                        ResponseData.Error(Throwable(message))
                }
            }
        }
    }

    suspend fun addPokemon(pokemon: Pokemon) = pokeRepo.addPokemon(pokemon)

    suspend fun deletePokemon(pokemon: Pokemon) = pokeRepo.deletePokemon(pokemon)

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}