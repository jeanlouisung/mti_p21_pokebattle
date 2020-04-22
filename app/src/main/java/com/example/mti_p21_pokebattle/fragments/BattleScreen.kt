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
import com.example.mti_p21_pokebattle.models.PokemonDetail
import com.example.mti_p21_pokebattle.models.PokemonStatsSimplified
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_battle_screen.view.*
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
class BattleScreen : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_battle_screen, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    private val pokemonsDetail: MutableList<PokemonDetail> = arrayListOf()
    private val pokemonsStats: MutableList<PokemonStatsSimplified> = arrayListOf()
    private var enemyLeft : Int = 3

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
        val data: MutableList<PokedexPokemonDetail> = arrayListOf()

        val baseUrl = "https://pokeapi.co/api/v2/"
        var clickedPokedexLine: PokedexPokemonDetail = PokedexPokemonDetail(1, "", "", arrayListOf())

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
                    Log.w("Battle Screen", "WebService call failed")
                    Log.w("Error", t)
                }

                override fun onResponse(
                    call: Call<PokemonDetail>,
                    response: Response<PokemonDetail>
                ) {
                    Log.d("TAG", "WebService call BattleScreen " + response.code());
                    if (response.code() == 200) {
                        // We got our data !
                        val responseData = response.body()
                        if (responseData != null) {
                            pokemonsDetail.add(responseData)
                            var pokemonStats = PokemonStatsSimplified()
                            responseData.stats.forEach {
                                if (it.stat.name == "speed")
                                    pokemonStats.speed = it.base_stat
                                if (it.stat.name == "special-defense")
                                    pokemonStats.sdefense = it.base_stat
                                if (it.stat.name == "special-attack")
                                    pokemonStats.sattack = it.base_stat
                                if (it.stat.name == "defense")
                                    pokemonStats.defense = it.base_stat
                                if (it.stat.name == "attack")
                                    pokemonStats.attack = it.base_stat
                                if (it.stat.name == "hp")
                                    pokemonStats.hp = it.base_stat
                            }
                            pokemonsStats.add(pokemonStats)
                            loadScreen()
                        }
                    }
                }
            }
        service.getPokemonDetail(arguments!!.getInt("enemy1")).enqueue(wsCallback)
     //   service.getPokemonDetail(arguments!!.getInt("enemy2")).enqueue(wsCallback)
     //   service.getPokemonDetail(arguments!!.getInt("enemy3")).enqueue(wsCallback)
     //   service.getPokemonDetail(arguments!!.getInt("ally1")).enqueue(wsCallback)
     //   service.getPokemonDetail(arguments!!.getInt("ally2")).enqueue(wsCallback)
     //   service.getPokemonDetail(arguments!!.getInt("ally3")).enqueue(wsCallback)
    }

    private fun newEnemy() {
        val enemyIndex = 3 - enemyLeft
        Glide.with(context!!).load(pokemonsDetail[enemyIndex].sprites.front_default)
            .into(view!!.enemyImage)
        view!!.enemyName.text = pokemonsDetail[enemyIndex].name
        view!!.enemyType1.setImageResource(
            getType(pokemonsDetail[enemyIndex].types[0].type.name)
        )
        if (pokemonsDetail[enemyIndex].types.size < 2)
            view!!.enemyType2.setImageResource(android.R.color.transparent)
        else
            view!!.enemyType2.setImageResource(getType(pokemonsDetail[enemyIndex].types[1].type.name))
        view!!.enemyHPValue.text = pokemonsStats[enemyIndex].hp.toString()
    }

    fun updateEnemy() {
        val enemyIndex = 3 - enemyLeft
    }

    fun loadScreen() {
        view!!.enemiesLeftValue.text = enemyLeft.toString()
        newEnemy()
    }
}
