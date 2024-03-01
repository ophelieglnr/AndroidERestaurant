package fr.isen.goelner.androiderestaurant

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.goelner.androiderestaurant.Basket.BasketActivity
import fr.isen.goelner.androiderestaurant.ui.theme.AndroidERestaurantTheme

enum class DishType{
    STARTER, MAIN, DESSERT;

    @Composable
    fun title(): String{
        return when(this) {
            STARTER -> stringResource(id = R.string.menu_starter)
            MAIN -> stringResource(id = R.string.menu_main)
            DESSERT -> stringResource(id = R.string.menu_dessert)
        }
    }
}

interface MenuInterface {
    fun dishPressed(dishType: DishType)
}

class HomeActivity : ComponentActivity(), MenuInterface {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidERestaurantTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    SetupView(this, Modifier)
                }
            }
        }

        Log.d("lifeCycle", "Home Activity - OnCreate")
    }

    override fun dishPressed(dishType: DishType) {
        val intent = Intent(this, MenuActivity::class.java)
        intent.putExtra(MenuActivity.CATEGORY_EXTRA_KEY, dishType)
        startActivity(intent)
    }

    override fun onPause(){
        Log.d("lifeCycle", "Home Activity - OnPause")
        super.onPause()
    }
    override fun onResume(){
        super.onResume()
        Log.d("lifeCycle", "Home Activity - OnResume")
    }
    override fun onDestroy(){
        Log.d("lifeCycle", "Home Activity - OnDestroy")
        super.onDestroy()
    }
}


@Composable
fun SetupView(menu:MenuInterface, modifier: Modifier) {
    val context = LocalContext.current
    Surface (
        modifier = Modifier.fillMaxSize(),
        color = colorResource(id = R.color.white),
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = " Nycticebus pygmaeus ",
                    fontFamily = FontFamily.Cursive,
                    fontSize = 44.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Image(
                    painterResource(R.drawable.oip),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .size(200.dp)
                )
                CustomButton(type = DishType.STARTER, menu)
                Divider()
                CustomButton(type = DishType.MAIN, menu)
                Divider()
                CustomButton(type = DishType.DESSERT, menu)
                Divider()
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
    }
}


@Composable
fun CustomButton(type: DishType, menu: MenuInterface) {
    TextButton(
        onClick = { menu.dishPressed(type)},
        modifier = Modifier.padding(vertical = 15.dp) // Ajout de l'espace vertical
    ) {
        Text(
            text = type.title(),
            fontSize = 30.sp // Augmentation de la taille du texte
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidERestaurantTheme {
        SetupView(HomeActivity(), Modifier)
    }
}
