import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.TextFieldColors
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
import com.github.mikephil.charting.formatter.PercentFormatter
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters

/**
 * Calculate category-wise totals and bucket small ones into "Others".
 * @param ingredients list of all fridge items
 * @return list of PieEntry(label: category, value: total)
 */
fun categoryFridgeValue(ingredients: List<Ingredient>): List<PieEntry> {
    // Group by category and sum value (quantity * price)
    val sums: Map<String, Float> = ingredients
        .groupBy { it.category }
        .mapValues { (_, group) ->
            group.sumOf {
                // use normalised quantity for accurate value calculation
                val normalizedQty = normaliseQuantity(it.quantity, it.unit)
                (normalizedQty * it.unitPrice).toDouble()
            }.toFloat()
        }

    // Grand total across all categories
    val fridgeTotal: Float = sums.values.sum()

    // Partition into major (>=5%) vs minor (<5%) categories
    val (major, minor) = sums.entries.partition { it.value / fridgeTotal >= 0.05f }

    // Build final PieEntry list, merging minor into "Others"
    return mutableListOf<PieEntry>().apply {
        // Add each major category as its own slice
        major.forEach { (cat, total) ->
            add(PieEntry(total, cat))
        }
        // Sum up all minor categories and add one "Others" slice
        val otherTotal = minor.sumOf { it.value.toDouble() }.toFloat()
        if (otherTotal > 0f) {
            add(PieEntry(otherTotal, "Others"))
        }
    }
}

// *** helper function to normalize quantity to base unit (kg or L)
fun normaliseQuantity(quantity: Int, unit: String): Float {
    return when (unit) {
        // converts grams to kg, ml to L
        "g" -> quantity / 1000f
        "ml" -> quantity / 1000f
        else -> quantity.toFloat()
    }
}

/**
 * Renders a PieChart showing fridge value breakdown by category.
 * Buckets small categories into "Others", shows percentages.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PieChartScreen(viewModel: IngredientViewModel = viewModel()) {
    // 1) Observe ingredient list from ViewModel
    val ingredients by viewModel.allIngredients.collectAsState(emptyList())

    // 2) Prepare 9-color dashboard palette
    val dashboardColors = listOf(
        0xFF3A506B.toInt(), // Steel Blue
        0xFF5BC0EB.toInt(), // Pastel Blue
        0xFF9BC53D.toInt(), // Teal-Green
        0xFFCBE32D.toInt(), // Lime
        0xFFFDE74C.toInt(), // Mustard
        0xFFFF5964.toInt(), // Coral
        0xFFFF924C.toInt(), // Soft Orange
        0xFFC65A97.toInt(), // Magenta
        0xFFBC9CFF.toInt()  // Lavender
    )

    // 3) Compute pie entries once when data changes
    val pieEntries = remember(ingredients) {
        categoryFridgeValue(ingredients)
    }

    // 4) Build the PieDataSet with styling
    val pieDataSet = PieDataSet(pieEntries, "").apply {
        colors = dashboardColors               // custom slice colors
        valueTextSize = 14f                    // label font size
        xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE // draw percent outside
        yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        valueTextColor = Color.Black.toArgb() // percent label color
        valueLineColor = Color.Black.toArgb() // connecting line color
        sliceSpace = 4f                        // gap between slices
        setDrawValues(true)                    // draw percent values
        // format values as percent of total
        valueFormatter = PercentFormatter()
    }

    // 5) Wrap dataset in PieData and re-apply percent formatter
    val pieData = PieData(pieDataSet).also {
        it.setValueFormatter(PercentFormatter())
    }

    // 6) Embed the Android PieChart view in Compose
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        factory = { ctx ->
            PieChart(ctx).apply {
                data = pieData
                description.isEnabled = false
                centerText = "Fridge"
                setUsePercentValues(true)
                setEntryLabelColor(Color.Black.toArgb()) // hide slice text
                setDrawEntryLabels(false)
                animateY(800)
                // set formatter with chart reference
                val pf = PercentFormatter(this)
                this.data.setValueFormatter(pf)
            }
        },
        update = { chart ->
            // on recomposition, update data and formatter
            chart.data = pieData
            chart.data.setValueFormatter(PercentFormatter(chart))
            chart.invalidate() // redraw
        }
    )
}
