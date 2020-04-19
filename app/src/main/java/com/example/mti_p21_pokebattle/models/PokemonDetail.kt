package com.example.mti_p21_pokebattle.models

data class PokemonDetail(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val sprites: PokemonSprite,
    val stats: MutableList<PokemonStats>,
    val types: MutableList<PokemonTypes>
)