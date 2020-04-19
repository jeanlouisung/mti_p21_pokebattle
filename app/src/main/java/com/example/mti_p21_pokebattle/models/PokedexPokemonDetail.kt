package com.example.mti_p21_pokebattle.models

data class PokedexPokemonDetail(val id: Int, val name: String, val sprite: String, val types: MutableList<PokedexPokemonType>)