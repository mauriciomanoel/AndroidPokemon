package com.mauricio.pokemon.pokemon.viewmodel

import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mauricio.pokemon.PokemonApplication
import com.mauricio.pokemon.R
import com.mauricio.pokemon.di.component.DaggerAppComponent
import com.mauricio.pokemon.pokemon.models.Pokemon
import com.mauricio.pokemon.pokemon.models.TOTAL_INICIAL_POKEMONS
import com.mauricio.pokemon.pokemon.repository.PokemonRepository
import com.mauricio.pokemon.main.BaseViewModel
import com.mauricio.pokemon.network.RetrofitApiService
import javax.inject.Inject

class PokemonViewModel @Inject constructor(private val application: Application): BaseViewModel()  {

    private var  mApplication: Application = application

    @Inject
    lateinit var repository: PokemonRepository

    val pokemons = MutableLiveData<ArrayList<Pokemon>>()
    val fullPokemon = MutableLiveData<Pokemon>()
    val morePokemons = MutableLiveData<ArrayList<Pokemon>>()
    private var offset = 0

    //initializing the necessary components and classes
    init {
        DaggerAppComponent.builder().app(mApplication).build().inject(this)
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
            pokemons.value = it
        }
//        e?.let {
////            messageError.value = mApplication.getString(R.string.error_operation)
//        }
    }

    private fun processPokemons(values: ArrayList<Pokemon>?, e: Throwable?) {
        hideLoading()
        values?.let {
            pokemons.value = it
        }
//        e?.let { messageError.value = mApplication.getString(R.string.error_operation) }
    }

    private fun processMorePokemons(values: ArrayList<Pokemon>?, e: Throwable?) {
        hideLoading()
        values?.let {
            morePokemons.value?.clear()
            morePokemons.value = it
        }
//        e?.let { messageError.value = mApplication.getString(R.string.error_operation) }
    }
}