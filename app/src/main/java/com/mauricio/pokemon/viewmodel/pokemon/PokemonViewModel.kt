package com.mauricio.pokemon.viewmodel.pokemon

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.mauricio.pokemon.models.pokemon.Pokemon
import com.mauricio.pokemon.models.pokemon.TOTAL_INICIAL_POKEMONS
import com.mauricio.pokemon.repository.PokemonRepository
import com.mauricio.pokemon.viewmodel.BaseViewModel

class PokemonViewModel(application: Application): BaseViewModel(application) {

    private val repository: PokemonRepository by lazy { PokemonRepository.getInstance(application.baseContext) }
    val pokemons = MutableLiveData<ArrayList<Pokemon>>()
    val fullPokemon = MutableLiveData<Pokemon>()
    val morePokemons = MutableLiveData<ArrayList<Pokemon>>()
    private var offset = 0

    fun getPokemons() {
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
        values?.let {
            pokemons.postValue(it)
        }
        e?.let {
//            onError(Exception(appControllerCalculate.getResourceByKey(ResourcesTranslationConfig.ITSCALCULATOR_SIMULATOR_TECHNICALERROR)))
        }
    }

    private fun processPokemons(values: ArrayList<Pokemon>?, e: Throwable?) {
        values?.let {
            pokemons.value = it
        }
        e?.let {
//            onError(Exception(appControllerCalculate.getResourceByKey(ResourcesTranslationConfig.ITSCALCULATOR_SIMULATOR_TECHNICALERROR)))
        }
    }

    private fun processMorePokemons(values: ArrayList<Pokemon>?, e: Throwable?) {
        values?.let {
            morePokemons.value?.clear()
            morePokemons.value = it
        }
        e?.let {
//            onError(Exception(appControllerCalculate.getResourceByKey(ResourcesTranslationConfig.ITSCALCULATOR_SIMULATOR_TECHNICALERROR)))
        }
    }
}