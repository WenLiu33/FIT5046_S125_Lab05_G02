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
        originalQuantity = 2,
        unit       = "L",
        unitPrice  = 1.99f,
        insertDate = dateFormat.parse("2025-05-01")!!,
        expiryDate = dateFormat.parse("2025-05-19")!!,
        category   = "Dairy"
    ),
    Ingredient(
        name       = "Eggs",
        quantity   = 12,
        originalQuantity = 12,
        unit       = "pcs",
        unitPrice  = 0.25f,
        insertDate = dateFormat.parse("2025-05-02")!!,
        expiryDate = dateFormat.parse("2025-05-13")!!,
        category   = "Protein"
    ),
    Ingredient(
        name       = "Butter",
        quantity   = 1,
        originalQuantity = 1,
        unit       = "kg",
        unitPrice  = 5.49f,
        insertDate = dateFormat.parse("2025-04-30")!!,
        expiryDate = dateFormat.parse("2025-06-01")!!,
        category   = "Dairy"
    ),
    Ingredient(
        name       = "Cheese",
        quantity   = 500,
        originalQuantity = 500,
        unit       = "g",
        unitPrice  = 4.99f,
        insertDate = dateFormat.parse("2025-05-03")!!,
        expiryDate = dateFormat.parse("2025-06-03")!!,
        category   = "Dairy"
    ),
    Ingredient(
        name       = "Yogurt",
        quantity   = 4,
        originalQuantity = 4,
        unit       = "cups",
        unitPrice  = 0.99f,
        insertDate = dateFormat.parse("2025-05-01")!!,
        expiryDate = dateFormat.parse("2025-05-08")!!,
        category   = "Dairy"
    ),
    Ingredient(
        name       = "Apple",
        quantity   = 6,
        originalQuantity = 6,
        unit       = "pcs",
        unitPrice  = 0.30f,
        insertDate = dateFormat.parse("2025-05-04")!!,
        expiryDate = dateFormat.parse("2025-05-14")!!,
        category   = "Produce"
    ),
    Ingredient(
        name       = "Banana",
        quantity   = 8,
        originalQuantity = 8,
        unit       = "pcs",
        unitPrice  = 0.20f,
        insertDate = dateFormat.parse("2025-05-05")!!,
        expiryDate = dateFormat.parse("2025-05-13")!!,
        category   = "Produce"
    ),
    Ingredient(
        name       = "Carrot",
        quantity   = 1,
        originalQuantity = 1,
        unit       = "kg",
        unitPrice  = 2.29f,
        insertDate = dateFormat.parse("2025-05-02")!!,
        expiryDate = dateFormat.parse("2025-05-20")!!,
        category   = "Produce"
    ),
    Ingredient(
        name       = "Chicken Breast",
        quantity   = 1,
        originalQuantity = 1,
        unit       = "kg",
        unitPrice  = 7.99f,
        insertDate = dateFormat.parse("2025-05-03")!!,
        expiryDate = dateFormat.parse("2025-05-09")!!,
        category   = "Protein"
    ),
    Ingredient(
        name       = "Bread",
        quantity   = 1,
        originalQuantity = 1,
        unit       = "loaf",
        unitPrice  = 2.50f,
        insertDate = dateFormat.parse("2025-05-06")!!,
        expiryDate = dateFormat.parse("2025-05-08")!!,
        category   = "Bakery"
    ),
    Ingredient(
        name       = "Bread1",
        quantity   = 10,
        originalQuantity = 10,
        unit       = "loaf",
        unitPrice  = 2.50f,
        insertDate = dateFormat.parse("2025-05-15")!!,
        expiryDate = dateFormat.parse("2025-05-16")!!,
        category   = "Bakery"
    ),
    Ingredient(
        name       = "Bread2",
        quantity   = 20,
        originalQuantity = 20,
        unit       = "loaf",
        unitPrice  = 2.50f,
        insertDate = dateFormat.parse("2025-05-14")!!,
        expiryDate = dateFormat.parse("2025-05-18")!!,
        category   = "Diary"
    ),
    Ingredient(
        name       = "Bread3",
        quantity   = 50,
        originalQuantity = 50,
        unit       = "loaf",
        unitPrice  = 2.50f,
        insertDate = dateFormat.parse("2025-05-11")!!,
        expiryDate = dateFormat.parse("2025-05-18")!!,
        category   = "Cafe"
    ),
    Ingredient(
        name       = "Bread4",
        quantity   = 40,
        originalQuantity = 40,
        unit       = "loaf",
        unitPrice  = 2.50f,
        insertDate = dateFormat.parse("2025-05-18")!!,
        expiryDate = dateFormat.parse("2025-05-20")!!,
        category   = "Americano"
    )


)