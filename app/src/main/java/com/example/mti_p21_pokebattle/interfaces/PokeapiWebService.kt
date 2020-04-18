package com.example.mti_p21_pokebattle.interfaces

import retrofit2.Call
import retrofit2.http.GET
import com.example.mti_p21_pokebattle.models.PokemonDetail as PokemonDetail

interface PokeapiWebService {
    @GET("pokemons.json")
    fun listPokemons(): Call<List<PokemonDetail>>
}