package com.example.fit5046a4

import android.graphics.Paint.Align
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
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
fun AddIngredientsToDB(viewModel: IngredientViewModel, navController: NavController) {

    //rememberSaveable so even if user accidentally taps into another screen the inputs are still there
    var name by rememberSaveable { mutableStateOf("") }
    var quantity by rememberSaveable { mutableStateOf("") }
    var unit by rememberSaveable { mutableStateOf("") }
    var unitPrice by rememberSaveable { mutableStateOf("") }
    var expiryDateText by rememberSaveable { mutableStateOf("") }
    var expiryDate by rememberSaveable { mutableStateOf(Date()) }
    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    var category by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            // When user taps away from the field, they keyboard disappears unless relevent field is tapped
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("Item:", fontSize = 16.sp, fontWeight = FontWeight.Medium)
        TextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp)),
            shape = RoundedCornerShape(10.dp),

            // When user clicks on the textfield, it changes colour to indicate click
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFD7DEFB).copy(alpha = 0.6f),
                unfocusedContainerColor = Color(0xFFD7DEFB).copy(alpha = 0.2f)

            )
        )

        //Accepts input from a numerical keyboard only
        //To enable on emulator, go to settings, disable stylus input
        //and enable on screen keyboard
        Text("Quantity & Unit:", fontSize = 15.sp, fontWeight = FontWeight.Medium)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = quantity,
                onValueChange = { quantity = it },
                placeholder = { Text("select qty", fontSize = 14.sp) },

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

            UnitDropDown(
                selectedUnit = unit,
                onUnitSelected = { unit = it },
                modifier = Modifier.weight(1f)
            )
        }
        //Accepts input from a numerical keyboard only
        //To enable on emulator, go to settings, disable stylus input
        //and enable on screen keyboard
        Text("Item Price & Category:", fontSize = 15.sp, fontWeight = FontWeight.Medium)
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = unitPrice,
                onValueChange = { unitPrice = it },
                placeholder = { Text("e.g. 2.50", fontSize = 14.sp) },

                //numerical keyboard
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp)),
                singleLine = true,
                shape = RoundedCornerShape(10.dp),

                // when user clicks on the textfield, it changes colour to indicate click
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFD7DEFB).copy(alpha = 0.6f),
                    unfocusedContainerColor = Color(0xFFD7DEFB).copy(alpha = 0.2f)

                )
            )

            CategoryDropDown(
                selectedCategory = category,
                onCategorySelected = { category = it },
                modifier = Modifier.weight((1f))
            )

        }

        Text(
            "Expiry Date:",
            modifier = Modifier.padding(top = 4.dp),
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
        )

        //ExpiryDatePickerField() is called here so a calender is displayed
        //User selects expiry date on the calender date-picker pop-up
        ExpiryDatePickerField(
            expiryDate = expiryDateText,
            onDateSelected = {
                expiryDateText = it
                expiryDate = formatter.parse(it.removePrefix("Expiry: ")) ?: Date()

            }
        )

        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            //Checking inputs for clean data entry into the Database//
            //Each statement returns to @ElevatedButton upon failing to prevent crash
            ElevatedButton(
                onClick = {
                    // Check for blank fields before submission
                    if (name.isBlank() || quantity.isBlank() || unitPrice.isBlank() || expiryDateText.isBlank() || category.isBlank()) {
                        Toast.makeText(context, "Plese fill all fields", Toast.LENGTH_SHORT).show()
                        return@ElevatedButton
                    }

                    // Ensure quantity is a valid Int and > 0
                    if (quantity.toIntOrNull() == null || quantity.toInt() <= 0) {
                        Toast.makeText(
                            context,
                            "Please enter a valid quantity > 0.",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@ElevatedButton
                    }

                    //Multiple checks for unit price to allow free items
                    val price = unitPrice.toFloatOrNull()
                    if (price == null) {
                        Toast.makeText(context, "Please enter a valid price", Toast.LENGTH_SHORT).show()
                        return@ElevatedButton
                    }
                    if (price < 0f) {
                        Toast.makeText(context, "Price cannot be negative", Toast.LENGTH_SHORT).show()
                        return@ElevatedButton
                    }
                    //if item is $0, it will be marked as free
                    // This is special scenario where e.g. user may have potentially received free food from neighbours
                    if (price == 0f) {
                        Toast.makeText(context, "Note: item marked as free", Toast.LENGTH_SHORT).show()
                    }

                    //Checks expiry date, show a toast warning if expiry date is before today
                    if (expiryDate.before(Date())) {
                        Toast.makeText(
                            context,
                            "Note: this item is already expired",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    val ingredient = Ingredient(
                        name = name.trim().replaceFirstChar { it.uppercaseChar() },
                        quantity = quantity.toInt(),
                        unit = unit,
                        unitPrice = unitPrice.toFloat() / quantity.toInt(),
                        insertDate = Date(),
                        expiryDate = expiryDate,
                        category = category
                    )
                    viewModel.insertIngredient(ingredient)
                    name = ""; quantity = ""; unit = ""; unitPrice = ""; expiryDateText =
                    ""; category = ""
                    Toast.makeText(context, "Item added!", Toast.LENGTH_SHORT).show()
                },
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(text = "Add Item")
            }

            Spacer(modifier = Modifier.width(16.dp))

            //This is button for user when they want to cancel adding ingredients
            //When this is clicked, all fields will be cleared and user will be redirected back to fridge screen
            ElevatedButton(
                onClick = {
                    name = ""; quantity = ""; unit = ""; unitPrice = ""; expiryDateText =
                    ""; category = ""
                    //navigates user back to fridge screen upon cancellation
                    navController.navigate("fridge") {
                        popUpTo("add_ingredient") {
                            inclusive = true
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp),
            ) {
                Text("Cancel")
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
    }

}

//This function is the Screen when user clicks 'Add Ingredients'
// It is navigated to from the Fridge screen when the user clicks "Add Ingredient".
// The navController parameter is passed in to allow navigation actions.
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddIngredientScreen(navController: NavController) {
    //TEMPORARY COMMENT OUT: currently using BottomNavigationBarAndTopBar scaffold
    //Layout of the screen with grocery image
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth(),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(text = "Add Ingredients")
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = Color(0xFFD7DEFB),
//                    titleContentColor = MaterialTheme.colorScheme.primary,
//                )
//            )
//        }
//    ) { innerPadding ->
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            //.padding(innerPadding)
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
        item {
            val image: Painter = painterResource(R.drawable.grocery)
            Image(
                painter = image,
                contentDescription = "Grocery Image",
                modifier = Modifier.size(150.dp)
            )
        }
        item {
            AddIngredientsToDB(viewModel = viewModel(), navController = navController)
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
                .clip(RoundedCornerShape(10.dp)),
            readOnly = true,
            value = selectedUnit,
            onValueChange = {},
            label = { Text("select unit", fontSize = 14.sp) },

            ////When user clicks on the textfield, it changes colour to indicate click
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
//Parameter logic similar to @UnitDropDown function
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpiryDatePickerField(
    expiryDate: String,
    onDateSelected: (String) -> Unit
) {
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
        label = { Text("select date", fontSize = 14.sp) },
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable { showDatePicker = true },
        shape = RoundedCornerShape(10.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFD7DEFB).copy(alpha = 0.6f),
            unfocusedContainerColor = Color(0xFFD7DEFB).copy(alpha = 0.2f)
        ),
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Select Date",
                modifier = Modifier
                    .clickable { showDatePicker = true }
                    .size(24.dp)
            )
        }
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
            },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    selectedDate = datePickerState.selectedDateMillis ?: calendar.timeInMillis
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
        )
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

) {
    val categoryOptions =
        listOf("Fruit", "Vegetables", "Proteins", "Dairy", "Grains", "Condiments", "Frozen goods")
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
                .clip(RoundedCornerShape(10.dp)),
            readOnly = true,
            value = selectedCategory,
            onValueChange = {},
            label = { Text("select category", fontSize = 14.sp) },

            ////When user clicks on the textfield, it changes colour to indicate click
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFD7DEFB).copy(alpha = 0.6f),
                unfocusedContainerColor = Color(0xFFD7DEFB).copy(alpha = 0.2f)

            ),
            //Manages the arrow icon up and down
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
