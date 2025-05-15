package com.example.fit5046a4

import android.graphics.Paint.Align
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fit5046a4.database.Ingredient
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Calendar
import java.util.Date
import java.util.Locale


//*******FUNCTIONS BELOW********


//This function is the field inputs for adding ingredients
//This will be inserted into existing database
//After adding the ingredient or cancelling, it will automatically redirect to Fridge Screen

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddIngredientsToDB(viewModel: IngredientViewModel) {
    val ingredients by viewModel.allIngredients.collectAsState(initial = emptyList())
    var name by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("") }
    var unitPrice by remember { mutableStateOf("") }
    var expiryDateText by remember { mutableStateOf("")}
    //var selectedIngredient by remember { mutableStateOf<Ingredient?>(null) }
    var category by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Ingredient:")
        TextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp)),
            shape = RoundedCornerShape(10.dp),

            // when user clicks on the textfield, it changes colour to indicate click
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFD7DEFB).copy(alpha = 0.6f),
                unfocusedContainerColor = Color(0xFFD7DEFB).copy(alpha = 0.2f)

            )
        )

        //Accepts input from a numerical keyboard only
        //To enable on emulator, go to settings, disable stylus input
        //and enable on screen keyboard
        Text("Quantity & Unit:")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = quantity,
                onValueChange = { quantity = it },
                placeholder = { Text("select qty") },

                //numerical keyboarc
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp)),
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                colors = TextFieldDefaults.colors(

                    // when user clicks on the textfield, it changes colour to indicate click
                    focusedContainerColor = Color(0xFFD7DEFB).copy(alpha = 0.6f),
                    unfocusedContainerColor = Color(0xFFD7DEFB).copy(alpha = 0.2f)

                )

            )

            UnitDropDown( // ** properly nested inside Row with quantity field **
                selectedUnit = unit,
                onUnitSelected = { unit = it },
                modifier = Modifier.weight(1f)
            )
        }
            //Accepts input from a numerical keyboard only
            //To enable on emulator, go to settings, disable stylus input
            //and enable on screen keyboard
            Text("Unit Price:")
            TextField(
                value = unitPrice,
                onValueChange = { unitPrice = it },
                placeholder = { Text("e.g. 2.50") },

                //numerical keyboard
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp)),
                singleLine = true,
                shape = RoundedCornerShape(10.dp),

                // when user clicks on the textfield, it changes colour to indicate click
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFD7DEFB).copy(alpha = 0.6f),
                    unfocusedContainerColor = Color(0xFFD7DEFB).copy(alpha = 0.2f)

                )
            )

            Text("Expiry Date:", modifier = Modifier.padding(top = 12.dp))

            //ExpiryDatePickerField() is called here so a calender is displayed
            //User selects expiry date on the calender date-picker pop-up
            ExpiryDatePickerField(
                expiryDate = expiryDateText,
                onDateSelected = {expiryDateText = it}
            )

            Text("Category:", modifier = Modifier.padding(top = 12.dp))

            CategoryDropDown(
                selectedCategory = category,
                onCategorySelected = { category = it }
            )

        Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                //Checking inputs for clean data entry into the Database//
                //TO DO: More validation checks
                //TO DO: Cancel if user decides to not add anything
                //TO DO: After cancellation or add ingredient, user is directed to fridge homescreen OR?
                //Pop up asking if user wants to add more ingredients or not (if we have time)
                ElevatedButton(
                    onClick = {
                        if (name.isNotBlank() && quantity.isNotBlank() && unit.isNotBlank() && unitPrice.isNotBlank()) {
                            val ingredient = Ingredient(
                                name = name.trim().replaceFirstChar { it.uppercaseChar() },
                                quantity = quantity.toIntOrNull() ?: 0,
                                unit = unit.trim(),
                                unitPrice = unitPrice.toFloatOrNull() ?: 0f,

                                insertDate = Date(),
                                expiryDate = Date(), //CHANGE THIS AFTER DATE PICKER FUNCTION IS IMPLEMENTED
                                category = category.trim(), //TO FIX
                            )
                            viewModel.insertIngredient(ingredient)
                            name = ""; quantity = ""; unit = ""; unitPrice = ""
                            Toast.makeText(context, "Ingredient added!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT)
                                .show()
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

//This function is the Screen when user clicks 'Add Ingredients'
//in the Fridge Screen
//TO DO: NAV ROUTE IN FRIDGE SCREEN
@RequiresApi(Build.VERSION_CODES.O)
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
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
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

//This function will have unit as a drop down menu
//This will be called into the AddIngredientsToDB() function

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitDropDown(
    selectedUnit: String,
    onUnitSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val unitOptions = listOf("g", "kg", "ml", "L", "pc(s)", "cup(s)")
    var isExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = it },
        modifier = modifier
            .padding(bottom = 8.dp)
    ) {
        TextField(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .focusProperties { canFocus = false }
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFD7DEFB).copy(alpha = 0.2f)),
            readOnly = true,
            value = selectedUnit,
            onValueChange = {},
            label = { Text("select unit") },

            ////When user clicks on the textfield, it changes colour to indicate click
            //TO DO: can't get this working for some reason
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFD7DEFB).copy(alpha = 0.6f),
                unfocusedContainerColor = Color(0xFFD7DEFB).copy(alpha = 0.2f)

            ),

            //manages the arrow icon up and down
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            singleLine = true,
            shape = RoundedCornerShape(10.dp)

        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            unitOptions.forEach { selectedOption ->
                DropdownMenuItem(
                    text = { Text(selectedOption) },
                    onClick = {
                        onUnitSelected(selectedOption)
                        isExpanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

//This function will be the date picker for expiry date
//This will be called into the AddIngredientsToDB() function
//Week3 lab content
//parameter logic similar to UnitDropDown function

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpiryDatePickerField(
    expiryDate:String,
    onDateSelected: (String) -> Unit
){
    val calendar = Calendar.getInstance()
    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Instant.now().toEpochMilli()
    )

    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableLongStateOf(calendar.timeInMillis) }

    TextField(
        value = expiryDate,
        onValueChange = {},
        readOnly = true,
        label = { Text("select date")},
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable { showDatePicker= true },
        shape = RoundedCornerShape(10.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFD7DEFB).copy(alpha = 0.6f),
            unfocusedContainerColor = Color(0xFFD7DEFB).copy(alpha = 0.2f)
        ),
        trailingIcon = {
            Icon(imageVector = Icons.Default.DateRange,
                contentDescription = "Select Date",
                modifier = Modifier
                    .clickable { showDatePicker= true }
                    .size(24.dp)
            )
        }
    )

    if (showDatePicker){
        DatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
            },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    selectedDate = datePickerState.selectedDateMillis?:calendar.timeInMillis
                    onDateSelected("Expiry: ${formatter.format(Date(selectedDate))}")
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDatePicker = false
                }) {
                    Text("Cancel")
                }
            }
        ) //end of dialogue
        {
            DatePicker(state = datePickerState)
        }
    }
}

//This function will have category as drop down menu
//This will be called into the AddIngredientsToDB() function
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropDown(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier

){
    val categoryOptions = listOf("Fruit", "Vegetables", "Proteins", "Dairy", "Grains", "Condiments", "Frozen goods")
    var isExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = it },
        modifier = modifier
            .padding(bottom = 8.dp)
    ) {
        TextField(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .focusProperties { canFocus = false }
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFD7DEFB).copy(alpha = 0.2f)),
            readOnly = true,
            value = selectedCategory,
            onValueChange = {},
            label = { Text("select category") },

            ////When user clicks on the textfield, it changes colour to indicate click
            //TO DO: can't get this working for some reason
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFD7DEFB).copy(alpha = 0.6f),
                unfocusedContainerColor = Color(0xFFD7DEFB).copy(alpha = 0.2f)

            ),
            //manages the arrow icon up and down
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            singleLine = true,
            shape = RoundedCornerShape(10.dp)

        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            categoryOptions.forEach { selectedOption ->
                DropdownMenuItem(
                    text = { Text(selectedOption) },
                    onClick = {
                        onCategorySelected(selectedOption)
                        isExpanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}
//@Preview(showBackground = true)
//@Composable
//fun AddIngredientsPreview() {
//    AddIngredientScreen()
//}
//


