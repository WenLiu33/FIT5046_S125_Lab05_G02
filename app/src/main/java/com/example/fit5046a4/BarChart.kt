import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.ColorUtils
import com.example.fit5046a4.ui.theme.FIT5046A4Theme
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

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

@Composable
fun BarChartScreen() {
    val barEntries = listOf(
        BarEntry(0f, 1070f),
        BarEntry(1f, 4050f),
        BarEntry(2f, 3890f),
        BarEntry(3f, 5599f),
        BarEntry(4f, 2300f),
        BarEntry(5f, 4055f)
    )
    val barDataSet = BarDataSet(barEntries, "AUD")
    barDataSet.colors = macaroonPastelPalette()
    val barData = BarData(barDataSet)
    barData.barWidth = 1.0f
    AndroidView(
        modifier = Modifier.fillMaxSize(), factory = { context ->
            BarChart(context).apply { data = barData
                description.isEnabled = false
                setFitBars(true)
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.valueFormatter =
                IndexAxisValueFormatter(listOf("Sun", "Mon", "Tues", "Wed", "Thurs", "Fri","Sat"))
                animateY(4000) }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun BarPreview(){
    FIT5046A4Theme {
        BarChartScreen()
    }
}