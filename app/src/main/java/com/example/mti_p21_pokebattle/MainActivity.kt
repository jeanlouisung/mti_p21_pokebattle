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
