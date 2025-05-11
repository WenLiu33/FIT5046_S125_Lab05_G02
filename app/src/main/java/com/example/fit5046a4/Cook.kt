package com.example.fit5046a4

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.*
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import com.example.fit5046a4.ui.theme.FIT5046A4Theme
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview



@Composable
fun Cook(modifier: Modifier = Modifier) {
    val mealOptions = listOf("Breakfast", "Lunch", "Dinner")
    var selectMeal by remember { mutableStateOf("Breakfast") }
    val fakeRecipes = listOf("Recipe 1 from API", "Recipe 2 from API", "Recipe 3 from API")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 24.dp, start = 16.dp, end = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Image(
            painter = painterResource(id = R.drawable.ic_chef),
            contentDescription = "chef icon",
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.CenterHorizontally)
        )

        Text(
            text = "Meal Type",
            style = MaterialTheme.typography.titleMedium
        )

        var expanded by remember { mutableStateOf(false) }
        var dropdownWidth by remember { mutableStateOf(0) }
        val density = LocalDensity.current

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .clickable { expanded = true }
                .padding(12.dp)
                .onGloballyPositioned { coordinates ->
                    dropdownWidth = coordinates.size.width
                }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = selectMeal)
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "Dropdown Arrow"
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.width(with(density) { dropdownWidth.toDp() })
            ) {
                mealOptions.forEach { meal ->
                    DropdownMenuItem(
                        text = { Text(meal) },
                        onClick = {
                            selectMeal = meal
                            expanded = false
                        }
                    )
                }
            }
        }

        fakeRecipes.forEach { recipe ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                shape = RoundedCornerShape(8.dp),
                tonalElevation = 2.dp
            ) {
                Text(
                    text = recipe,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Button(
            onClick = { /* TODO */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
        ) {
            Text("Cook")
        }
    }
}



@Preview(showBackground = true)
@Composable
fun CookMealScreenPreview() {
    FIT5046A4Theme {
        Cook()
    }
}


