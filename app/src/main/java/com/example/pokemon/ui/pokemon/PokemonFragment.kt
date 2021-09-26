package com.example.pokemon.ui.pokemon

import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import com.example.pokemon.R
import com.example.pokemon.databinding.FragmentPokemonBinding
import com.example.pokemon.model.ResponseData
import com.example.pokemon.model.Type
import com.example.pokemon.model.responce.Pokemon
import com.example.pokemon.ui.parseTypeToColor
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import kotlin.random.Random

private const val FRAGMENT_TYPE = "type"
private const val SAVED_POKEMON = "pokeSaved"
class SearchFragment : Fragment() {
    private var _binding: FragmentPokemonBinding? = null
    private val binding get() = _binding!!
    private val viewModel by inject<PokemonViewModel>()
    private var pokemonsDB: MutableList<Pokemon> = mutableListOf()
    private var pokeSaved: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pokeSaved = savedInstanceState?.getString(SAVED_POKEMON)
        viewModel.pokeLiveData.observe(viewLifecycleOwner, { pokemons ->
            for (pokemon in pokemons) {
                pokemonsDB.add(pokemon)
            }
        })
        when(arguments?.getSerializable(FRAGMENT_TYPE)) {
            Type.Random -> {
                binding.pokemonSearch.visibility = View.GONE
                random(pokeSaved)
            }
            Type.Search -> {
                binding.pokemonSearch.visibility = View.VISIBLE
                search(pokeSaved)
            }
        }
    }

    private fun search(savedPoke: String?) {
        viewModel.ldPokemon.observe(viewLifecycleOwner, {
            renderData(it)
        })
        if(savedPoke != null) {
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.getPokemon(savedPoke)
            }
        }
        binding.pokemonSearch.setEndIconOnClickListener {
            val pokemonName = binding.inputEditText.text.toString().trim()
            if(pokemonName.isNotEmpty()){
                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.getPokemon(pokemonName)
                }
            pokeSaved = pokemonName
            } else {
                dialogWithOneButtonShow("Error",
                    "U need to enter some name of Pokemon or his ID to work",
                    "Try again")
            }
            binding.inputEditText.text = null
        }
    }

    private fun random(savedPoke: String?) {
        viewModel.ldPokemon.observe(viewLifecycleOwner, {
            renderData(it)
        })
        pokeSaved = if(savedPoke != null) {
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.getPokemon(savedPoke)
            }
            savedPoke
        } else {
            val random: Int = Random.nextInt(1,1000)
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.getPokemon(random.toString())
            }
            random.toString()
        }
    }

    private fun renderData(responseData: ResponseData?) {
        when(responseData) {
            is ResponseData.Success -> {
                val pokeData = responseData.pokemonResponse
                initViews(pokeData)
            }
            is ResponseData.Loading -> {
                binding.loading.visibility = View.VISIBLE
            }
            is ResponseData.Error -> {
                when(arguments?.getSerializable(FRAGMENT_TYPE)) {
                    Type.Random -> {
                        dialogWithOneButtonShow("Error",
                            "Something happens with server",
                            "Try again")
                        Throwable(IllegalAccessError())
                    }
                    Type.Search -> {
                        dialogWithOneButtonShow("Error",
                            "Incorrect name of pokemon",
                            "Try another One")
                        Throwable(IllegalAccessError())
                    }
                }
            }
        }
    }

    private fun initViews(pokeData: Pokemon) {
        setButtonDBVisibility(pokeData)
        binding.pokeImg.visibility = View.VISIBLE
        binding.pokeCard.visibility = View.VISIBLE
        binding.pokeType2.visibility = View.GONE
        binding.loading.visibility = View.GONE
        Picasso
            .get()
            .load(pokeData.sprites.front_default)
            .into(binding.pokeImg)
        val gd = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(ContextCompat.getColor(requireContext(),parseTypeToColor(pokeData.types[0].type)),
                ContextCompat.getColor(requireContext(),R.color.black)))
        gd.cornerRadius = 0f
        binding.mainContainer.background = gd
        val pokeName = "#${pokeData.id} ${pokeData.name}"
        binding.pokeName.text = pokeName
        binding.pokeType1.apply {
            text = pokeData.types[0].type.name
            backgroundTintList = ContextCompat.getColorStateList(this@SearchFragment.requireContext(),parseTypeToColor(pokeData.types[0].type))
        }
        binding.pokeAdd.backgroundTintList = ContextCompat.getColorStateList(this@SearchFragment.requireContext(), parseTypeToColor(pokeData.types[0].type))
        binding.pokeDelete.backgroundTintList = ContextCompat.getColorStateList(this@SearchFragment.requireContext(), parseTypeToColor(pokeData.types[0].type))
        if(pokeData.types.size > 1){
            binding.pokeType2.apply {
                visibility = View.VISIBLE
                text = pokeData.types[1].type.name
                backgroundTintList = ContextCompat.getColorStateList(this@SearchFragment.requireContext(),parseTypeToColor(pokeData.types[1].type))
            }
            binding.pokeType2.visibility = View.VISIBLE
        }
        val height = "${pokeData.height} m"
        val weight = "${pokeData.weight} kg"
        binding.heightText.text = height
        binding.weightText.text = weight
        binding.pokeAdd.setOnClickListener {
            viewModel.viewModelScope.launch {
                viewModel.addPokemon(pokeData)
            }
            binding.pokeAdd.visibility = View.GONE
            binding.pokeDelete.visibility = View.VISIBLE
        }
        binding.pokeDelete.setOnClickListener {
            viewModel.viewModelScope.launch {
                viewModel.deletePokemon(pokeData)
            }
            binding.pokeAdd.visibility = View.VISIBLE
            binding.pokeDelete.visibility = View.GONE
        }
        setProgressBarUI(pokeData)
    }

    private fun setProgressBarUI(pokeData: Pokemon) {
        progressBarAnimation(binding.statHp,pokeData.stats[0].base_stat,698)
        progressBarAnimation(binding.statAttack,pokeData.stats[1].base_stat,702)
        progressBarAnimation(binding.statDefence,pokeData.stats[2].base_stat,699)
        progressBarAnimation(binding.statSpeed,pokeData.stats[3].base_stat,701)
        binding.statHp.progress = pokeData.stats[0].base_stat
        binding.statAttack.progress = pokeData.stats[1].base_stat
        binding.statDefence.progress = pokeData.stats[2].base_stat
        binding.statSpeed.progress = pokeData.stats[3].base_stat
        val drawableAttack = ResourcesCompat.getDrawable(resources,R.drawable.attack_progress_bg,context?.theme)
        binding.statAttack.progressDrawable = drawableAttack
        val drawableDefence = ResourcesCompat.getDrawable(resources,R.drawable.defence_progress_bg,context?.theme)
        binding.statDefence.progressDrawable = drawableDefence
        val drawableSpeed = ResourcesCompat.getDrawable(resources,R.drawable.speed_progress_bg,context?.theme)
        binding.statSpeed.progressDrawable = drawableSpeed
    }

    private fun progressBarAnimation(progressBar: ProgressBar, end: Int,duration: Long){
        val animator = ObjectAnimator.ofInt(progressBar,"progress",0,end)
        animator.duration = duration
        animator.start()
    }

    private fun dialogWithOneButtonShow(title: String, description: String, textButton: String) {
        val dialog = AlertDialog.Builder(context,R.style.MyDialogTheme)
        dialog.apply {
            setTitle(title)
            setMessage(description)
            setPositiveButton(textButton)
            { dialog, _ ->
                dialog.cancel()
            }
        }.create().show()
    }

    private fun setButtonDBVisibility(pokeData: Pokemon) {
        var pokeDB: Pokemon? = null
        for (pokemonDB in pokemonsDB) {
            if(pokemonDB.id == pokeData.id) {
                pokeDB = pokemonDB
            }
        }
        if(pokeDB != null) {
            binding.pokeAdd.visibility = View.GONE
            binding.pokeDelete.visibility = View.VISIBLE
        } else {
            binding.pokeAdd.visibility = View.VISIBLE
            binding.pokeDelete.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if(pokeSaved != null) {
            outState.putString(SAVED_POKEMON, pokeSaved)
        }
    }
}