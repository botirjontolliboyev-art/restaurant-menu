package com.example.restaurantmenu.navigation

/**
 * Central place for all navigation destinations.
 * Add new routes here when new features (order, cart, payment...) are introduced.
 */
object Routes {
    const val SPLASH = "splash"
    const val HOME = "home"
    const val MENU = "menu"
    const val ABOUT = "about"
    const val CATEGORY_DETAIL = "category_detail/{categoryId}/{categoryName}"

    fun categoryDetail(categoryId: String, categoryName: String) =
        "category_detail/$categoryId/$categoryName"
}

/** Items shown in the bottom navigation bar. */
enum class BottomNavItem(val route: String, val label: String) {
    HOME(Routes.HOME, "Bosh sahifa"),
    MENU(Routes.MENU, "Menu"),
    ABOUT(Routes.ABOUT, "Biz haqimizda")
}
