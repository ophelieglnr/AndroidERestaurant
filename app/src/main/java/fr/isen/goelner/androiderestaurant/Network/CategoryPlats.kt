package fr.isen.goelner.androiderestaurant.Network

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CategoryPlats(
    @SerializedName("name_fr")val name: String,
    @SerializedName("items")val item: List<Dish>
): Serializable