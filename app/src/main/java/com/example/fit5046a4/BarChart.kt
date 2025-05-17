import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.ColorUtils
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fit5046a4.IngredientViewModel
import com.example.fit5046a4.database.Ingredient
import com.example.fit5046a4.ui.theme.FIT5046A4Theme
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters

@Composable
fun macaroonPastelPalette(): List<Int> {
    // 1. Grab your base primary color as an ARGB Int
    val baseColor = MaterialTheme.colorScheme.primary.toArgb()

    // 2. Convert to HSL
    val hsl = FloatArray(3)
    ColorUtils.colorToHSL(baseColor, hsl)

    // 3. Generate seven variants from 30% → 90% lightness
    val lightnessSteps = listOf(0.30f, 0.40f, 0.50f, 0.60f, 0.70f, 0.80f, 0.90f)
    return lightnessSteps.map { light ->
        hsl[2] = light
        ColorUtils.HSLToColor(hsl)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun weeklySpendSundayToSaturday(ingredients: List<Ingredient>): List<Float> {
    // 1) Anchor to the Sunday of the current week
    val sunday = LocalDate.now()
        .with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))

    // 2) Build a list of the seven dates
    val weekDays = (0L..6L).map { sunday.plusDays(it) }

    // 3) For each date, sum up expense
    return weekDays.map { date ->
        ingredients
            .asSequence()
            .filter { ing ->
                Instant.ofEpochMilli(ing.insertDate.time)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate() == date
            }
            .sumOf { it.quantity * it.unitPrice.toDouble() }
            .toFloat()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BarChartScreen(viewModel: IngredientViewModel = viewModel()) {
    val ingredients by viewModel.allIngredients.collectAsState(
        initial = emptyList())
    val spendByDay = weeklySpendSundayToSaturday(ingredients)

    val barEntries: List<BarEntry> = spendByDay
        .mapIndexedNotNull { index, amount ->
            if (amount > 0f) {
                BarEntry(index.toFloat(), amount)
            } else {
                BarEntry(index.toFloat(), 0f)
            }
        }

    val barDataSet = BarDataSet(barEntries, "AUD").apply {
        colors = macaroonPastelPalette()
        // Optional: hide the little value-labels above the bars
        setDrawValues(false)
    }
    barDataSet.colors = macaroonPastelPalette()
    val barData = BarData(barDataSet).apply {
        barWidth = 0.8f
    }
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        factory = { ctx ->
            BarChart(ctx).apply {
                data = barData
                description.isEnabled = false
                setFitBars(true)
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.valueFormatter =
                    IndexAxisValueFormatter(listOf("Sun","Mon","Tues","Wed","Thurs","Fri","Sat"))
                animateY(400)
            }
        },
        update = { chart ->
            chart.data = barData
//            chart.invalidate()
        }
    )
}

//@Preview(showBackground = true)
//@Composable
//fun BarPreview(){
//    FIT5046A4Theme {
//        BarChartScreen()
//    }
//}