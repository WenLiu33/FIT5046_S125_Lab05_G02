package com.example.fit5046a4


import android.app.Application
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController


//This was the UI implementation for Fridge
//To be deleted after Fridge is properly implemented
//This is just used as a guide for now :)
@Composable
fun Fridge2() {
    val context = LocalContext.current


    val fridgeItems = mapOf(
        "Eggs" to "2", "Milk" to "1", "Tomatoes" to "3"
    ).toList()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        val image: Painter = painterResource(id = R.drawable.refrigerator)
        Image(
            painter = image, contentDescription = "Fridge Image", modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(50.dp))
        Text("Here's what you've got right now:", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(30.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()
        ) {
            items(fridgeItems) { (ingredient, quantity) ->
                Card(
                    shape = RoundedCornerShape(10.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "$ingredient x$quantity", modifier = Modifier.weight((1f))
                        )
                        Text(
                            text = "edit",
                            color = Color(0xFF03A9F4),
                            modifier = Modifier.clickable {
                                Toast.makeText(context, "Edit $ingredient", Toast.LENGTH_SHORT)
                                    .show()
                            })
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(50.dp))
        ElevatedButton(
            onClick = {
                Toast.makeText(context, "Add new ingredient", Toast.LENGTH_SHORT).show()
            }, shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(text = "Add Ingredients")
        }


    }
}

//This is the implementation of FridgeItemList
//This function will display the current list of ingredients as a lazy list
//The list of ingredients will be displayed from the ingredient database
@Composable
//From ingredient database
fun FridgeItemList(viewModel: IngredientViewModel = viewModel()) {
    val ingredients by viewModel.allIngredients.collectAsState(initial = emptyList())
    val context = LocalContext.current

    //If Ingredients isn't empty, it will be displayed in lazy Column
    if (ingredients.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(8.dp),
                tonalElevation = 2.dp,
                color = Color(0xFFD7DEFB).copy(alpha = 0.2f)
            ) {
                Row(
                    modifier = Modifier
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Your fridge is currently empty! \nAdd ingredients below.",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        //color = Color(0xFF888888)
                        color = Color(0xFF495D92)
                    )
                }
            }
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(8.dp)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(ingredients) { ingredient ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(8.dp),
                        tonalElevation = 2.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                //Displays the ingredient, quantity and unit
                                text = "${ingredient.name} x ${ingredient.quantity} ${ingredient.unit}",
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = "edit",
                                color = Color(0xFF03A9F4),
                                modifier = Modifier.clickable {
                                    Toast.makeText(
                                        context,
                                        "Edit ${ingredient.name}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

//This is the Fridge function that lays out the components
//The first component will be the FridgeItemList
//The second component will be the AddIngredients Function
//navController is passed in to allow navigation from inside this screen (e.g., to "add_ingredient")
@Composable
fun Fridge(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        val image: Painter = painterResource(id = R.drawable.refrigerator)
        Image(
            painter = image, contentDescription = "Fridge Image", modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(50.dp))
        Text("Here's what you've got right now:", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(30.dp))

        //displays fridge items from database
        FridgeItemList()

        Spacer(modifier = Modifier.height(30.dp))

        //displays button to add ingredients
        AddIngredientsButton(navController)
    }
}

//This is the function to edit the Ingredients quickly
@Composable
fun EditIngredients(){
    Text("TO DO")
}

//This is the function to Add Ingredients
//Upon clicking this button, user will be navigated to the AddIngredientScreen
@Composable
fun AddIngredientsButton(navController: NavController){
    val context = LocalContext.current
    ElevatedButton(
        onClick = {
            navController.navigate("add_ingredient")
        },
        shape = RoundedCornerShape(12.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add",
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(text="Add Ingredients")
    }
}

