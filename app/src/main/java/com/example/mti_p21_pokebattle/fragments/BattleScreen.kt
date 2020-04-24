package com.example.mti_p21_pokebattle.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

import com.example.mti_p21_pokebattle.R
import com.example.mti_p21_pokebattle.getType
import com.example.mti_p21_pokebattle.interfaces.PokeapiWebService
import com.example.mti_p21_pokebattle.models.PokemonDetail
import com.example.mti_p21_pokebattle.models.PokemonStatsSimplified
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_battle_screen.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

    private val pokemonsDetail: MutableList<PokemonDetail> = arrayListOf()            // [enemy1, enemy2, enemy3, ally1, ally2, ally3]
    private val pokemonsStats: MutableList<PokemonStatsSimplified> = arrayListOf()   // [enemy1, enemy2, enemy3, ally1, ally2, ally3]
    private var identifiedPokemon : Int = 0
    private var currentEnemyIndex : Int = 0
    private var currentAllyIndex : Int = 3

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
                            addDataToPokemonsDetail(responseData)
                        }
                    }
                }
            }
        service.getPokemonDetail(arguments!!.getInt("enemy1")).enqueue(wsCallback)
        service.getPokemonDetail(arguments!!.getInt("enemy2")).enqueue(wsCallback)
        service.getPokemonDetail(arguments!!.getInt("enemy3")).enqueue(wsCallback)
        service.getPokemonDetail(arguments!!.getInt("ally1")).enqueue(wsCallback)
        service.getPokemonDetail(arguments!!.getInt("ally2")).enqueue(wsCallback)
        service.getPokemonDetail(arguments!!.getInt("ally3")).enqueue(wsCallback)
    }

    private fun generatePokemonStats() {
        pokemonsDetail.forEach {
            var pokemonStats = PokemonStatsSimplified()
            it.stats.forEach {
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
            pokemonsStats.add(pokemonStats);
        }
    }

    private fun sortPokemonsDetail() {
        for (index in 0..5) {
            if (pokemonsDetail[index].id == arguments!!.getInt("enemy1")) {
                val tmp = pokemonsDetail[0]
                pokemonsDetail[0] = pokemonsDetail[index]
                pokemonsDetail[index] = tmp
                break;
            }
        }
        for (index in 1..5) {
            if (pokemonsDetail[index].id == arguments!!.getInt("enemy2")) {
                val tmp = pokemonsDetail[1]
                pokemonsDetail[1] = pokemonsDetail[index]
                pokemonsDetail[index] = tmp
                break;
            }
        }
        for (index in 2..5) {
            if (pokemonsDetail[index].id == arguments!!.getInt("enemy3")) {
                val tmp = pokemonsDetail[2]
                pokemonsDetail[2] = pokemonsDetail[index]
                pokemonsDetail[index] = tmp
                break;
            }
        }
        for (index in 3..5) {
            if (pokemonsDetail[index].id == arguments!!.getInt("ally1")) {
                val tmp = pokemonsDetail[3]
                pokemonsDetail[3] = pokemonsDetail[index]
                pokemonsDetail[index] = tmp
                break;
            }
        }
        for (index in 4..5) {
            if (pokemonsDetail[index].id == arguments!!.getInt("ally2")) {
                val tmp = pokemonsDetail[4]
                pokemonsDetail[4] = pokemonsDetail[index]
                pokemonsDetail[index] = tmp
                break;
            }
        }
    }

    private fun addDataToPokemonsDetail(detail: PokemonDetail) {
        pokemonsDetail.add(detail)
        identifiedPokemon++
        if (identifiedPokemon == 6) {
            sortPokemonsDetail()
            generatePokemonStats()
            loadScreen()
        }
    }

    private fun updateHP(stats: PokemonStatsSimplified, HPTextView: TextView) {
        HPTextView.text = stats.hp.toString()
    }

    private fun loadSprite(detail: PokemonDetail, spriteImageView: ImageView) {
        Glide.with(context!!).load(detail.sprites.front_default).into(spriteImageView)
    }

    private fun loadCurrentPokemon() {
        loadSprite(pokemonsDetail[currentAllyIndex], view!!.currentImage)
        updateHP(pokemonsStats[currentAllyIndex], view!!.currentHPValue)
        view!!.currentName.text = pokemonsDetail[currentAllyIndex].name
        view!!.currentType1.setImageResource(
            getType(pokemonsDetail[currentAllyIndex].types[0].type.name)
        )
        if (pokemonsDetail[currentAllyIndex].types.size < 2)
            view!!.currentType2.setImageResource(android.R.color.transparent)
        else
            view!!.currentType2.setImageResource(getType(pokemonsDetail[currentAllyIndex].types[1].type.name))
    }

    private fun loadAlliesSprites() {
        loadSprite(pokemonsDetail[3], view!!.allyImage1)
        updateHP(pokemonsStats[3], view!!.allyHPValue1)
        loadSprite(pokemonsDetail[4], view!!.allyImage2)
        updateHP(pokemonsStats[4], view!!.allyHPValue2)
        loadSprite(pokemonsDetail[5], view!!.allyImage3)
        updateHP(pokemonsStats[5], view!!.allyHPValue3)
        loadCurrentPokemon()
    }

    private fun newEnemy() {
        loadSprite(pokemonsDetail[currentEnemyIndex], view!!.enemyImage)
        updateHP(pokemonsStats[currentEnemyIndex], view!!.enemyHPValue)
        view!!.enemyName.text = pokemonsDetail[currentEnemyIndex].name
        view!!.enemyType1.setImageResource(
            getType(pokemonsDetail[currentEnemyIndex].types[0].type.name)
        )
        if (pokemonsDetail[currentEnemyIndex].types.size < 2)
            view!!.enemyType2.setImageResource(android.R.color.transparent)
        else
            view!!.enemyType2.setImageResource(getType(pokemonsDetail[currentEnemyIndex].types[1].type.name))
    }

    fun loadScreen() {
        view!!.enemiesLeftValue.text = (3 - currentEnemyIndex).toString()
        newEnemy()
        loadAlliesSprites()
    }
}
