package com.mauricio.pokemon.pokemon.viewmodel

import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import com.mauricio.pokemon.R
import com.mauricio.pokemon.pokemon.models.Pokemon
import com.mauricio.pokemon.pokemon.models.TOTAL_INICIAL_POKEMONS
import com.mauricio.pokemon.pokemon.repository.PokemonRepository
import com.mauricio.pokemon.main.BaseViewModel
import javax.inject.Inject

class PokemonViewModel @Inject constructor(application: Application) : BaseViewModel(application) {

    private val mApplication: Application = application
    @Inject lateinit var repository: PokemonRepository
    val pokemons = MutableLiveData<ArrayList<Pokemon>>()
    val fullPokemon = MutableLiveData<Pokemon>()
    val morePokemons = MutableLiveData<ArrayList<Pokemon>>()
    private var offset = 0

//    constructor(application: Application): super(application) {
//        mApplication = application
//        this.repository = PokemonRepository.getInstance(application)
//    }

    @VisibleForTesting
    constructor(application: Application, repository: PokemonRepository) : this(application) {
        this.repository = repository
    }

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
        fullPokemon.value = value
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