package com.example.fit5046a4.api

/**
 * Data class representing a food category.
 *
 * This class holds the information related to each food category, including:
 * - A unique identifier for the category.
 * - The name of the category.
 * - A URL or path to the thumbnail image associated with the category.
 * - A description of the category.
 *
 * @author Sylvia
 * @version 1.0
 */
data class Category(
    val idCategory: String,
    val strCategory: String, //food category name
    val strCategoryThumb: String, // image
    val strCategoryDescription: String
)
