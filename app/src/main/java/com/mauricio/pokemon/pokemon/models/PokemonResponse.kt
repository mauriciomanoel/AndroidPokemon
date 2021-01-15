package com.mauricio.pokemon.pokemon.models

class PokemonResponse (
    val count: Int,
    val next: String,
    val previous: String,
    val results: ArrayList<Result>
)

data class Result (
    val name: String,
    val url: String
)
