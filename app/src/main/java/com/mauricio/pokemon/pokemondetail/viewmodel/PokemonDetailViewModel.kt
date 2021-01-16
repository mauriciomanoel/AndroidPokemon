package com.mauricio.pokemon.pokemondetail.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.mauricio.pokemon.di.component.DaggerAppComponent
import com.mauricio.pokemon.pokemon.repository.PokemonRepository
import com.mauricio.pokemon.main.BaseViewModel
import com.mauricio.pokemon.pokemon.models.Pokemon
import com.mauricio.pokemon.pokemondetail.models.PokemonDetailResponse
import com.mauricio.pokemon.pokemondetail.models.Stat
import com.mauricio.pokemon.pokemondetail.models.Type
import javax.inject.Inject

class PokemonDetailViewModel @Inject constructor(private val application: Application): BaseViewModel()  {

    private var  mApplication: Application = application

    @Inject lateinit var repository: PokemonRepository

    val statsPokemon = MutableLiveData<ArrayList<Stat>>()
    val typesPokemon = MutableLiveData<ArrayList<Type>>()
    val fullPokemon = MutableLiveData<Pokemon>()
    private lateinit var pokemon: Pokemon

    init {
        DaggerAppComponent.builder().app(mApplication).build().inject(this)
    }

    fun checkDataPokemon(pokemon: Pokemon) {
        showLoading()
        this.pokemon = pokemon
        pokemon.detail?.let {detail ->
            fullPokemon.value = pokemon
            statsPokemon.value = ArrayList(detail.stats)
            typesPokemon.value = ArrayList(detail.types)
            hideLoading()
        } ?: run {
            // get Details Pokemon
            pokemon.getId()?.let { id ->
                repository.getDetailPokemon(id, ::processDetailPokemon)
            }
        }
    }

    private fun processDetailPokemon(value: PokemonDetailResponse?, e: Throwable?) {
        hideLoading()
        value?.let {detail ->
            pokemon.detail = detail
            fullPokemon.value = pokemon
            statsPokemon.value = ArrayList(detail.stats)
            typesPokemon.value = ArrayList(detail.types)
        } ?: run {
            // show Error
        }

    }
}