package com.example.fit5046a4

import android.util.Log
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.*
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.*

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import com.example.fit5046a4.ui.theme.FIT5046A4Theme
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.fit5046a4.api.RecipeViewModel

/**
 * Composable function to display a screen with a dropdown menu to select meal type
 * It shows images, names, and descriptions for each food category, and allows the user to interact
 * with the UI to switch between different meal categories.
 *
 * The categories are fetched from the [RecipeViewModel], which provides the data via LiveData.
 * The UI will display categories dynamically based on the selected meal type.
 *
 * @param viewModel The [RecipeViewModel] that provides the food categories for different meal types.
 * @param modifier Optional modifier to customize the layout.
 *
 * @author Sylvia
 * @version 2.0
 */
@Composable
fun Cook(viewModel: RecipeViewModel = viewModel(), modifier: Modifier = Modifier) {
    // Meal options and selected meal
    val mealOptions = listOf("Breakfast", "Lunch", "Dinner")
    var selectedMeal by remember { mutableStateOf("Breakfast") }

    // Observing LiveData from RecipeViewModel
    val breakfastCategories by viewModel.breakfastCategory.observeAsState(emptyList())
    val lunchCategories by viewModel.lunchCategory.observeAsState(emptyList())
    val dinnerCategories by viewModel.dinnerCategory.observeAsState(emptyList())

    //AI-generated: Trigger ViewModel to fetch categories when the screen is first shown
    LaunchedEffect(Unit) {
        viewModel.fetchCategories()
    }

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
            text = "\uD83C\uDF7D\uFE0F Meal Type",
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
                Text(text = selectedMeal)
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
                            selectedMeal = meal
                            expanded = false
                        }
                    )
                }
            }
        }

        // Display categories based on selected meal type
        val categories = when (selectedMeal) {
            "Breakfast" -> breakfastCategories
            "Lunch" -> lunchCategories
            "Dinner" -> dinnerCategories
            else -> emptyList()
        }

        // A part of AI generate
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories.filterNotNull()) { category ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    tonalElevation = 2.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Image
                        category.strCategoryThumb?.let { imageUrl ->
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = category.strCategory,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        // Name
                        Text(
                            text = category.strCategory ?: "Unknown category",
                            style = MaterialTheme.typography.titleMedium
                        )

                        // Description
                        Text(
                            text = category.strCategoryDescription ?: "",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
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