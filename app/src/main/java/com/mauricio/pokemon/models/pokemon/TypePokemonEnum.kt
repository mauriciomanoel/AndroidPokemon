package com.mauricio.pokemon.models.pokemon

enum class TypePokemonEnum(val code: String, val title: String, val color:String) {

    NORMAL("normal", "Normal", "#9DA0AA"),
    FIRE("fire", "Fire", "#FD7D24"),
    WATER("water", "Water", "#4A90DA"),
    ELECTRIC("electric", "Electric", "#EED535"),
    GRASS("grass", "Grass", "#62B957"),
    ICE("ice", "Ice", "#61CEC0"),
    FIGHTING("fighting", "Fighting", "#D04164"),
    POISON("poison", "Poison", "#A552CC"),
    GROUND("ground", "Ground", "#DD7748"),
    FLYING("flying", "Flying", "#748FC9"),
    PSYCHIC("psychic", "Psychic", "#EA5D60"),
    BUG("bug", "Bug", "#8CB230"),
    ROCK("rock", "Rock", "#BAAB82"),
    GHOST("ghost", "Ghost", "#556AAE"),
    DRAGON("dragon", "Dragon", "#0F6AC0"),
    DARK("dark", "Dark", "#58575F"),
    STEEL("steel", "Steel", "#417D9A"),
    FAIRY("fairy", "Fairy", "#ED6EC7");

    companion object {
        fun getByCode(code: String): TypePokemonEnum {
            values().firstOrNull { it.code == code }?.let {
                return it
            } ?: run {
                return NORMAL
            }
        }
    }
}