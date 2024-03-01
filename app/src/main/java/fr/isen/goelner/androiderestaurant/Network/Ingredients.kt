package fr.isen.goelner.androiderestaurant.Network

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Ingredients(@SerializedName("name_fr")val name:String): Serializable