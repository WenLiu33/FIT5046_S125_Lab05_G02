import android.os.Build
import android.view.View.TEXT_ALIGNMENT_VIEW_START
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.ColorUtils
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fit5046a4.IngredientViewModel
import com.example.fit5046a4.database.Ingredient
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
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
/**
 * Generate a pastel palette by lightening the theme’s primary color.
 * Returns seven pastel variants for chart coloring.
 */
@Composable
fun macaroonPastelPalette(): List<Int> {
    // 1. Get ARGB of current Material primary color
    val baseColor = MaterialTheme.colorScheme.primary.toArgb()

    // 2. Convert that color to HSL for hue/lightness manipulation
    val hsl = FloatArray(3)
    ColorUtils.colorToHSL(baseColor, hsl)

    // 3. Create seven shades from dark (30% lightness) to very light (90%)
    val lightnessSteps = listOf(0.30f, 0.40f, 0.50f, 0.60f, 0.70f, 0.80f, 0.90f)
    return lightnessSteps.map { light ->
        hsl[2] = light
        ColorUtils.HSLToColor(hsl)
    }
}

/**
 * Compute total spend per day for the current week (Sunday → Saturday).
 * Returns a List<Float> of length 7, index 0 = Sunday, ..., 6 = Saturday.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun weeklySpendSundayToSaturday(ingredients: List<Ingredient>): List<Float> {
    // Find the most recent Sunday (or today if it's Sunday)
    val sunday = LocalDate.now()
        .with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))

    // Build the list of the seven dates of this week
    val weekDays = (0L..6L).map { sunday.plusDays(it) }

    // For each date, filter ingredients whose insertDate matches,
    // then sum quantity * price
    return weekDays.map { date ->
        ingredients
            .asSequence()
            .filter { ing ->
                // convert java.util.Date → LocalDate for comparison
                Instant.ofEpochMilli(ing.insertDate.time)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate() == date
            }
            .sumOf { it.originalQuantity * it.unitPrice.toDouble() }
            .toFloat()
    }
}

/**
 * Composable that renders the bar chart of weekly spend.
 * Disables zoom and customizes the legend and axes.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BarChartScreen(viewModel: IngredientViewModel = viewModel()) {
    // Collect full ingredient list from ViewModel
    val ingredients by viewModel.allIngredients.collectAsState(initial = emptyList())

    // Compute a 7-element list of floats (one per weekday)
    val spendByDay = weeklySpendSundayToSaturday(ingredients)

    // Map each nonzero day to a BarEntry—days with zero still get an entry at height 0
    val barEntries: List<BarEntry> = spendByDay
        .mapIndexed { index, amount ->
            BarEntry(index.toFloat(), amount)
        }

    // Create a BarDataSet with a label and our pastel colors
    val barDataSet = BarDataSet(barEntries, "(AUD)").apply {
        colors = macaroonPastelPalette() // pastel bar fill
        setDrawValues(true)              // show values above bars
    }

    // Wrap dataset in BarData and define bar width
    val barData = BarData(barDataSet).apply {
        barWidth = 0.8f
    }

    // Host the Android BarChart inside Compose
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),    // fixed height for consistency
        factory = { ctx ->
            BarChart(ctx).apply {
                data = barData
                description.isEnabled = false  // hide bottom-right description

                setFitBars(true)              // make bars fit nicely

                // Configure legend to show only text, left-aligned at bottom
                legend.apply {
                    form = Legend.LegendForm.NONE    // no color box
                    formSize = 0f                    // collapse form space
                    formToTextSpace = 0f
                    horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
                    verticalAlignment   = Legend.LegendVerticalAlignment.BOTTOM
                    orientation         = Legend.LegendOrientation.HORIZONTAL
                    textSize            = 11f
                    isWordWrapEnabled   = true
                    setDrawInside(false)             // draw outside chart area
                }

                // X-axis labels for days of week
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.valueFormatter =
                    IndexAxisValueFormatter(listOf("Sun","Mon","Tues","Wed","Thurs","Fri","Sat"))

                animateY(400)                   // animate bars vertically
                // Disable all user zoom interactions:
                setScaleEnabled(false)
                setPinchZoom(false)
                setDoubleTapToZoomEnabled(false)
            }
        },
        update = { chart ->
            // On recomposition, update data without recreating chart
            chart.data = barData
            // chart.invalidate() // optional: force redraw
        }
    )
}






