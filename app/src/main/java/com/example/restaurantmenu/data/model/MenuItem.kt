package com.example.restaurantmenu.data.model

data class MenuItem(
    val id: String = "",
    val categoryId: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",
    val imageBase64: String? = null,
    val description: String? = null,
    val available: Boolean = true
)
