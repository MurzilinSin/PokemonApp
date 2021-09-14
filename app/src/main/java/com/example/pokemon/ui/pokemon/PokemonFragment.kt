package com.example.pokemon.ui.pokemon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pokemon.databinding.FragmentSearchBinding
import com.example.pokemon.model.ResponseData
import com.example.pokemon.model.Type
import com.squareup.picasso.Picasso
import org.koin.android.ext.android.inject
import kotlin.random.Random

const val TAG = "Fragment"
private const val ARG_POKEMON = "pokemon"
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel by inject<PokemonViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val typeFragment = arguments?.getSerializable("type")
        when(typeFragment) {
            Type.Random -> {
                binding.pokemonSearch.visibility = View.GONE
                random()
            }
            Type.Search -> {
                binding.pokemonSearch.visibility = View.VISIBLE
                search()
            }
        }
        println(typeFragment)

    }

    private fun search(){
        viewModel.ldPokemon.observe(viewLifecycleOwner, {
            renderData(it)
        })
        binding.pokemonSearch.setEndIconOnClickListener {
            val pokemonName = binding.inputEditText.text.toString().trim()
            if(pokemonName.isEmpty()){
                println()
            } else {
                viewModel.getPokemon(pokemonName )
            }
        }
    }

    private fun random() {
        val random: Int = Random.nextInt(1,100)
        viewModel.ldPokemon.observe(viewLifecycleOwner, {
            renderData(it)
        })
        println(random)
        viewModel.getPokemon(random.toString())
    }

    private fun renderData(responseData: ResponseData?) {
        when(responseData) {
            is ResponseData.Success -> {
                binding.includedPokemon.pokeImg.visibility = View.VISIBLE
                binding.includedPokemon.pokeCard.visibility = View.VISIBLE
                binding.includedPokemon.pokeType2.visibility = View.GONE
                binding.loading.visibility = View.GONE
                val pokeData = responseData.pokemonResponse
                Picasso
                    .get()
                    .load(pokeData.sprites.front_default)
                    .into(binding.includedPokemon.pokeImg)
                binding.includedPokemon.pokeName.text = "#${pokeData.id} ${pokeData.name}"
                binding.includedPokemon.pokeType1.text = pokeData.types[0].type.name
                if(pokeData.types.size > 1){
                    binding.includedPokemon.pokeType2.text = pokeData.types[1].type.name
                    binding.includedPokemon.pokeType2.visibility = View.VISIBLE
                }
                binding.includedPokemon.heightText.text = "${pokeData.height} m"
                binding.includedPokemon.weightText.text = "${pokeData.weight} kg"
            }
            is ResponseData.Loading -> {
                binding.loading.visibility = View.VISIBLE
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
}