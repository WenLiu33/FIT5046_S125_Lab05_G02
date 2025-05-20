package com.example.fit5046a4.api

/**
 * Data class representing a response containing a list of food categories.
 *
 * This class holds the list of `Category` objects returned as part of the API response.
 *
 *
 * @author Sylvia
 * @version 1.0
 */
data class CategoryResponse(
    val categories: List<Category>
)
