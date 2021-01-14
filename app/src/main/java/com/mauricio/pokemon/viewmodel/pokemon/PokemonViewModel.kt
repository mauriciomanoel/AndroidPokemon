package com.mauricio.pokemon.viewmodel.pokemon

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.mauricio.pokemon.R
import com.mauricio.pokemon.models.pokemon.Pokemon
import com.mauricio.pokemon.models.pokemon.TOTAL_INICIAL_POKEMONS
import com.mauricio.pokemon.repository.PokemonRepository
import com.mauricio.pokemon.viewmodel.BaseViewModel

class PokemonViewModel(application: Application): BaseViewModel(application) {
    private val mApplication: Application = application
    private val repository: PokemonRepository by lazy { PokemonRepository.getInstance(application.baseContext) }
    val pokemons = MutableLiveData<ArrayList<Pokemon>>()
    val fullPokemon = MutableLiveData<Pokemon>()
    val morePokemons = MutableLiveData<ArrayList<Pokemon>>()
    private var offset = 0

    fun getPokemons() {
        showLoading()
        repository.getPokemons(limit = TOTAL_INICIAL_POKEMONS, offset = offset, ::processPokemons, ::processFullPokemon)
    }

    fun getFullPokemons() {
        repository.getFullPokemons(limit = TOTAL_INICIAL_POKEMONS, offset = offset, ::processFullPokemons)
    }

    fun getMorePokemons() {
        offset += TOTAL_INICIAL_POKEMONS
        repository.getPokemons(limit = TOTAL_INICIAL_POKEMONS, offset = offset, ::processMorePokemons, ::processFullPokemon)
    }

    private fun processFullPokemon(value: Pokemon) {
        fullPokemon.postValue(value)
    }

    private fun processFullPokemons(values: ArrayList<Pokemon>?, e: Throwable?) {
        hideLoading()
        values?.let {
            pokemons.postValue(it)
        }
        e?.let { messageError.value = mApplication.getString(R.string.error_operation) }
    }

    private fun processPokemons(values: ArrayList<Pokemon>?, e: Throwable?) {
        hideLoading()
        values?.let {
            pokemons.value = it
        }
        e?.let { messageError.value = mApplication.getString(R.string.error_operation) }
    }

    private fun processMorePokemons(values: ArrayList<Pokemon>?, e: Throwable?) {
        hideLoading()
        values?.let {
            morePokemons.value?.clear()
            morePokemons.value = it
        }
        e?.let { messageError.value = mApplication.getString(R.string.error_operation) }
    }
}