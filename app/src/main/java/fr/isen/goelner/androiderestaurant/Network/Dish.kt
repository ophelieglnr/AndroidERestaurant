package fr.isen.goelner.androiderestaurant.Network

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Dish (
    @SerializedName("name_fr") val name: String,
    @SerializedName( "images") val images: List<String>,
    @SerializedName("ingredients") val ingredients: List<Ingredients>,
    @SerializedName("prices") val prices: List<Price>
): Serializable