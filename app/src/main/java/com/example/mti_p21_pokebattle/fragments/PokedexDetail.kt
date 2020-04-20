package com.example.mti_p21_pokebattle.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.mti_p21_pokebattle.R
import com.example.mti_p21_pokebattle.getType
import com.example.mti_p21_pokebattle.interfaces.PokeapiWebService
import com.example.mti_p21_pokebattle.models.PokemonDetail
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_pokedex_detail.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PokedexDetail : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pokedex_detail, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
        val baseUrl = "https://pokeapi.co/api/v2/"

        val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(jsonConverter)
            .build()

        val service: PokeapiWebService = retrofit.create(
            PokeapiWebService::class.java
        )

        val wsCallback: Callback<PokemonDetail> =
            object : Callback<PokemonDetail> {
                override fun onFailure(call: Call<PokemonDetail>, t: Throwable) {
                    // Code here what happens if calling the WebService fails
                    Log.w("Pokedex Detail", "WebService call failed")
                    Log.w("Error", t)
                }

                override fun onResponse(
                    call: Call<PokemonDetail>,
                    response: Response<PokemonDetail>
                ) {
                    Log.d("TAG", "WebService call PokedexDetail " + response.code());
                    if (response.code() == 200) {
                        // We got our data !
                        val responseData = response.body()
                        if (responseData != null) {
                            Glide.with(context!!).load(responseData.sprites.front_default)
                                .into(view.pokedex_detail_pokemon_sprite_img)
                            view.pokedex_detail_pokemon_name.text = responseData.name
                            view.pokedex_detail_pokemon_height_value_text.text =
                                responseData.height.toString()
                            view.pokedex_detail_pokemon_weight_value_text.text =
                                responseData.weight.toString()
                            if (responseData.types.size > 1) {
                                responseData.types.forEach {
                                    if (it.slot == 1) {
                                        view.pokedex_detail_pokemon_type1_img.setImageResource(
                                            getType(
                                                it.type.name
                                            )
                                        )
                                    }
                                    if (it.slot == 2) {
                                        view.pokedex_detail_pokemon_type2_img.setImageResource(
                                            getType(
                                                it.type.name
                                            )
                                        )
                                    }
                                }
                            } else {
                                view.pokedex_detail_pokemon_type1_img.setImageResource(getType(responseData.types[0].type.name))
                                view.pokedex_detail_pokemon_type2_img.setImageResource(android.R.color.transparent)
                            }



                            responseData.stats.forEach {
                                val baseStat = it.base_stat.toString()
                                if (it.stat.name == "speed") {
                                    view.pokedex_detail_pokemon_speed_value_text.text = baseStat
                                }
                                if (it.stat.name == "special-defense") {
                                    view.pokedex_detail_pokemon_specialdef_value_text.text =
                                        baseStat
                                }
                                if (it.stat.name == "special-attack") {
                                    view.pokedex_detail_pokemon_specialatk_value_text.text =
                                        baseStat
                                }
                                if (it.stat.name == "defense") {
                                    view.pokedex_detail_pokemon_defense_value_text.text =
                                        baseStat
                                }
                                if (it.stat.name == "attack") {
                                    view.pokedex_detail_pokemon_attack_value_text.text =
                                        baseStat
                                }
                                if (it.stat.name == "hp") {
                                    view.pokedex_detail_pokemon_hp_value_text.text = baseStat
                                }
                            }
                        }
                    }
                }
            }
        service.getPokemonDetail(arguments!!.getInt("id")).enqueue(wsCallback)
    }
}