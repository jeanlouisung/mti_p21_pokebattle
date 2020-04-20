package com.example.mti_p21_pokebattle.models

data class TypeRelations(
    val no_damage_to: List<Type>,
    val half_damage_to: List<Type>,
    val double_damage_to: List<Type>,
    val no_damage_from: List<Type>,
    val half_damage_from: List<Type>,
    val double_damage_from: List<Type>
)