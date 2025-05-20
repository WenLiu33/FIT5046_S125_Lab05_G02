package com.example.fit5046a4.reportScreen

import BarChartScreen
import PieChartScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fit5046a4.IngredientViewModel
import monthFormatter


/**
 * A reusable collapsible section with a header and animated content.
 * @param title The header text for the section.
 * @param initiallyExpanded Whether the section starts open (true) or closed (false).
 * @param content The composable content to show when expanded.
 */
@Composable
fun CollapsibleSection(
    title: String,
    modifier: Modifier = Modifier,
    initiallyExpanded: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    // Track expanded/collapsed state
    var expanded by remember { mutableStateOf(initiallyExpanded) }

    // Card container for the section
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // Header row: title + chevron icon
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded } // toggle on click
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Section title with semi-bold weight
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
                // Chevron icon indicating expanded/collapsed state
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand"
                )
            }
            // Animated content; only visible when expanded
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    content = content
                )
            }
        }
    }
}

/**
 * Displays the total current value of items in the fridge, followed by a pie chart breakdown.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TotalFridgeValue(viewModel: IngredientViewModel = viewModel()) {
    // Collect the full list of ingredients from the ViewModel
    val ingredients by viewModel.allIngredients.collectAsState(initial = emptyList())

    // Calculate total value
    var totalValue = 0f
    if (ingredients.isEmpty()) {
        // Empty-state message if no ingredients
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Your fridge is currently empty!",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    } else {
        // Sum price × quantity for each ingredient
        ingredients.forEach { ingredient ->
            totalValue += ingredient.unitPrice * ingredient.quantity
        }
        // Display total
        Text(
            text = "💰 Current value: \$${"%.2f".format(totalValue)}",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        // Pie chart of value by category
        PieChartScreen()
    }
}

/**
 * Lists ingredients expiring within the next 5 days.
 */
@Composable
fun ExpiringIngredientsList(viewModel: IngredientViewModel = viewModel()) {
    val ingredients by viewModel.allIngredients.collectAsState(initial = emptyList())

    // Filter out those expiring soon
    val expiringSoon = remember(ingredients) {
        val cutOff = System.currentTimeMillis() + 5.dayInMillis
        ingredients.filter { it.expiryDate.time <= cutOff }
    }

    if (expiringSoon.isEmpty()) {
        // Empty-state message if none expiring
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Nothing is expiring soon!",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    } else {
        // Header labels
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Item", modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
            Text("Quantity", modifier = Modifier.weight(0.4f), style = MaterialTheme.typography.titleMedium)
            Text("Expiry date", style = MaterialTheme.typography.titleMedium)
        }
        Spacer(modifier = Modifier.height(8.dp))
        // Scrollable list of items
        LazyColumn(
            modifier = Modifier.height(150.dp),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(expiringSoon) { ingredient ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(ingredient.name, modifier = Modifier.weight(1f))
                    Text(
                        text = "${ingredient.quantity} ${ingredient.unit}",
                        modifier = Modifier.weight(0.4f)
                    )
                    Text(
                        text = "${monthFormatter.format(ingredient.expiryDate)}",
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}

/**
 * Shows ingredients with low stock based on unit-specific thresholds.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun IngredientsRunningLow(viewModel: IngredientViewModel = viewModel()) {
    val ingredients by viewModel.allIngredients.collectAsState(initial = emptyList())

    // Custom low-stock criteria per unit
    val runningLow = remember(ingredients) {
        ingredients.filter { ing ->
            val isLowG   = ing.unit == "g"   && ing.quantity < 100f
            val isLowMl  = ing.unit == "ml"  && ing.quantity < 100f
            val isLowPc  = ing.unit == "pcs" && ing.quantity < 5f
            val isLowCup = ing.unit == "cups"&& ing.quantity < 5f
            // include if any condition is true
            isLowG || isLowMl || isLowPc || isLowCup
        }
    }

    if (runningLow.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Nothing is running low!",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    } else {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Item", modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
            Text("Quantity", modifier = Modifier.weight(0.4f), style = MaterialTheme.typography.titleMedium)
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(
            modifier = Modifier.height(150.dp),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(runningLow) { ingredient ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(ingredient.name, modifier = Modifier.weight(1f))
                    Text("${ingredient.quantity} ${ingredient.unit}", modifier = Modifier.weight(0.35f))
                }
            }
        }
    }
}

/**
 * Wrapper for the bar chart section with fixed height and padding.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BarChartSection(viewModel: IngredientViewModel = viewModel()) {
    Column(
        modifier = Modifier
            .height(200.dp)
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        BarChartScreen(viewModel)
    }
}

/**
 * Main report screen combining all sections into a scrollable column.
 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Report(modifier: Modifier = Modifier) {
    // Scaffold provides top/bottom bars if needed
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {}, bottomBar = {}
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(35.dp))
            Text(
                text = "🔍 Value of Fridge by Category",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF495D92)
                )
            )

            // Display total and pie chart
            TotalFridgeValue()

            // Collapsible lists for expiring and low items
            CollapsibleSection(title = "🗓️ Item(s) Expiring in 5 days") {
                ExpiringIngredientsList()
            }
            CollapsibleSection(title = "📉 Items Running Low") {
                IngredientsRunningLow()
            }

            // Collapsible bar chart section
            CollapsibleSection(title = "💸 Grocery Spending This Week") {
                BarChartSection()
            }
        }
    }
}
