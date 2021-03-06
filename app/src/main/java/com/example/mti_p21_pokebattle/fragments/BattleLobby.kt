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
import kotlinx.android.synthetic.main.fragment_battle_lobby.view.*
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

    //TypeHelp
    private var RandomPokemonType1 : String? = null
    private var RandomPokemonType2 : String? = null
    private var CurrentPokemonType1 : String? = null
    private var CurrentPokemonType2 : String? = null

    //BattleScreen
    private val myPokemonTeamsId : Array<Int> = Array(3) { _ -> 0 }
    private val opponentPokemons: MutableList<PokedexPokemonDetail> = arrayListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
        val data: MutableList<PokedexPokemonDetail> = arrayListOf()

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

                //TypeHelp
                CurrentPokemonType1 = clickedPokedexLine.types[0].name
                CurrentPokemonType2 = clickedPokedexLine.types[1].name
            }
            else if (clickedPokedexLine.types.size == 1) {
                selected_pokemon_type1.setImageResource(getType(clickedPokedexLine.types[0].name))
                selected_pokemon_type1.setVisibility(View.VISIBLE)
                selected_pokemon_type2.setVisibility(View.INVISIBLE)

                //TypeHelp
                CurrentPokemonType1 = clickedPokedexLine.types[0].name
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
                            getRandomPokemon(data)
                        }
                    }
                }
            }

        //TypeHelp
        val onRandomType1ButtonClickListener = View.OnClickListener {
            val fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()

            val dataBundle = Bundle()
            dataBundle.putString("typeName", RandomPokemonType1)

            val typeHelp = TypeHelp()
            typeHelp.arguments = dataBundle

            fragmentTransaction.add(R.id.main_container, typeHelp, "TypeHelp")
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        //TypeHelp
        val onRandomType2ButtonClickListener = View.OnClickListener {
            if (RandomPokemonType2 != null) {
                val fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()

                val dataBundle = Bundle()
                dataBundle.putString("typeName", RandomPokemonType2)

                val typeHelp = TypeHelp()
                typeHelp.arguments = dataBundle

                fragmentTransaction.add(R.id.main_container, typeHelp, "TypeHelp")
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
        }

        //TypeHelp
        val onCurrentType1ButtonClickListener = View.OnClickListener {
            val fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()

            val dataBundle = Bundle()
            dataBundle.putString("typeName", CurrentPokemonType1)

            val typeHelp = TypeHelp()
            typeHelp.arguments = dataBundle

            fragmentTransaction.add(R.id.main_container, typeHelp, "TypeHelp")
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        //TypeHelp
        val onCurrentType2ButtonClickListener = View.OnClickListener {
            if (CurrentPokemonType2 != null) {
                val fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()

                val dataBundle = Bundle()
                dataBundle.putString("typeName", CurrentPokemonType2)

                val typeHelp = TypeHelp()
                typeHelp.arguments = dataBundle

                fragmentTransaction.add(R.id.main_container, typeHelp, "TypeHelp")
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
        }

        //BattleScreen
        val onFightButtonClickListener = View.OnClickListener {
            val fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()

            val dataBundle = Bundle()
            dataBundle.putInt("enemy1", opponentPokemons[0].id)
            dataBundle.putInt("enemy2", opponentPokemons[1].id)
            dataBundle.putInt("enemy3", opponentPokemons[2].id)
            dataBundle.putInt("ally1", myPokemonTeamsId[0])
            dataBundle.putInt("ally2", myPokemonTeamsId[1])
            dataBundle.putInt("ally3", myPokemonTeamsId[2])

            val battleScreen = BattleScreen()
            battleScreen.arguments = dataBundle

            fragmentTransaction.replace(R.id.main_container, battleScreen, "BattleScreen")
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        //TypeHelp
        view.first_oponnent_pokemon_type1.setOnClickListener(onRandomType1ButtonClickListener)
        view.first_opponent_pokemon_type2.setOnClickListener(onRandomType2ButtonClickListener)
        view.selected_pokemon_type1.setOnClickListener(onCurrentType1ButtonClickListener)
        view.selected_pokemon_type2.setOnClickListener(onCurrentType2ButtonClickListener)

        view.fight_button.setOnClickListener(onFightButtonClickListener)
        service.listPokemons().enqueue(wsCallback);

    }

    fun getRandomPokemon(data: MutableList<PokedexPokemonDetail>) {
        val randomValues = List(3) { Random.nextInt(0, data.size) }
        for (value in randomValues) {
            opponentPokemons.add(data[value])
        }
        Glide.with(context!!).load(opponentPokemons[0].sprite)
            .into(first_opponent_pokemon_image)
        first_opponent_pokemon_name.text = opponentPokemons[0].name
        if (opponentPokemons[0].types.size == 1) {
            first_oponnent_pokemon_type1.setImageResource(getType(opponentPokemons[0].types[0].name))
            first_opponent_pokemon_type2.setVisibility(View.INVISIBLE)

            //TypeHelp
            RandomPokemonType1 = opponentPokemons[0].types[0].name
        }
        else {
            first_oponnent_pokemon_type1.setImageResource(getType(opponentPokemons[0].types[0].name))
            first_opponent_pokemon_type2.setImageResource(getType(opponentPokemons[0].types[1].name))

            //TypeHelp
            RandomPokemonType1 = opponentPokemons[0].types[0].name
            RandomPokemonType2 = opponentPokemons[0].types[1].name
        }
    }

    fun showFightButton(array: Array<Int>) {
        if (array[0] != 0 && array[1] != 0 && array[2] != 0) {
            fight_button.setVisibility(View.VISIBLE)
        }
    }

}
