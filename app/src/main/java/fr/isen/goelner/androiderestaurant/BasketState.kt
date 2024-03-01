package fr.isen.goelner.androiderestaurant

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import fr.isen.goelner.androiderestaurant.Basket.Basket



object BasketState {
    val itemCountInBasket = mutableStateOf(0)

    fun updateItemCountInBasket(context: Context) {
        itemCountInBasket.value = Basket.current(context).items.sumOf { it.count }

    }

}