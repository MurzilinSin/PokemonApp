package com.example.pokemon.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pokemon.databinding.FragmentPokemonBinding
import com.example.pokemon.databinding.FragmentSearchBinding
import com.example.pokemon.model.ResponseData
import com.squareup.picasso.Picasso
import org.koin.android.ext.android.bind
import org.koin.android.ext.android.inject

const val TAG = "Fragment"
private const val ARG_POKEMON = "pokemon"
class SearchFragment : Fragment() {
    private var _bindingSearch: FragmentSearchBinding? = null
    private val bindingSearch get() = _bindingSearch!!
    private var _bindingRandom: FragmentPokemonBinding? = null
    private val bindingRandom get() = _bindingRandom!!
    private val viewModel by inject<SearchViewModel>()
    private var callback: Callback? = null
    private var state: String = "search"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if(state == "search"){
            _bindingSearch = FragmentSearchBinding.inflate(inflater,container,false)
            return bindingSearch.root
        } else
            _bindingRandom = FragmentPokemonBinding.inflate(inflater,container,false)
            return bindingRandom.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.ldPokemon.observe(viewLifecycleOwner, {
            renderData(it)
        })
        bindingSearch.pokemonSearch.setEndIconOnClickListener {
            val pokemonName = bindingSearch.inputEditText.text.toString().trim()
            if(pokemonName.isEmpty()){
                println()
            } else {
                viewModel.getPokemon(pokemonName )
            }
        }
    }

    private fun renderData(responseData: ResponseData?) {
        when(responseData) {
            is ResponseData.Success -> {
                bindingSearch.includedPokemon.pokeImg.visibility = View.VISIBLE
                bindingSearch.includedPokemon.pokeCard.visibility = View.VISIBLE
                bindingSearch.loading.visibility = View.GONE
                val pokeData = responseData.pokemonResponse
                Picasso
                    .get()
                    .load(pokeData.sprites.front_default)
                    .into(bindingSearch.includedPokemon.pokeImg)
                bindingSearch.includedPokemon.pokeName.text = "#${pokeData.id} ${pokeData.name}"
                bindingSearch.includedPokemon.pokeType1.text = pokeData.types[0].type.name
                bindingSearch.includedPokemon.pokeType2.text = pokeData.types[1].type.name
                bindingSearch.includedPokemon.heightText.text = pokeData.height.toString()
                bindingSearch.includedPokemon.weightText.text = pokeData.weight.toString()
            }
            is ResponseData.Loading -> {
                bindingSearch.loading.visibility = View.VISIBLE
                println("WEVE JUST LOADING AND LOADING")
            }
            is ResponseData.Error -> {
               /* dialogWithOneButtonShow("Error",
                    "Incorrect name of city",
                    "Try another One")*/
                Throwable(IllegalAccessError())
            }
        }
    }

    companion object {
        fun newInstance(state: String): SearchFragment{
            val args = Bundle().apply {
                putSerializable(ARG_POKEMON, state)
            }
            return  SearchFragment().apply {
                arguments = args
            }
        }
    }

    interface Callback{
        fun onScreenSelect(state: String)
    }
}