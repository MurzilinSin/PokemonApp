package com.example.pokemon.ui.favourite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemon.R
import com.example.pokemon.model.responce.Pokemon
import com.example.pokemon.ui.favourite.FavouriteAdapter.*
import com.squareup.picasso.Picasso

class FavouriteAdapter(var pokemons: List<Pokemon>): RecyclerView.Adapter<PokemonViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PokemonViewHolder(layoutInflater.inflate(R.layout.recycler_favourite_poke_item,parent,false))
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = pokemons[position]
        holder.apply {
            bind(pokemon)
        }
    }

    override fun getItemCount(): Int {
        return pokemons.size
    }
    inner class PokemonViewHolder(view: View): RecyclerView.ViewHolder(view) {
        lateinit var pokemon: Pokemon
        val image = itemView.findViewById<ImageView>(R.id.poke_img)
        fun bind(pokemon: Pokemon){
            this.pokemon = pokemon
            Picasso
                .get()
                .load(pokemon.sprites.front_default)
                .into(image)
        }
    }
}