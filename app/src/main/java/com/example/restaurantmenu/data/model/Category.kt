package com.example.restaurantmenu.data.model

/**
 * Firestore "categories" collection document.
 * Fields:
 *  - id: String (document id, e.g. "pizza")
 *  - name: String (e.g. "Pizza")
 *  - iconUrl: String? (optional icon/image for category tile)
 *  - order: Int (display order, ascending)
 */
data class Category(
    val id: String = "",
    val name: String = "",
    val iconUrl: String? = null,
    val order: Int = 0
)
