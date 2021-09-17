package com.example.pokemon.ui.pokemon

import android.animation.ObjectAnimator
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat.jumpDrawablesToCurrentState
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import com.example.pokemon.R
import com.example.pokemon.databinding.FragmentSearchBinding
import com.example.pokemon.model.ResponseData
import com.example.pokemon.model.Type
import com.example.pokemon.model.responce.Pokemon
import com.example.pokemon.ui.parseTypeToColor
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import kotlin.random.Random

const val TAG = "Fragment"
private const val ARG_POKEMON = "pokemon"
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel by inject<PokemonViewModel>()
    private lateinit var pokemon: Pokemon

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
        when(arguments?.getSerializable("type")) {
            Type.Random -> {
                binding.pokemonSearch.visibility = View.GONE
                random()
            }
            Type.Search -> {
                binding.pokemonSearch.visibility = View.VISIBLE
                search()
            }
        }
    }

    private fun search(){
        viewModel.ldPokemon.observe(viewLifecycleOwner, {
            renderData(it)
        })
        binding.pokemonSearch.setEndIconOnClickListener {
            val pokemonName = binding.inputEditText.text.toString().trim()
            if(pokemonName.isEmpty()){
                binding.inputEditText.text?.clear()
            } else {
                viewModel.getPokemon(pokemonName)
            }
        }
    }

    private fun random() {
        val random: Int = Random.nextInt(1,1000)
        viewModel.ldPokemon.observe(viewLifecycleOwner, {
            renderData(it)
        })
        viewModel.getPokemon(random.toString())
    }

    private fun renderData(responseData: ResponseData?) {
        when(responseData) {
            is ResponseData.Success -> {
                pokemon = responseData.pokemonResponse
                viewModel.pokeLiveData.observe(viewLifecycleOwner,{ pokemons ->
                    for (pokemonFromDB in pokemons) {
                        if(pokemon.id == pokemonFromDB.id) {
                            binding.includedPokemon.isFavourite.isChecked = pokemonFromDB.isFavourite
                            binding.includedPokemon.isFavourite.jumpDrawablesToCurrentState()
                        }
                    }
                })


                binding.includedPokemon.pokeImg.visibility = View.VISIBLE
                binding.includedPokemon.pokeCard.visibility = View.VISIBLE
                binding.includedPokemon.pokeType2.visibility = View.GONE
                binding.loading.visibility = View.GONE
                val pokeData = responseData.pokemonResponse
                if(pokeData.isFavourite) {
                    binding.includedPokemon.isFavourite.isChecked = true
                }
                Picasso
                    .get()
                    .load(pokeData.sprites.front_default)
                    .into(binding.includedPokemon.pokeImg)
                val gd = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(ContextCompat.getColor(requireContext(),R.color.black),
                    ContextCompat.getColor(requireContext(),parseTypeToColor(pokeData.types[0].type))))
                gd.cornerRadius = 0f
                binding.includedPokemon.mainContainer.background = gd
                val text2 = "#${pokeData.id} ${pokeData.name}"
                binding.includedPokemon.pokeName.text = text2
                binding.includedPokemon.pokeType1.apply {
                    text = pokeData.types[0].type.name
                    backgroundTintList = ContextCompat.getColorStateList(this@SearchFragment.requireContext(),parseTypeToColor(pokeData.types[0].type))
                }
                if(pokeData.types.size > 1){
                    binding.includedPokemon.pokeType2.apply {
                        visibility = View.VISIBLE
                        text = pokeData.types[1].type.name
                        backgroundTintList = ContextCompat.getColorStateList(this@SearchFragment.requireContext(),parseTypeToColor(pokeData.types[1].type))
                    }
                    binding.includedPokemon.pokeType2.visibility = View.VISIBLE
                }
                binding.includedPokemon.heightText.text = "${pokeData.height} m"
                binding.includedPokemon.weightText.text = "${pokeData.weight} kg"
                binding.includedPokemon.isFavourite.setOnCheckedChangeListener { _, isChecked ->
                    viewModel.viewModelScope.launch {
                        if(isChecked){
                            viewModel.addPokemon(pokemon)
                        } else viewModel.deletePokemon(pokemon)
                    }
                }
                binding.includedPokemon.statHp.progress = pokeData.stats[0].base_stat
                progressBarAnimation(binding.includedPokemon.statHp,pokeData.stats[0].base_stat,700)
                progressBarAnimation(binding.includedPokemon.statAttack,pokeData.stats[1].base_stat,700)
                progressBarAnimation(binding.includedPokemon.statDefence,pokeData.stats[2].base_stat,700)
                progressBarAnimation(binding.includedPokemon.statSpeed,pokeData.stats[3].base_stat,700)
                binding.includedPokemon.statAttack.progress = pokeData.stats[1].base_stat
                binding.includedPokemon.statDefence.progress = pokeData.stats[2].base_stat
                binding.includedPokemon.statSpeed.progress = pokeData.stats[3].base_stat
                val drawableAttack = ResourcesCompat.getDrawable(resources,R.drawable.attack_progress_bg,context?.theme)
                binding.includedPokemon.statAttack.progressDrawable = drawableAttack
                val drawableDefence = ResourcesCompat.getDrawable(resources,R.drawable.defence_progress_bg,context?.theme)
                binding.includedPokemon.statDefence.progressDrawable = drawableDefence
                val drawableSpeed = ResourcesCompat.getDrawable(resources,R.drawable.speed_progress_bg,context?.theme)
                binding.includedPokemon.statSpeed.progressDrawable = drawableSpeed
            }
            is ResponseData.Loading -> {
                binding.loading.visibility = View.VISIBLE
                println("WE'VE JUST LOADING AND LOADING")
            }
            is ResponseData.Error -> {
               /* dialogWithOneButtonShow("Error",
                    "Incorrect name of city",
                    "Try another One")*/
                Throwable(IllegalAccessError())
            }
        }
    }

    private fun progressBarAnimation(progressBar: ProgressBar, end: Int,duration: Long){
        val animator = ObjectAnimator.ofInt(progressBar,"progress",0,end)
        animator.duration = duration
        animator.start()
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