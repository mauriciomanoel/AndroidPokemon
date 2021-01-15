package com.mauricio.pokemon.pokemon.models

import android.net.Uri
import com.mauricio.pokemon.pokemondetail.models.PokemonDetailResponse

class Pokemon(
    val name: String,
    val url: String,
    var detail: PokemonDetailResponse? = null
) {
    fun getIdFormatted(): String {
        return "#${String.format("%04d", getId())}"
    }
    fun getImage(): String {
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${getId()}.png"
    }
    fun getId(): Int? {
        return Uri.parse(url).lastPathSegment?.toInt()
    }
    fun getNameFormatted():String {
        return name.capitalize()
    }
    fun getWidthImageList(): Int {
        return POKEMON_IMAGE_LIST_WIDTH
    }
    fun getHeightImageList(): Int {
        return POKEMON_IMAGE_LIST_HEIGHT
    }
    fun getWidthImageDetail(): Int {
        return POKEMON_IMAGE_DETAIL_WIDTH
    }
    fun getHeightImageDetail(): Int {
        return POKEMON_IMAGE_DETAIL_HEIGHT
    }
    fun isDetailAvaiable(): Boolean {
        var value = true
        if (detail == null) value = false
        return value
    }
    override fun toString(): String {
        return "${getId()}"
    }
    companion object {
        val TAG = Pokemon::class.java.name
    }
}

