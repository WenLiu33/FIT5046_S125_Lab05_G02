package com.example.fit5046a4

import android.graphics.Paint.Align
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fit5046a4.database.Ingredient
import java.util.Date


@OptIn(ExperimentalMaterial3Api::class)

//UI SCREEN ONLY
//Used to guide placement
@Composable
fun AddIngredientScreenUI() {
    val context = LocalContext.current


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(), // full width so we can center
                        contentAlignment = Alignment.Center // center its content
                    ) {
                        Text(text = "Add Ingredients")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFD7DEFB),
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),

            ) {

            Spacer(modifier = Modifier.height(20.dp))

            val image: Painter = painterResource(R.drawable.grocery)
            Image(
                painter = image,
                contentDescription = "Fridge Image",
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text("Ingredient:")
            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFD0E8FF).copy(alpha = 0.1f)),
                shape = RoundedCornerShape(10.dp)
            )

            Text("Quantity & Unit")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("Qty") },
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFD0E8FF).copy(alpha = 0.1f)),
                    singleLine = true,


                    shape = RoundedCornerShape(10.dp),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Dropdown Arrow"
                        )
                    }
                )

                OutlinedTextField(
                    value = "Select Unit",
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("Unit") },
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFD0E8FF).copy(alpha = 0.1f)),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Dropdown Arrow"
                        )
                    }
                )
            }

            Text("Unit Price")
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("e.g. 2.50") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFD0E8FF).copy(alpha = 0.1f)),
                singleLine = true,
                shape = RoundedCornerShape(10.dp)
            )

            Text("Category:")
            OutlinedTextField(
                value = "Select Category",
                onValueChange = {}, readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFD0E8FF).copy(alpha = 0.1f)),
                shape = RoundedCornerShape(10.dp),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown Arrow"
                    )
                }
            )

            Text("Expiry Date:")
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("DD/MM/YYYY") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFD0E8FF).copy(alpha = 0.1f)),
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                ElevatedButton(
                    onClick = {
                        Toast.makeText(context, "Save Ingredient", Toast.LENGTH_SHORT).show()
                    },
                    shape = RoundedCornerShape(12.dp)
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
    }
}

//This is the screen for adding ingredients
//This will be inserted into existing database
@Composable
fun AddIngredientsToDB(viewModel: IngredientViewModel) {
    val ingredients by viewModel.allIngredients.collectAsState(initial = emptyList())
    var name by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("") }
    var unitPrice by remember { mutableStateOf("") }
    //var selectedIngredient by remember { mutableStateOf<Ingredient?>(null) }
    val context = LocalContext.current

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Ingredient:")
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFD0E8FF).copy(alpha = 0.1f)),
            shape = RoundedCornerShape(10.dp)
        )

        Text("Quantity & Unit")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            //TO DO: Make drop-down
            OutlinedTextField(
                value = quantity,
                onValueChange = { quantity = it },
                placeholder = { Text("Qty") },
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFD0E8FF).copy(alpha = 0.1f)),
                singleLine = true,
                shape = RoundedCornerShape(10.dp)
            )

            OutlinedTextField(
                value = unit,
                onValueChange = { unit = it },
                placeholder = { Text("Unit") },
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFD0E8FF).copy(alpha = 0.1f)),
                singleLine = true,
                shape = RoundedCornerShape(10.dp)
            )
        }
        //TO DO: Make it so user can only enter numbers
        //numerical keyboard to show up?
        Text("Unit Price")
        OutlinedTextField(
            value = unitPrice,
            onValueChange = { unitPrice = it },
            placeholder = { Text("e.g. 2.50") },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFD0E8FF).copy(alpha = 0.1f)),
            singleLine = true,
            shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            //Checking inputs for clean data entry into the Database//
            //TO DO: More validation checks
            ElevatedButton(
                onClick = {
                    if (name.isNotBlank() && quantity.isNotBlank() && unit.isNotBlank() && unitPrice.isNotBlank()) {
                        val ingredient = Ingredient(
                            name = name.trim().replaceFirstChar { it.uppercaseChar() },
                            quantity = quantity.toIntOrNull() ?: 0,
                            unit = unit.trim(),
                            unitPrice = unitPrice.toFloatOrNull() ?: 0f,
                            insertDate = Date(),
                            expiryDate = Date()
                        )
                        viewModel.insertIngredient(ingredient)
                        name = ""; quantity = ""; unit = ""; unitPrice = ""
                        Toast.makeText(context, "Ingredient added!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    }
                },
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(text = "Add Ingredient")
            }
        }
    }

}

//This is the Screen with AddIngredientsToDB to call when user clicks 'Add Ingredients'
//in the Fridge Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddIngredientScreen() {
    //Layout of the screen with grocery image
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(), // full width so we can center
                        contentAlignment = Alignment.Center // center its content
                    ) {
                        Text(text = "Add Ingredients")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFD7DEFB),
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),

            ) {

            Spacer(modifier = Modifier.height(20.dp))

            val image: Painter = painterResource(R.drawable.grocery)
            Image(
                painter = image,
                contentDescription = "Grocery Image",
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.CenterHorizontally)
            )
            AddIngredientsToDB(viewModel = viewModel())
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun AddIngredientsPreview() {
//    AddIngredientScreen()
//}

