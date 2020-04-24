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
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide

import com.example.mti_p21_pokebattle.R
import com.example.mti_p21_pokebattle.adapters.MoveAdapter
import com.example.mti_p21_pokebattle.getType
import com.example.mti_p21_pokebattle.interfaces.PokeapiWebService
import com.example.mti_p21_pokebattle.models.Move
import com.example.mti_p21_pokebattle.models.PokemonDetail
import com.example.mti_p21_pokebattle.models.PokemonStatsSimplified
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_battle_screen.*
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
    private val pokemonsMoves: MutableList<MutableList<Move>> = arrayListOf()         // [enemy1, enemy2, enemy3, ally1, ally2, ally3]
    private var currentPokemonMoves: MutableList<Move> = arrayListOf()
    private var sizeOfPokemonMovesList : Int = 0                                // var used to know when we have gotten every moves
    private var identifiedPokemon : Int = 0                                     // var used to know when we have gotten every pokemon details
    private var currentEnemyIndex : Int = 0                                     // index of the current enemy in the Lists
    private var currentAllyIndex : Int = 3                                       // index of the current ally in the Lists

    private val baseUrl = "https://pokeapi.co/api/v2/"
    private val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(jsonConverter)
        .build()

    private val service: PokeapiWebService = retrofit.create(
        PokeapiWebService::class.java
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)

        val pokemonDetailCallback: Callback<PokemonDetail> =
            object : Callback<PokemonDetail> {
                override fun onFailure(call: Call<PokemonDetail>, t: Throwable) {
                    // Code here what happens if calling the WebService fails
                    Log.w("Battle Screen Pokemon", "WebService call failed")
                    Log.w("Error", t)
                }

                override fun onResponse(
                    call: Call<PokemonDetail>,
                    response: Response<PokemonDetail>
                ) {
                    Log.d("TAG", "WebService call BattleScreen Pokemon" + response.code());
                    if (response.code() == 200) {
                        // We got our data !
                        val responseData = response.body()
                        if (responseData != null) {
                            addDataToPokemonsDetail(responseData)
                        }
                    }
                }
            }
        val onSwitch1ButtonClickListener = View.OnClickListener {
            currentAllyIndex = 3
            loadCurrentPokemon()
            updateSwitchButtons()
        }
        val onSwitch2ButtonClickListener = View.OnClickListener {
            currentAllyIndex = 4
            loadCurrentPokemon()
            updateSwitchButtons()
        }
        val onSwitch3ButtonClickListener = View.OnClickListener {
            currentAllyIndex = 5
            loadCurrentPokemon()
            updateSwitchButtons()
        }


        view.switchAlly1.setOnClickListener(onSwitch1ButtonClickListener)
        view.switchAlly2.setOnClickListener(onSwitch2ButtonClickListener)
        view.switchAlly3.setOnClickListener(onSwitch3ButtonClickListener)

        service.getPokemonDetail(arguments!!.getInt("enemy1")).enqueue(pokemonDetailCallback)
        service.getPokemonDetail(arguments!!.getInt("enemy2")).enqueue(pokemonDetailCallback)
        service.getPokemonDetail(arguments!!.getInt("enemy3")).enqueue(pokemonDetailCallback)
        service.getPokemonDetail(arguments!!.getInt("ally1")).enqueue(pokemonDetailCallback)
        service.getPokemonDetail(arguments!!.getInt("ally2")).enqueue(pokemonDetailCallback)
        service.getPokemonDetail(arguments!!.getInt("ally3")).enqueue(pokemonDetailCallback)

        movesList.setHasFixedSize(false)
        movesList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        movesList.adapter =
            MoveAdapter(
                currentPokemonMoves, context, onMoveLineClickListener
            )

        for (i in 1..6)
            pokemonsMoves.add(arrayListOf())
    }

    val moveCallback: Callback<Move> =
        object : Callback<Move> {
            override fun onFailure(call: Call<Move>, t: Throwable) {
                // Code here what happens if calling the WebService fails
                Log.w("Battle Screen Move", "WebService call failed")
                Log.w("Error", t)
            }

            override fun onResponse(
                call: Call<Move>,
                response: Response<Move>
            ) {
                Log.d("TAG", "WebService call BattleScreen Move" + response.code());
                if (response.code() == 200) {
                    // We got our data !
                    val responseData = response.body()
                    if (responseData != null) {
                        currentPokemonMoves.add(responseData)
                        if (currentPokemonMoves.size == sizeOfPokemonMovesList) {
                            var filteredPokemonMove: MutableList<Move> = arrayListOf()
                            currentPokemonMoves.forEach{
                                if (it.power > 0 && it.accuracy > 0)
                                    filteredPokemonMove.add(it)
                            }
                            currentPokemonMoves = filteredPokemonMove.sortedByDescending { it.power }.toMutableList()
                            pokemonsMoves[currentAllyIndex] = currentPokemonMoves
                            (movesList.adapter as MoveAdapter).setMoves(currentPokemonMoves)
                        }
                    }
                }
            }
        }

    // fill the pokemonsStats List
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

    // Sort the pokemonsDetail List
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

    // add a pokemon detail to the pokemonsDetail List and sort it when we get all 6 pokemons detail
    private fun addDataToPokemonsDetail(detail: PokemonDetail) {
        pokemonsDetail.add(detail)
        identifiedPokemon++
        if (identifiedPokemon == 6) {
            sortPokemonsDetail()
            generatePokemonStats()
            loadScreen()
        }
    }

    // update a HP text view
    // stats: the stats of the pokemon associated to the HPTextView
    // HPTextView: the TextView of the HP associated to the pokemon
    private fun updateHP(stats: PokemonStatsSimplified, HPTextView: TextView) {
        HPTextView.text = stats.hp.toString()
    }

    // update a sprite image view
    // detail: the detail of the pokemon associated to the spriteImageView
    // spriteImageView: the ImageView of the sprite associated to the pokemon
    private fun loadSprite(detail: PokemonDetail, spriteImageView: ImageView) {
        Glide.with(context!!).load(detail.sprites.front_default).into(spriteImageView)
    }

    //TODO
    // clickListener of an attack (called when the user click on an attack)
    // it will be used in the MoveAdapter, you may need to make some functions public
    val onMoveLineClickListener = View.OnClickListener {
        val clickedPokedexLine = it.tag as Move
    }

    //TODO
    //
    fun calculateDamage() {}

    // load the moves of the current ally pokemon
    private fun loadCurrentPokemonMoves() {
        currentPokemonMoves = pokemonsMoves[currentAllyIndex]
        (movesList.adapter as MoveAdapter).setMoves(currentPokemonMoves)
        if (currentPokemonMoves.size < 1) {
            val movesDetail = pokemonsDetail[currentAllyIndex].moves
            sizeOfPokemonMovesList = movesDetail.size
            movesDetail.forEach {
                val name = it.move.name
                service.getMoveDetail(name).enqueue(moveCallback)
            }
        }
    }

    // load the sprite, the HP value and the moves of the current pokemon
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
        loadCurrentPokemonMoves()
    }

    // load the sprite and HP of the 3 allies then load the current pokemon
    private fun loadAlliesSprites() {
        loadSprite(pokemonsDetail[3], view!!.allyImage1)
        loadSprite(pokemonsDetail[4], view!!.allyImage2)
        loadSprite(pokemonsDetail[5], view!!.allyImage3)
        updateHP(pokemonsStats[3], view!!.allyHPValue1)
        updateHP(pokemonsStats[4], view!!.allyHPValue2)
        updateHP(pokemonsStats[5], view!!.allyHPValue3)
        loadCurrentPokemon()
    }

    // update the switch buttons, hide the button of the current pokemon and of the pokemons with 0HP left
    private fun updateSwitchButtons() {
        if (currentAllyIndex == 3 || pokemonsStats[3].hp == 0)
            view!!.switchAlly1.setVisibility(View.GONE)
        else
            view!!.switchAlly1.setVisibility(View.VISIBLE)
        if (currentAllyIndex == 4 || pokemonsStats[4].hp == 0)
            view!!.switchAlly2.setVisibility(View.GONE)
        else
            view!!.switchAlly2.setVisibility(View.VISIBLE)
        if (currentAllyIndex == 5 || pokemonsStats[5].hp == 0)
            view!!.switchAlly3.setVisibility(View.GONE)
        else
            view!!.switchAlly3.setVisibility(View.VISIBLE)
    }

    // update the current enemy (include sprite, HP and types
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

    // load screen (include enemy left value, the current enemy, allies and switch buttons)
    fun loadScreen() {
        view!!.enemiesLeftValue.text = (3 - currentEnemyIndex).toString()
        newEnemy()
        loadAlliesSprites()
        updateSwitchButtons()
    }
}
