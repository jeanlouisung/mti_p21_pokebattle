package com.example.mti_p21_pokebattle.interfaces

import com.example.mti_p21_pokebattle.models.PokedexPokemonDetail
import com.example.mti_p21_pokebattle.models.PokemonDetail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeapiWebService {
    @GET("pokemons.json")
    fun listPokemons(): Call<List<PokedexPokemonDetail>>

    @GET("pokemon/{id}")
    fun getPokemonDetail(@Path("id") id: Int): Call<PokemonDetail>
}