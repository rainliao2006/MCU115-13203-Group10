package com.example.foodorderingwizard

data class Order(
    val mainMeal: String = "",
    val sideDishes: List<String> = emptyList(),
    val drink: String = "",
) {
    fun isComplete(): Boolean {
        return mainMeal.isNotEmpty() && sideDishes.isNotEmpty() && drink.isNotEmpty()
    }
}
