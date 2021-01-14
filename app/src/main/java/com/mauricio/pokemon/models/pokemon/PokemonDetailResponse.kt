package com.mauricio.pokemon.models.pokemon

import com.google.gson.annotations.SerializedName
import com.mauricio.pokemon.utils.ConverterUtils

data class PokemonDetailResponse (
    val abilities: List<Ability>,
    val forms: List<Species>,
    val height: Long,
    val id: Long,
    val name: String,
    val order: Long,
    val species: Species,
    val sprites: Sprites,
    val stats: List<Stat>,
    val types: List<Type>,
    val weight: Long
) {
    fun getHeightFormmated(): String {
        val valueMeter = (height / 10.0)
        return "${valueMeter}m (${ConverterUtils.meterToFeetFormatted(valueMeter.toFloat())})"
    }
    fun getWeightFormmated(): String {
        val valueKg = (weight / 10.0)
        return "${valueKg}kg (${ConverterUtils.kilogramsToPounds(valueKg.toFloat())})"
    }
    fun getMainAbility(): String? {
        abilities.firstOrNull { it.isHidden }?.let { ability ->
            return "1. ${ability.ability.name.capitalize()}"
        } ?: run {
            return null
        }
    }
    fun geHiddenAbility(): String? {
        abilities.firstOrNull { !it.isHidden }?.let { ability ->
            return "${ability.ability.name.capitalize()} (hidden ability)"
        } ?: run {
            return null
        }
    }
}

data class Ability (
    @SerializedName("ability")
    val ability: AbilitySpecies,
    @SerializedName("is_hidden")
    val isHidden: Boolean,
    val slot: Long
)

data class AbilitySpecies (
    val name: String,
    val url: String
)

data class Species (
    val name: String,
    val url: String
) {
    fun getNameFormatted(): String {
        when (name) {
            enumStatPokemon.HP.code -> return enumStatPokemon.HP.display
            enumStatPokemon.ATTACK.code -> return enumStatPokemon.ATTACK.display
            enumStatPokemon.DEFENSE.code -> return enumStatPokemon.DEFENSE.display
            enumStatPokemon.SPECIAL_ATTACK.code -> return enumStatPokemon.SPECIAL_ATTACK.display
            enumStatPokemon.SPECIAL_DEFENSE.code -> return enumStatPokemon.SPECIAL_DEFENSE.display
            enumStatPokemon.SPEED.code -> return enumStatPokemon.SPEED.display
            else -> return ""
        }
    }
}

data class Sprites (
    val other: Other? = null,
)

data class DreamWorld (
    @SerializedName("front_default")
    val frontDefault: String? = null,
    @SerializedName("front_female")
    val frontFemale: String? = null
)

data class Other (
    @SerializedName("dream_world")
    val dreamWorld: DreamWorld,
    @SerializedName("official-artwork")
    val officialArtwork: OfficialArtwork
)

data class OfficialArtwork (
    @SerializedName("front_default")
    val frontDefault: String
)

data class Stat (
    @SerializedName("base_stat")
    val baseStat: Int,
    val effort: Int,
    val stat: Species
)

data class Type (
    val slot: Long,
    val type: Species
)


enum class enumStatPokemon(val code : String, val display: String) {
    HP("hp", "HP"),
    ATTACK("attack", "Attack"),
    DEFENSE("defense", "Defense"),
    SPECIAL_ATTACK("special-attack", "Sp. Atk"),
    SPECIAL_DEFENSE("special-defense", "Sp. Def"),
    SPEED("speed", "Speed")
}


