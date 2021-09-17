package com.example.pokemon.ui.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemon.databinding.FragmentFavouriteBinding
import com.example.pokemon.model.responce.Pokemon
import org.koin.android.ext.android.inject

class FavouriteFragment: Fragment() {
    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var recycler : RecyclerView
    private var adapter: FavouriteAdapter? = FavouriteAdapter(emptyList())
    private val viewModel by inject<FavouriteViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteBinding.inflate(inflater,container,false)
        recycler = binding.favouriteRecycler
        recycler.layoutManager = GridLayoutManager(context,2)
        recycler.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.pokeLiveData.observe(viewLifecycleOwner,{
            if(it.isNotEmpty()) {
                updateUI(it)
            } else {
                recycler.visibility = View.GONE
                binding.emptyView.visibility = View.VISIBLE
            }
        })
    }

    private fun updateUI(pokemons: List<Pokemon>) {
        recycler.visibility = View.VISIBLE
        binding.emptyView.visibility = View.GONE
        adapter = FavouriteAdapter(pokemons)
        recycler.adapter = adapter
    }
}