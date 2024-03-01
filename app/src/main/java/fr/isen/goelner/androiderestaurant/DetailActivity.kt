package fr.isen.goelner.androiderestaurant

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import fr.isen.goelner.androiderestaurant.Basket.Basket
import fr.isen.goelner.androiderestaurant.Basket.BasketActivity
import fr.isen.goelner.androiderestaurant.Network.Dish
import kotlin.math.max

class DetailActivity : ComponentActivity() {
    @SuppressLint("ResourceType")
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dish = intent.getSerializableExtra(DISH_EXTRA_KEY) as? Dish
        setContent {
            val context = LocalContext.current
            val count = remember{
                mutableIntStateOf(1)
            }
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = colorResource(id = R.color.teal_700),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Ingredients", // Corrigé "Ingredients" à "Ingrédients"
                        fontFamily = FontFamily.Cursive,
                        fontSize = 44.sp,
                        modifier = Modifier.padding(bottom = 10.dp) // Augmentation de l'espacement
                    )
                    dish?.ingredients?.forEach {
                        IngredientItem(ingredient = it.name)

                    }
                    Row (
                        modifier = Modifier
                            .padding(5.dp)
                    ){
                        OutlinedButton(onClick =  {
                            count.value = max( 1, count.value -1)
                        })
                        {
                            Text(text = "-")
                        }

                        Text(
                            count.value.toString(),
                            fontSize = 25.sp,
                            modifier = Modifier
                                .padding(horizontal = 56.dp)
                        )

                        OutlinedButton(onClick =  {
                            count.value = count.value + 1
                        })
                        {
                            Text(text = "+")
                        }
                    }
                    Row  {
                        OutlinedButton(onClick = {
                            Toast.makeText(context, "Ajout Panier", Toast.LENGTH_LONG).show()
                            if (dish != null)    {
                                Basket.current(context).add(dish, count.value, context)
                            }
                        }){
                            Text(text = "Ajouter au Panier")
                        }
                        Text(
                            "${(dish?.prices?.first()?.price?.toFloat() ?: 0f) * count.value} €"
                            ,
                            modifier = Modifier
                                .padding(16.dp)
                        )
                        Box  {
                            OutlinedButton(onClick = {
                                Toast.makeText(context, "Voir Panier", Toast.LENGTH_LONG).show()
                                val intent = Intent(context, BasketActivity::class.java)
                                context.startActivity(intent)
                            }){
                                Image(
                                    painterResource(R.drawable.r),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .width(20.dp)

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

                }


                Box(
                    modifier = Modifier
                        .padding(top = 200.dp, start = 20.dp, end = 20.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    HorizontalPager(state = rememberPagerState {
                        dish?.images?.count() ?: 0
                    }) {caroussel ->
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(dish?.images?.get(caroussel))
                                .build(),
                            null,
                            placeholder = painterResource(R.drawable.ic_launcher_foreground),
                            error = painterResource(R.drawable.ic_launcher_foreground),
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .width(800.dp)
                                .clip(RoundedCornerShape(10))

                        )
                    }
                }
            }
        }
    }

    @Composable
    fun IngredientItem(ingredient: String) {
        Text(
            text = ingredient,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }

    companion object {
        val DISH_EXTRA_KEY = "DISH_EXTRA_KEY"
    }
}

