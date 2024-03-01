package fr.isen.goelner.androiderestaurant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.PrecomputedText.Params
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.view.menu.MenuView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.GsonBuilder
import fr.isen.goelner.androiderestaurant.Network.CategoryPlats
import fr.isen.goelner.androiderestaurant.Network.Dish
import fr.isen.goelner.androiderestaurant.Network.MenuResult
import fr.isen.goelner.androiderestaurant.Network.NetworkConstants
import org.json.JSONObject
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import fr.isen.goelner.androiderestaurant.Basket.BasketActivity
import java.lang.reflect.Method
import java.util.Locale.Category


class MenuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val type = (intent.getSerializableExtra(CATEGORY_EXTRA_KEY) as? DishType) ?: DishType.STARTER

        setContent {
            MenuView(type)
        }
        Log.d("lifeCycle", "menu Activity - OnCreate")
    }


    override fun onPause(){
        Log.d("lifeCycle", "Menu Activity - OnPause")
        super.onPause()
    }
    override fun onResume(){
        super.onResume()
        Log.d("lifeCycle", "Menu Activity - OnResume")
    }
    override fun onDestroy(){
        Log.d("lifeCycle", "Menu Activity - OnDestroy")
        super.onDestroy()
    }
    companion object {
        val CATEGORY_EXTRA_KEY = "CATEGORY_EXTRA_KEY"
    }
}

@Composable
fun MenuView(type: DishType) {
    val context = LocalContext.current
    Surface (
        modifier = Modifier.fillMaxSize(),
        color = colorResource(id = R.color.purple_200),
    ) {
        val category = remember {
            mutableStateOf<CategoryPlats?>(null)
        }
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    // Image à gauche du titre
                    Image(
                        painterResource(R.drawable.op__1_),
                        contentDescription = null,
                        modifier = Modifier.size(100.dp) // Taille de l'image
                    )
                    // Titre avec un texte agrandi
                    Text(
                        type.title(),
                        fontSize = 24.sp, // Taille de la police agrandie
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    // Image à droite du titre
                    Image(
                        painterResource(R.drawable.op__1_),
                        contentDescription = null,
                        modifier = Modifier.size(100.dp) // Taille de l'image
                    )
                }
                LazyColumn {
                    category.value?.let {
                        items(it.item) {
                            dishRow(it)
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 16.dp, top = 16.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        Toast.makeText(context, "Voir Panier", Toast.LENGTH_LONG).show()
                        val intent = Intent(context, BasketActivity::class.java)
                        context.startActivity(intent)
                    }
                ) {
                    Image(
                        painterResource(R.drawable.r),
                        contentDescription = null,
                        modifier = Modifier.width(20.dp)
                    )
                }
                Text(
                    text = BasketState.itemCountInBasket.value.toString(),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .background(colorResource(R.color.teal_200), shape = CircleShape)
                        .padding(4.dp)
                        .align(Alignment.TopEnd)
                )
            }
        }
        postData(type, category)
    }
}



@Composable fun dishRow(dish: Dish){
    val context = LocalContext.current
    Card (border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.DISH_EXTRA_KEY, dish)
                context.startActivity(intent)
            }
    ){
        Surface (
            modifier = Modifier.fillMaxSize(),
            color = colorResource(id = R.color.white),
        ){
            Row(Modifier.padding(8.dp)) {
                // Column for name and image
                Column(Modifier.weight(1f)) {
                    Text(
                        dish.name,
                        Modifier.padding(bottom = 15.dp),
                        fontFamily = FontFamily.Serif
                    )

                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(dish.images.first())
                            .build(),
                        null,
                        placeholder = painterResource(R.drawable.ic_launcher_foreground),
                        error = painterResource(R.drawable.ic_launcher_foreground),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .width(100.dp)
                            .height(100.dp)
                            .clip(RoundedCornerShape(10))
                    )
                }
                // Column for price
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Text(
                        "${dish.prices.first().price} €",
                        Modifier.padding(top = 8.dp)
                    )
                }
            }
        }

    }
}

@Composable
fun postData(type: DishType, category: MutableState<CategoryPlats?>){
    val currentCategory = type.title()
    val context = LocalContext.current
    val queue = Volley.newRequestQueue(context)

    val params = JSONObject()
    params.put(NetworkConstants.ID_SHOP, "1")

    val request = JsonObjectRequest(
        Request.Method.POST,
        NetworkConstants.URL,
        params,
        {
            Log.d("request", it.toString(2))
            val result = GsonBuilder().create().fromJson(it.toString(),MenuResult::class.java)
            val filteredResult = result.data.first { it.name == currentCategory}
            category.value = filteredResult
            Log.d("result","")
        },
        {
            Log.e("request", it.toString())
        }
    )
    queue.add(request)
}
