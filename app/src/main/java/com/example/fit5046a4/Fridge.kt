package com.example.fit5046a4

// Core Android and Jetpack Compose imports
import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.fit5046a4.database.Ingredient


//***FUNCTIONS BELOW****

/**
 * Displays the list of ingredients in the fridge via Lazy Column.
 * Shows an empty message when no ingredients are stored.
 * Allows editing and removal of items through a popup dialog.
 */
@Composable
//IngredientViewModel parameter to collect ingredients as list
fun FridgeItemList(viewModel: IngredientViewModel = viewModel()) {
    val ingredients by viewModel.allIngredients.collectAsState(initial = emptyList())
    val context = LocalContext.current

    // States for dialogue and selected ingredient for edit
    var selectedIngredient by remember { mutableStateOf<Ingredient?>(null) }
    var isEditDialogVisible by remember { mutableStateOf(false) }

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
                        text = "Your fridge is currently empty! \uD83D\uDE32 \nAdd items below.",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        //color = Color(0xFF888888)
                        color = Color(0xFF495D92)
                    )
                }
            }
        }
    }

    //Ingredients/items displayed from database
    else {
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
                                //Displays the ingredient/item, quantity and unit
                                text = "${ingredient.name}   x   ${ingredient.quantity} ${ingredient.unit}",
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = "edit",
                                color = Color(0xFF3949AB),
                                modifier = Modifier.clickable {

                                    // 'Edit' text to open dialogue for editing item, dialogue displayed upon click
                                    selectedIngredient = ingredient
                                    isEditDialogVisible = true
                                }
                            )
                        }
                    }
                }
            }
        }
    }
    // When edit is triggered, display the edit dialogue
    if (isEditDialogVisible && selectedIngredient != null) {
        EditIngredientDialog(
            ingredient = selectedIngredient!!,
            onDismiss = {
                isEditDialogVisible = false
                selectedIngredient = null
            },
            onSave = {
                viewModel.updateIngredient(it)
                isEditDialogVisible = false
                selectedIngredient = null
                Toast.makeText(context, "Item updated", Toast.LENGTH_SHORT).show()
            },
            onRemove = {
                viewModel.markIngredientAsDeleted(it.id)
                isEditDialogVisible = false
                selectedIngredient = null
                Toast.makeText(context, "Item removed", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

/**
 * Composable representing the Fridge screen layout including image, ingredient list, and add button.
 * The first cimponent will be the FrigeItemList()
 * The second component will be the AddIngredients()
 * @param navController is passed in to allow navigation from inside this screen
 */
@Composable
fun Fridge(navController: NavController) {
    val viewModel: IngredientViewModel = viewModel()
    val ingredients by viewModel.allIngredients.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        //Fridge image
        val image: Painter = painterResource(id = R.drawable.refrigerator)
        Image(
            painter = image, contentDescription = "Fridge Image", modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(50.dp))
        Text("\uD83D\uDCE6 Here's what you've got right now:", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(30.dp))

        //Displays fridge items from database
        FridgeItemList()

        Spacer(modifier = Modifier.height(30.dp))

        //Displays button to add ingredients
        //Upon clicking user will be taken to Add Ingredient screen
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AddIngredientsButton(navController = navController)

            // Clear Fridge button, this only shows when fridge is not empty
            if (ingredients.isNotEmpty()) {
                ClearFridgeButton(viewModel)
            }
        }
    }
}

/**
This is a composable function for editing a fridge item (previously referred to as ingredient, it is however,
still referred to ingredient in the database

This composable contains a dialogue that allows users to quickly:
- Update quantity and unit of selected item
- Remove the item
- Cancel edit function
@param ingredient - The item to be edited
@param onDismiss called when the user cancels or dismisses the dialogue
@param onSave when the user confirms the edit to pass the updated item
@param onRemove called when the user removes the item
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditIngredientDialog(
    ingredient: Ingredient,
    onDismiss: () -> Unit,
    onSave: (Ingredient) -> Unit,
    onRemove: (Ingredient) -> Unit
) {

    // This sets up the quantity input field with the item's current quantity (converted to a string).
    // so when the dialog opens, the TextField will show the corresponding item value from the database.
    var quantity by remember { mutableStateOf(ingredient.quantity.toString()) }

    // Selected unit for item
    var selectedUnit by remember { mutableStateOf(ingredient.unit) }
    var isExpanded by remember { mutableStateOf(false) }
    val unitOptions = listOf("g", "kg", "ml", "L", "pc(s)", "cup(s)")

    //Main layout of dialog
    AlertDialog(
        onDismissRequest = onDismiss, //called when user taps away from dialog
        containerColor = Color(0xFFFCFCFD),
        title = {
            Text(

                //takes name of ingredient instead of just 'edit item' so user knows what they are editing
                text = "Update or Remove ${ingredient.name}",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        },

        //main message in the centre of the dialog
        text = {

            //content of the dialog in fields
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(top = 0.dp)
            ) {
                Text("Quantity:", fontSize = 14.sp, fontWeight = FontWeight.Medium)

                TextField(
                    value = quantity,
                    //update state as user inputs quantity
                    onValueChange = { quantity = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp)),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFD7DEFB).copy(alpha = 0.8f),
                        unfocusedContainerColor = Color(0xFFD7DEFB).copy(alpha = 0.4f)
                    )
                )

                Text("Unit:", fontSize = 14.sp, fontWeight = FontWeight.Medium)

                //Drop down for unit
                //similar logic to see @UnitDropDown()
                ExposedDropdownMenuBox(
                    expanded = isExpanded,
                    onExpandedChange = { isExpanded = !isExpanded }
                ) {
                    TextField(
                        readOnly = true,
                        value = selectedUnit,
                        onValueChange = {},
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp)),
                        shape = RoundedCornerShape(10.dp),
                        label = { Text("Select unit", fontSize = 14.sp) },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                        },
                        //When user clicks on the textfield, it changes colour to indicate click
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFD7DEFB).copy(alpha = 0.8f),
                            unfocusedContainerColor = Color(0xFFD7DEFB).copy(alpha = 0.4f)
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = isExpanded,
                        onDismissRequest = { isExpanded = false }
                    ) {
                        unitOptions.forEach { selectedOption ->
                            DropdownMenuItem(
                                text = { Text(selectedOption) },
                                onClick = {
                                    selectedUnit = selectedOption
                                    isExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        },

        //Action buttons: Cancel, Remove, Save
        confirmButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.LightGray,
                        contentColor = Color.Black
                    )
                ) {
                    Text("Cancel", fontSize = 10.sp)
                }

                Button(
                    onClick = { onRemove(ingredient) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFA3AB),
                        contentColor = Color.White
                    )
                ) {
                    Text("Remove", fontSize = 9.sp)
                }

                Button(
                    onClick = {
                        val updated = ingredient.copy(
                            // not updating the originalQuantity as this is used for report calculations
                            // instead it is updating the current quantity of the item
                            quantity = quantity.toIntOrNull() ?: ingredient.quantity,
                            unit = selectedUnit
                        )
                        onSave(updated)
                    },
                    modifier = Modifier.weight(1f),
                ) {
                    Text("Save", fontSize = 10.sp)
                }
            }
        }
    )
}

/**
 * Button for navigating to the AddIngredientScreen.
 */
@Composable
fun AddIngredientsButton(navController: NavController) {
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
        Text(text = "Add Items")
    }
}

/**
 * Button that clears all items from the fridge.
 *
 * Displays an ElevatedButton labeled "Clear Fridge". When clicked, it prompts
 * the user with a confirmation dialog to avoid accidental deletion.
 * The button is only visible when the fridge is not empty (controlled externally).
 *
 * @param viewModel The IngredientViewModel instance used to trigger fridge clearing.
 */
@Composable
fun ClearFridgeButton(viewModel: IngredientViewModel) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    // Show confirmation dialog only when user taps "Clear Fridge"
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirm clear") },
            text = { Text("\uD83D\uDDD1\uFE0F Are you sure you want to clear your fridge? \n\nThis cannot be undone. \uD83D\uDE45\uD83C\uDFFB") },

            // Action buttons cancel and clear fridge
            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Closes dialog, cancels clear
                    Button(
                        onClick = { showDialog = false },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.LightGray,
                            contentColor = Color.Black
                        )
                    ) {
                        Text("Cancel", fontSize = 10.sp)
                    }

                    // Fridge cleared
                    Button(
                        onClick = {
                            viewModel.markAllIngredientsAsDeleted()
                            Toast.makeText(context, "Fridge cleared!", Toast.LENGTH_SHORT).show()
                            showDialog = false
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFA3AB),
                            contentColor = Color.Black
                        )
                    ) {
                        Text("Yes", fontSize = 10.sp)
                    }
                }
            }
        )
    }

    // Main button shown on screen
    ElevatedButton(
        onClick = { showDialog = true },
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFFA3AB),
            contentColor = Color.White
        )
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Clear",
            modifier = Modifier.padding(end = 8.dp)
        )
        Text("Clear Fridge")
    }
}
