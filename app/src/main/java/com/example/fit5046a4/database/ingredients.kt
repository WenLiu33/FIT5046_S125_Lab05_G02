import com.example.fit5046a4.database.Ingredient
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Locale.getDefault

// 1. Helper to parse dates
val dateFormat = SimpleDateFormat("yyyy-MM-dd", getDefault())
val monthFormatter = SimpleDateFormat("dd, MMM", Locale.getDefault())
// 2. Sample data
val ingredients = listOf(
    Ingredient(
        name       = "Milk",
        quantity   = 2,
        unit       = "L",
        unitPrice  = 1.99f,
        insertDate = dateFormat.parse("2025-05-01")!!,
        expiryDate = dateFormat.parse("2025-05-13")!!
    ),
    Ingredient(
        name       = "Eggs",
        quantity   = 12,
        unit       = "pcs",
        unitPrice  = 0.25f,
        insertDate = dateFormat.parse("2025-05-02")!!,
        expiryDate = dateFormat.parse("2025-05-13")!!
    ),
    Ingredient(
        name       = "Butter",
        quantity   = 1,
        unit       = "kg",
        unitPrice  = 5.49f,
        insertDate = dateFormat.parse("2025-04-30")!!,
        expiryDate = dateFormat.parse("2025-06-01")!!
    ),
    Ingredient(
        name       = "Cheese",
        quantity   = 500,
        unit       = "g",
        unitPrice  = 4.99f,
        insertDate = dateFormat.parse("2025-05-03")!!,
        expiryDate = dateFormat.parse("2025-06-03")!!
    ),
    Ingredient(
        name       = "Yogurt",
        quantity   = 4,
        unit       = "cups",
        unitPrice  = 0.99f,
        insertDate = dateFormat.parse("2025-05-01")!!,
        expiryDate = dateFormat.parse("2025-05-08")!!
    ),
    Ingredient(
        name       = "Apple",
        quantity   = 6,
        unit       = "pcs",
        unitPrice  = 0.30f,
        insertDate = dateFormat.parse("2025-05-04")!!,
        expiryDate = dateFormat.parse("2025-05-14")!!
    ),
    Ingredient(
        name       = "Banana",
        quantity   = 8,
        unit       = "pcs",
        unitPrice  = 0.20f,
        insertDate = dateFormat.parse("2025-05-05")!!,
        expiryDate = dateFormat.parse("2025-05-13")!!
    ),
    Ingredient(
        name       = "Carrot",
        quantity   = 1,
        unit       = "kg",
        unitPrice  = 2.29f,
        insertDate = dateFormat.parse("2025-05-02")!!,
        expiryDate = dateFormat.parse("2025-05-20")!!
    ),
    Ingredient(
        name       = "Chicken Breast",
        quantity   = 1,
        unit       = "kg",
        unitPrice  = 7.99f,
        insertDate = dateFormat.parse("2025-05-03")!!,
        expiryDate = dateFormat.parse("2025-05-09")!!
    ),
    Ingredient(
        name       = "Bread",
        quantity   = 1,
        unit       = "loaf",
        unitPrice  = 2.50f,
        insertDate = dateFormat.parse("2025-05-06")!!,
        expiryDate = dateFormat.parse("2025-05-08")!!
    )
)
