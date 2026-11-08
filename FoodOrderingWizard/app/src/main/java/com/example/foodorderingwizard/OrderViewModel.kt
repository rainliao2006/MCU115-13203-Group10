package com.example.foodorderingwizard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OrderViewModel : ViewModel() {
    private val _order = MutableLiveData(Order())
    val order: LiveData<Order> = _order

    fun setMainMeal(meal: String) {
        val current = _order.value ?: Order()
        _order.value = current.copy(mainMeal = meal)
    }

    fun setDrink(drink: String) {
        val current = _order.value ?: Order()
        _order.value = current.copy(drink = drink)
    }

    fun updateSideDish(dish: String, isChecked: Boolean) {
        val currentOrder = _order.value ?: Order()
        val currentSides = currentOrder.sideDishes.toMutableList()
        if (isChecked) {
            if (!currentSides.contains(dish)) {
                currentSides.add(dish)
            }
        } else {
            currentSides.remove(dish)
        }
        _order.value = currentOrder.copy(sideDishes = currentSides)
    }

    fun resetOrder() {
        _order.value = Order()
    }
}
