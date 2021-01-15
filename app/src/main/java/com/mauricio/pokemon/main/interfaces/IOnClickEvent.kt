package com.mauricio.pokemon.main.interfaces

import com.mauricio.pokemon.pokemon.models.Pokemon

interface IOnClickEvent {
    fun onClickDetailPokemon(pokemon: Pokemon)
    fun showLoading()
    fun hideLoading()
}