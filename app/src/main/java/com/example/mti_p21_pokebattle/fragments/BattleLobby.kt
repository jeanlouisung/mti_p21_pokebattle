package com.example.mti_p21_pokebattle.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide

import com.example.mti_p21_pokebattle.R
import com.example.mti_p21_pokebattle.adapters.PokedexAdapter
import com.example.mti_p21_pokebattle.getType
import com.example.mti_p21_pokebattle.interfaces.PokeapiWebService
import com.example.mti_p21_pokebattle.models.PokedexPokemonDetail
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_battle_lobby.*
import kotlinx.android.synthetic.main.fragment_pokedex.*
import kotlinx.android.synthetic.main.fragment_pokedex_detail.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.random.Random

/**
 * A simple [Fragment] subclass.
 */
class BattleLobby : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_battle_lobby, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)

        val data: MutableList<PokedexPokemonDetail> = arrayListOf()
        val myPokemonTeamsId: Array<Number> = Array(3) { i -> 0 }

        val baseUrl = "https://www.surleweb.xyz/api/"
        var clickedPokedexLine: PokedexPokemonDetail = PokedexPokemonDetail(1, "", "", arrayListOf())

        val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(jsonConverter)
            .build()

        val service: PokeapiWebService = retrofit.create(
            PokeapiWebService::class.java
        )

        fight_button.setVisibility(View.GONE)
        selected_pokemon_type1.setVisibility(View.INVISIBLE)
        selected_pokemon_type2.setVisibility(View.INVISIBLE)
        selected_pokemon_name.setVisibility(View.INVISIBLE)

        val onPokedexLineClickListener = View.OnClickListener {
            clickedPokedexLine = it.tag as PokedexPokemonDetail
            selected_pokemon_name.text = clickedPokedexLine.name
            selected_pokemon_name.setVisibility(View.VISIBLE)
            if (clickedPokedexLine.types.size > 1) {
                selected_pokemon_type1.setImageResource(getType(clickedPokedexLine.types[0].name))
                selected_pokemon_type2.setImageResource(getType(clickedPokedexLine.types[1].name))
                selected_pokemon_type1.setVisibility(View.VISIBLE)
                selected_pokemon_type2.setVisibility(View.VISIBLE)
            }
            else if (clickedPokedexLine.types.size == 1) {
                selected_pokemon_type1.setImageResource(getType(clickedPokedexLine.types[0].name))
                selected_pokemon_type1.setVisibility(View.VISIBLE)
                selected_pokemon_type2.setVisibility(View.INVISIBLE)
            }
        }

        first_select_button.setOnClickListener {
            if (selected_pokemon_name.text != "TextView") {
                first_select_name.text = clickedPokedexLine.name
                Glide.with(context!!).load(clickedPokedexLine.sprite)
                    .into(first_select_image)
                myPokemonTeamsId[0] = clickedPokedexLine.id
            }
            showFightButton(myPokemonTeamsId)
        }

        second_select_button.setOnClickListener {
            if (selected_pokemon_name.text != "TextView") {
                second_select_name.text = clickedPokedexLine.name
                Glide.with(context!!).load(clickedPokedexLine.sprite)
                    .into(second_select_image)
                myPokemonTeamsId[1] = clickedPokedexLine.id
            }
            showFightButton(myPokemonTeamsId)

        }

        third_select_button.setOnClickListener {
            if (selected_pokemon_name.text != "TextView") {
                third_select_name.text = clickedPokedexLine.name
                Glide.with(context!!).load(clickedPokedexLine.sprite)
                    .into(third_select_image)
                myPokemonTeamsId[2] = clickedPokedexLine.id
            }
            showFightButton(myPokemonTeamsId)
        }

        val wsCallback: Callback<List<PokedexPokemonDetail>> =
            object : Callback<List<PokedexPokemonDetail>> {
                override fun onFailure(call: Call<List<PokedexPokemonDetail>>, t: Throwable) {
                    // Code here what happens if calling the WebService fails
                    Log.w("TAG", "WebService call failed")
                    Log.w("Error", t);
                }

                override fun onResponse(
                    call: Call<List<PokedexPokemonDetail>>,
                    response: Response<List<PokedexPokemonDetail>>
                ) {
                    Log.d("TAG", "WebService call " + response.code());
                    if (response.code() == 200) {
                        // We got our data !
                        val responseData = response.body()
                        if (responseData != null) {
                            data.addAll(responseData.sortedWith(compareBy { it.types.get(0).id }))
                            pokemon_list.setHasFixedSize(true)
                            pokemon_list.layoutManager =
                                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                            pokemon_list.adapter =
                                PokedexAdapter(
                                    data, context, onPokedexLineClickListener
                                )
                            val OpponentPokemons = getRandomPokemon(data)
                        }
                    }
                }
            }
        service.listPokemons().enqueue(wsCallback);

    }

    fun getRandomPokemon(data: MutableList<PokedexPokemonDetail>): MutableList<PokedexPokemonDetail> {
        val listPokemons: MutableList<PokedexPokemonDetail> = arrayListOf()
        val randomValues = List(3) { Random.nextInt(0, data.size) }
        for (value in randomValues) {
            listPokemons.add(data[value])
        }
        Glide.with(context!!).load(listPokemons[0].sprite)
            .into(first_opponent_pokemon_image)
        first_opponent_pokemon_name.text = listPokemons[0].name
        if (listPokemons[0].types.size == 1) {
            first_oponnent_pokemon_type1.setImageResource(getType(listPokemons[0].types[0].name))
            first_opponent_pokemon_type2.setVisibility(View.INVISIBLE)
        }
        else {
            first_oponnent_pokemon_type1.setImageResource(getType(listPokemons[0].types[0].name))
            first_opponent_pokemon_type2.setImageResource(getType(listPokemons[0].types[1].name))
        }
        return listPokemons
    }

    fun showFightButton(array: Array<Number>) {
        if (array[0] != 0 && array[1] != 0 && array[2] != 0) {
            fight_button.setVisibility(View.VISIBLE)
        }
    }

}
