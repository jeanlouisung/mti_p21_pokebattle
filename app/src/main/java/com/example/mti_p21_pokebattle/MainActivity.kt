package com.example.mti_p21_pokebattle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mti_p21_pokebattle.fragments.Pokedex

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentTransaction = supportFragmentManager.beginTransaction()

        val pokedexFragment = Pokedex()

        fragmentTransaction.replace(R.id.main_container, pokedexFragment)
        fragmentTransaction.commit()
    }
}

fun getType(type: String): Int {
    if (type == "steel")
        return R.drawable.acier
    if (type == "fighting")
        return R.drawable.combat
    if (type == "dragon")
        return R.drawable.dragon
    if (type == "water")
        return R.drawable.eau
    if (type == "electric")
        return R.drawable.electrique
    if (type == "fairy")
        return R.drawable.fee
    if (type == "fire")
        return R.drawable.feu
    if (type == "ice")
        return R.drawable.glace
    if (type == "insect")
        return R.drawable.insecte
    if (type == "normal")
        return R.drawable.normal
    if (type == "grass")
        return R.drawable.plante
    if (type == "poison")
        return R.drawable.poison
    if (type == "psychic")
        return R.drawable.psy
    if (type == "rock")
        return R.drawable.roche
    if (type == "ghost")
        return R.drawable.spectre
    if (type == "dark")
        return R.drawable.tenebre
    if (type == "ground")
        return R.drawable.terre
    return R.drawable.vol
}
