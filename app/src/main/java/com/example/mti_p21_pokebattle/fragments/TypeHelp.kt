package com.example.mti_p21_pokebattle.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mti_p21_pokebattle.R
import com.example.mti_p21_pokebattle.models.Type
import kotlinx.android.synthetic.main.fragment_type_help.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.mti_p21_pokebattle.getType
import com.example.mti_p21_pokebattle.interfaces.PokeapiWebService
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class TypeHelp : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_type_help, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    fun updateNoDamageTo(types: List<Type>) {
        if (types.isNotEmpty()) {
            view!!.no_to1.setImageResource(getType(types[0].name))
        }
    }

    fun updateHalfDamageTo(types: List<Type>) {
        if (types.isNotEmpty()) {
            view!!.half_to1.setImageResource(getType(types[0].name))
        }
        if (types.size > 1) {
            view!!.half_to2.setImageResource(getType(types[1].name))
        }
        if (types.size > 2) {
            view!!.half_to3.setImageResource(getType(types[2].name))
        }
        if (types.size > 3) {
            view!!.half_to4.setImageResource(getType(types[3].name))
        }
        if (types.size > 4) {
            view!!.half_to5.setImageResource(getType(types[4].name))
        }
        if (types.size > 5) {
            view!!.half_to6.setImageResource(getType(types[5].name))
        }
        if (types.size > 6) {
            view!!.half_to7.setImageResource(getType(types[6].name))
        }
    }

    fun updateDoubleDamageTo(types: List<Type>) {
        if (types.isNotEmpty()) {
            view!!.double_to1.setImageResource(getType(types[0].name))
        }
        if (types.size > 1) {
            view!!.double_to2.setImageResource(getType(types[1].name))
        }
        if (types.size > 2) {
            view!!.double_to3.setImageResource(getType(types[2].name))
        }
        if (types.size > 3) {
            view!!.double_to4.setImageResource(getType(types[3].name))
        }
        if (types.size > 4) {
            view!!.double_to5.setImageResource(getType(types[4].name))
        }
    }

    fun updateNoDamageFrom(types: List<Type>) {
        if (types.isNotEmpty()) {
            view!!.no_from1.setImageResource(getType(types[0].name))
        }
        if (types.size > 1) {
            view!!.no_from2.setImageResource(getType(types[1].name))
        }
    }

    fun updateHalfDamageFrom(types: List<Type>) {
        if (types.isNotEmpty()) {
            view!!.half_from1.setImageResource(getType(types[0].name))
        }
        if (types.size > 1) {
            view!!.half_from2.setImageResource(getType(types[1].name))
        }
        if (types.size > 2) {
            view!!.half_from3.setImageResource(getType(types[2].name))
        }
        if (types.size > 3) {
            view!!.half_from4.setImageResource(getType(types[3].name))
        }
        if (types.size > 4) {
            view!!.half_from5.setImageResource(getType(types[4].name))
        }
        if (types.size > 5) {
            view!!.half_from6.setImageResource(getType(types[5].name))
        }
        if (types.size > 6) {
            view!!.half_from7.setImageResource(getType(types[6].name))
        }
        if (types.size > 7) {
            view!!.half_from8.setImageResource(getType(types[7].name))
        }
        if (types.size > 8) {
            view!!.half_from9.setImageResource(getType(types[8].name))
        }
        if (types.size > 9) {
            view!!.half_from10.setImageResource(getType(types[9].name))
        }
    }

    fun updateDoubleDamageFrom(types: List<Type>) {
        if (types.isNotEmpty()) {
            view!!.double_from1.setImageResource(getType(types[0].name))
        }
        if (types.size > 1) {
            view!!.double_from2.setImageResource(getType(types[1].name))
        }
        if (types.size > 2) {
            view!!.double_from3.setImageResource(getType(types[2].name))
        }
        if (types.size > 3) {
            view!!.double_from4.setImageResource(getType(types[3].name))
        }
        if (types.size > 4) {
            view!!.double_from5.setImageResource(getType(types[4].name))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val baseUrl = "https://pokeapi.co/api/v2/"

        val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(jsonConverter)
            .build()

        val service: PokeapiWebService = retrofit.create(
            PokeapiWebService::class.java
        )

        val wsCallback: Callback<Type> =
            object : Callback<Type> {
                override fun onFailure(call: Call<Type>, t: Throwable) {
                    // Code here what happens if calling the WebService fails
                    Log.w("TypeHelp", "WebService call failed")
                    Log.w("Error", t)
                }

                override fun onResponse(
                    call: Call<Type>,
                    response: Response<Type>
                ) {
                    Log.d("TAG", "WebService call TypeHelp " + response.code())
                    if (response.code() == 200) {
                        // We got our data !
                        val responseData = response.body()
                        if (responseData != null) {
                            view.current_type_help.setImageResource(
                                getType(
                                    responseData.name
                                )
                            )
                            val relations = responseData.damage_relations
                            updateDoubleDamageFrom(relations.double_damage_from)
                            updateDoubleDamageTo(relations.double_damage_to)
                            updateHalfDamageFrom(relations.half_damage_from)
                            updateHalfDamageTo(relations.half_damage_to)
                            updateNoDamageFrom(relations.no_damage_from)
                            updateNoDamageTo(relations.no_damage_to)
                        }
                    }
                }
            }
        service.getTypeDetail(arguments!!.getString("typeName")!!).enqueue(wsCallback)
    }
}
