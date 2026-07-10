package com.example.restaurantmenu.data.model

/**
 * Firestore "menuItems" collection document.
 * Fields:
 *  - id: String (document id, auto-generated)
 *  - categoryId: String (must match a Category.id, e.g. "pizza")
 *  - name: String (food name)
 *  - price: Double (price in local currency, e.g. so'm)
 *  - imageUrl: String (food photo URL)
 *  - description: String? (optional, for future use)
 *  - available: Boolean (whether it currently shows in the menu)
 */
data class MenuItem(
    val id: String = "",
    val categoryId: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",
    val description: String? = null,
    val available: Boolean = true
)
