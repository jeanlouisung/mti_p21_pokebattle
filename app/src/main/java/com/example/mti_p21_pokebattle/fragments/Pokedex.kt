package com.example.mti_p21_pokebattle.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mti_p21_pokebattle.interfaces.PokeapiWebService
import com.example.mti_p21_pokebattle.adapters.PokedexAdapter
import com.example.mti_p21_pokebattle.R
import com.example.mti_p21_pokebattle.models.PokedexPokemonDetail
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_pokedex.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * A simple [Fragment] subclass.
 */
class Pokedex : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pokedex, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
        val data: MutableList<PokedexPokemonDetail> = arrayListOf()

        val baseUrl = "https://www.surleweb.xyz/api/"

        val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(jsonConverter)
            .build()

        val service: PokeapiWebService = retrofit.create(
            PokeapiWebService::class.java
        )

        val onPokedexLineClickListener = View.OnClickListener {
//            val position = it.tag as Int
            val clickedPokedexLine = it.tag as PokedexPokemonDetail

            val fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()

            val dataBundle = Bundle()
            dataBundle.putInt("id", clickedPokedexLine.id)

            val pokedexDetailFragment = PokedexDetail()
            pokedexDetailFragment.arguments = dataBundle

            fragmentTransaction.replace(R.id.main_container, pokedexDetailFragment)
            fragmentTransaction.addToBackStack(fragmentManager!!.popBackStack().toString())
            fragmentTransaction.commit()
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
                            data.addAll(responseData.sortedWith(compareBy { it.name }))
                            pokedex_list.setHasFixedSize(true)
                            pokedex_list.layoutManager =
                                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                            pokedex_list.adapter =
                                PokedexAdapter(
                                    data, context, onPokedexLineClickListener
                                )
                        }
                    }
                }
            }

        service.listPokemons().enqueue(wsCallback);
    }
}
