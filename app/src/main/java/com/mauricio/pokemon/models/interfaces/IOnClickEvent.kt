package com.mauricio.pokemon.models.interfaces

import com.mauricio.pokemon.models.pokemon.Pokemon

interface IOnClickEvent {
    fun onClickDetailPokemon(pokemon: Pokemon)
    fun showLoading()
    fun hideLoading()
}