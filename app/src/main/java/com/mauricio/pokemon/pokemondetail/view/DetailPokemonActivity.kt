package com.mauricio.pokemon.pokemondetail.view

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.mauricio.pokemon.R
import com.mauricio.pokemon.databinding.ActivityDetailPokemonBinding
import com.mauricio.pokemon.main.MainViewModel
import com.mauricio.pokemon.pokemon.models.Pokemon
import com.mauricio.pokemon.pokemondetail.models.Stat
import com.mauricio.pokemon.pokemondetail.models.Type
import com.mauricio.pokemon.pokemondetail.adapter.PokemonDetailRecyclerViewAdapter
import com.mauricio.pokemon.pokemondetail.viewmodel.PokemonDetailViewModel
import com.mauricio.pokemon.pokemondetail.adapter.PokemonTypeRecyclerViewAdapter
import dagger.android.AndroidInjection
import javax.inject.Inject

class DetailPokemonActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPokemonBinding
    @Inject
    lateinit var viewModel: PokemonDetailViewModel

//    private val viewModel: PokemonDetailViewModel by lazy { ViewModelProviders.of(this).get(
//        PokemonDetailViewModel::class.java) }
    private lateinit var pokemonDetailAdapter: PokemonDetailRecyclerViewAdapter
    private lateinit var pokemonTypeAdapter: PokemonTypeRecyclerViewAdapter
    private val listStatsPokemon: ArrayList<Stat?> = ArrayList()
    private val typesPokemon: ArrayList<Type> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_pokemon)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)
        initToolbar()
        initPokemonAdapter()
        initObservers()
        getDataIntent()
    }

    fun getDataIntent() {
        val pokemon = Gson().fromJson(intent.getStringExtra(Pokemon.TAG), Pokemon::class.java)
        pokemon?.let {
            viewModel.checkDataPokemon(it)
            getSupportActionBar()?.title = it.getNameFormatted()
        }
    }

    fun initToolbar() {
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
    }

    private fun initPokemonAdapter() {
        pokemonDetailAdapter = PokemonDetailRecyclerViewAdapter(listStatsPokemon)
        binding.detailAdapter = pokemonDetailAdapter

        pokemonTypeAdapter = PokemonTypeRecyclerViewAdapter(typesPokemon)
        binding.typeAdapter = pokemonTypeAdapter
    }

    private fun initObservers() {
        viewModel.statsPokemon.observe(this, Observer { pokemons ->
            listStatsPokemon.addAll(pokemons)
            pokemonDetailAdapter.notifyDataSetChanged()
        })

        viewModel.typesPokemon.observe(this, Observer { types ->
            typesPokemon.addAll(types)
            pokemonTypeAdapter.notifyDataSetChanged()
        })

        viewModel.fullPokemon.observe(this, Observer { pokemon ->
            pokemon?.let {
                binding.pokemon = it
            }
        })
    }

    private fun unSubscribeObservers() {
        if (viewModel.statsPokemon.hasObservers()) {
            viewModel.statsPokemon.removeObservers(this)
        }
        if (viewModel.typesPokemon.hasObservers()) {
            viewModel.typesPokemon.removeObservers(this)
        }
        if (viewModel.fullPokemon.hasObservers()) {
            viewModel.typesPokemon.removeObservers(this)
        }
    }

    override fun onBackPressed() {
        if (callingActivity != null) {
            setResult(RESULT_CANCELED)
        }
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        unSubscribeObservers()
        super.onDestroy()
    }
}