package com.mauricio.pokemon.viewmodel.pokemon

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.mauricio.pokemon.models.pokemon.*
import com.mauricio.pokemon.repository.PokemonRepository
import com.mauricio.pokemon.viewmodel.BaseViewModel

class PokemonDetailViewModel(application: Application): BaseViewModel(application) {

    private val repository: PokemonRepository by lazy { PokemonRepository.getInstance(application.baseContext) }
    val statsPokemon = MutableLiveData<ArrayList<Stat>>()
    val typesPokemon = MutableLiveData<ArrayList<Type>>()

    fun checkDataPokemon(pokemon: Pokemon) {

        pokemon.detail?.let {detail ->
            statsPokemon.value = ArrayList(detail.stats)
            typesPokemon.value = ArrayList(detail.types)
        } ?: run {
            // get Details Pokemon
            pokemon.getId()?.let { id ->
                repository.getDetailPokemon(id, ::processDetailPokemon)
            }
        }
    }

    private fun processDetailPokemon(value: PokemonDetailResponse?, e: Throwable?) {
        value?.let {detail ->
            statsPokemon.value = ArrayList(detail.stats)
            typesPokemon.value = ArrayList(detail.types)
        } ?: run {
            // show Error
        }

    }
}