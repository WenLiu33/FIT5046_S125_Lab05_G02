package com.example.fit5046a4.reportScreen

import BarChartScreen
import PieChartScreen
import android.R.attr.fontWeight
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
import androidx.compose.material3.Typography
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

@Composable
fun CollapsibleSection(
    title: String,
    modifier: Modifier = Modifier,
    initiallyExpanded:Boolean =true,
    content: @Composable ColumnScope.() -> Unit
){
    var expanded by remember { mutableStateOf(initiallyExpanded) }

    Card(
        modifier = Modifier
                .fillMaxWidth().padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation= 4.dp)
        ){
            Column {
                Row(
                    modifier =Modifier.fillMaxWidth().clickable{expanded =!expanded}.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(title, style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold)
                    )
                    Icon(
                        imageVector= if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (expanded) "Collapse" else "Expand"
                    )
                }
            }
        }
    AnimatedVisibility(
        visible = expanded,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            content = content
        )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TotalFridgeValue(viewModel: IngredientViewModel = viewModel()){
    val ingredients by viewModel.allIngredients.collectAsState(initial = emptyList())

    var totalValue = 0f
    if(ingredients.isEmpty()){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Your fridge is currently empty!",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    }else{
        ingredients.forEach{ ingredient ->
            totalValue += ingredient.unitPrice * ingredient.quantity
        }
        Text(
            text = "💰 Current value: \$${"%.2f".format(totalValue)}",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        PieChartScreen()
    }
}

@Composable
fun ExpiringIngredientsList(viewModel: IngredientViewModel = viewModel()) {
    val ingredients by viewModel.allIngredients.collectAsState(
        initial = emptyList())

    val expiringSoon = remember(ingredients) {
        val cutOff = System.currentTimeMillis() + 5.dayInMillis
        ingredients.filter { it.expiryDate.time <= cutOff }
    }

    if (expiringSoon.isEmpty()) {
        // Show a friendly “empty state” message
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Nothing is expiring soon!",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    } else {
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween){
            Text("Item", modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
            Text("Quantity", modifier = Modifier.weight(0.4f), style = MaterialTheme.typography.titleMedium)
            Text("Expiry date", style = MaterialTheme.typography.titleMedium)
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(
            modifier = Modifier.height(150.dp),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(expiringSoon) { ingredient ->
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(ingredient.name, modifier = Modifier.weight(1f) )
                    Text("${ingredient.quantity} ${ingredient.unit}", modifier = Modifier.weight(0.4f) )
                    Text("${monthFormatter.format(ingredient.expiryDate)}",
                        textAlign = TextAlign.End )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun IngredientsRunningLow(viewModel: IngredientViewModel = viewModel()) {
    val ingredients by viewModel.allIngredients.collectAsState(
        initial = emptyList())

    val runningLow = remember(ingredients) {
        ingredients.filter { ing ->
            val isLowQtyG = (ing.unit == "g") && (ing.quantity < 100f)
            val isLowMl = (ing.unit == "ml") && (ing.quantity < 100f)
            val isLowPc = (ing.unit == "pc(s)") && (ing.quantity < 5f)
            val isLowCup = (ing.unit == "cup(s)") && (ing.quantity < 5f)

            isLowQtyG || isLowMl || isLowPc || isLowCup
        }
    }

    if (runningLow.isEmpty()) {
        // Show a friendly “empty state” message
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Nothing is running low!",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    } else {
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween){
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
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(ingredient.name, modifier = Modifier.weight(1f) )
                    Text("${ingredient.quantity} ${ingredient.unit}", modifier = Modifier.weight(0.35f) )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BarChartSection(viewModel: IngredientViewModel = viewModel()){
    Column(
        modifier = Modifier
            .height(200.dp)                    // fixed 200 dp high
            .fillMaxWidth()
            .padding(16.dp),                   // inner padding
        verticalArrangement = Arrangement.spacedBy(8.dp)  // 8 dp between children
    ) {
        BarChartScreen(viewModel)
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Report(
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier  = modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)  // push content above the bar
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(35.dp))
            Text(
                text = "Value of Fridge by Category",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    //More readable
                    color = Color(0xFF495D92)
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
//            CollapsibleSection(title = "Value of the fridge") {
//            }
            TotalFridgeValue()
            Spacer(modifier = Modifier.height(16.dp))
            CollapsibleSection(title = "\uD83D\uDDD3\uFE0F Item(s) Expiring in 5 days") {
                ExpiringIngredientsList()
            }
            Spacer(modifier = Modifier.height(16.dp))
            CollapsibleSection(title = "\uD83D\uDCC9 Items Running Low") {
                IngredientsRunningLow()
            }
            Spacer(modifier = Modifier.height(16.dp))
            CollapsibleSection(title = "\uD83D\uDCB8 Grocery Spendings this Week") {
                BarChartSection()
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun ReportPreview(){
//    FIT5046A4Theme {
//        Report()
//    }
//}