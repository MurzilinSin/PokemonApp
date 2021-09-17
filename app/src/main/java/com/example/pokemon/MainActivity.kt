package com.example.pokemon

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.pokemon.databinding.MainActivityBinding
import com.example.pokemon.model.Type

class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainActivityBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Pokemon)
        binding = MainActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val bottomNav = binding.bottomNavigationView
        navController = Navigation.findNavController(this,R.id.nav_host)
        NavigationUI.setupWithNavController(bottomNav,navController)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val bundle = Bundle()
        return when(item.itemId){
            R.id.searchFragment ->{
                bundle.putSerializable("searchType", Type.Search)
                navController.navigate(R.id.searchFragment,bundle)
                return true
            }
            R.id.randomFragment -> {
                bundle.putSerializable("randomType",Type.Random)
                navController.navigate(R.id.searchFragment,bundle)
                return true
            }
            else -> {
                NavigationUI.onNavDestinationSelected(item,navController) || super.onOptionsItemSelected(item)
            }
        }
    }
}