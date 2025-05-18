import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fit5046a4.IngredientViewModel
import com.example.fit5046a4.database.Ingredient
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters

/**
 * Returns one PieEntry per category, summing
 * all inserts in the current Sunday→Saturday week.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun categorySpendThisWeek(ingredients: List<Ingredient>): List<PieEntry> {
    // 1) Anchor to this week’s Sunday
    val sunday = LocalDate.now()
        .with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
    val saturday = sunday.plusDays(6)

    // 2) Filter to just those inserted this week
    val thisWeek = ingredients.filter { ing ->
        val date = Instant.ofEpochMilli(ing.insertDate.time)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        // keep if sunday ≤ date ≤ saturday
        !date.isBefore(sunday) && !date.isAfter(saturday)
    }

    // 3) Group by category and sum each
    val sumsByCategory: Map<String, Float> = thisWeek
        .groupBy { it.category }
        .mapValues { (_, group) ->
            group.sumOf { it.quantity * it.unitPrice.toDouble() }.toFloat()
        }

    // 4) Build PieEntry list
    return sumsByCategory.map { (category, total) ->
        PieEntry(total, category)
    }
}

fun categoryFridgeValue (ingredients: List<Ingredient>): List<PieEntry>{
    val sums = ingredients.groupBy { it.category }.mapValues { (_, group) ->
        group.sumOf{it.quantity* it.unitPrice.toDouble()}.toFloat() }
    val fridgeTotal = sums.values.sum()

    val (major, minor) = sums.entries.partition { it.value / fridgeTotal >=0.05f }

    val entries = mutableListOf<PieEntry>().apply {
        major.forEach{ (cat, total) ->
            add(PieEntry(total, cat))
        }
        val otherTotal = minor.sumOf{ it.value.toDouble()}.toFloat()
        if (otherTotal>0f){
            add(PieEntry(otherTotal, "Others"))
        }
    }

    return entries
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PieChartScreen(viewModel: IngredientViewModel = viewModel()) {
    // 1) Collect your full ingredient list
    val ingredients by viewModel.allIngredients.collectAsState(emptyList())

    // 2) Convert to PieEntry this week
    val pieEntries = remember(ingredients) {
//        categorySpendThisWeek(ingredients)
        categoryFridgeValue(ingredients)
    }

    // 3) Build the chart just as you did before
    val pieDataSet = PieDataSet(pieEntries, "").apply {
        colors = macaroonPastelPalette()
        valueTextSize = 14f
        xValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE
        yValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE
        valueTextColor = Color.White.toArgb()
        setDrawValues(true)
        sliceSpace = 2f
//        valueFormatter = PercentValueFormatter() // if you like %
    }
    val pieData = PieData(pieDataSet)

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        factory = { ctx ->
            PieChart(ctx).apply {
                data = pieData
                description.isEnabled = false
                centerText = "Fridge"
                setUsePercentValues(true)  // or true if you used PercentValueFormatter
                setDrawEntryLabels(true)
                animateY(800)
            }
        },
        update = { chart ->
            chart.data = pieData
            chart.invalidate()
        }
    )
}

