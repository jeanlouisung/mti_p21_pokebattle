package com.example.mti_p21_pokebattle.models

data class Move(
    val name: String,
    val accuracy: Int,
    val power: Int,
    val type: Type
)