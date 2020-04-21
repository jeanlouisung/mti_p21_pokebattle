package com.example.mti_p21_pokebattle.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mti_p21_pokebattle.R
import kotlinx.android.synthetic.main.fragment_home.view.*

/**
 * A simple [Fragment] subclass.
 * Use the [Home.newInstance] factory method to
 * create an instance of this fragment.
 */
class Home : Fragment() {
    // TODO: Rename and change types of parameters
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceS

        view.home_description_text.text =
            "Welcome Trainer! You have a team of three pokemons and you will fight 3 wild pokemons in a row. But before the battle starts, you only know your first opponent. And remember, most Pokemons have strenghts and weaknesses.\nGood Luck!"

        val onBattleButtonClickListener = View.OnClickListener {
            val fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()

            val battleLobbyFragment = BattleLobby()

            fragmentTransaction.replace(R.id.main_container, battleLobbyFragment, "BattleLobby")
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        val onPokedexButtonClickListener = View.OnClickListener {
            //            val position = it.tag as Int
            val fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()

            val pokedexFragment = Pokedex()

            fragmentTransaction.replace(R.id.main_container, pokedexFragment, "Pokedex")
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        view.home_pokedex_button.setOnClickListener(onPokedexButtonClickListener)
        view.home_battle_button.setOnClickListener(onBattleButtonClickListener)
    }
}
