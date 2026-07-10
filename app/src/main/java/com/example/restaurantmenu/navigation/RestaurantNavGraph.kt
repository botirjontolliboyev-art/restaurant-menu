package com.example.restaurantmenu.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.restaurantmenu.ui.about.AboutScreen
import com.example.restaurantmenu.ui.home.HomeScreen
import com.example.restaurantmenu.ui.menu.CategoryDetailScreen
import com.example.restaurantmenu.ui.menu.MenuScreen
import com.example.restaurantmenu.ui.splash.SplashScreen
import com.example.restaurantmenu.viewmodel.MenuViewModel

/**
 * Entry composable for the whole app: handles splash -> main flow switch
 * and hosts the bottom-navigation NavHost.
 */
@Composable
fun RestaurantApp() {
    val rootNavController = rememberNavController()

    NavHost(navController = rootNavController, startDestination = Routes.SPLASH) {
        composable(Routes.SPLASH) {
            SplashScreen(onTimeout = {
                rootNavController.navigate(Routes.HOME) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            })
        }
        composable(Routes.HOME) {
            MainScaffold(startRoute = Routes.HOME)
        }
    }
}

@Composable
private fun MainScaffold(startRoute: String) {
    val navController = rememberNavController()
    val menuViewModel: MenuViewModel = viewModel()

    Scaffold(
        bottomBar = { BottomBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startRoute,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.HOME) {
                HomeScreen()
            }
            composable(Routes.MENU) {
                val categoriesState by menuViewModel.categories.collectAsState()
                MenuScreen(
                    categoriesState = categoriesState,
                    onCategoryClick = { category ->
                        navController.navigate(
                            Routes.categoryDetail(category.id, category.name)
                        )
                    }
                )
            }
            composable(Routes.ABOUT) {
                AboutScreen()
            }
            composable(
                route = Routes.CATEGORY_DETAIL,
                arguments = listOf(
                    navArgument("categoryId") { type = NavType.StringType },
                    navArgument("categoryName") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
                val itemsMap by menuViewModel.menuItemsByCategory.collectAsState()
                CategoryDetailScreen(
                    categoryId = categoryId,
                    itemsState = itemsMap[categoryId],
                    onFirstLoad = { menuViewModel.loadMenuItems(it) }
                )
            }
        }
    }
}

@Composable
private fun BottomBar(navController: androidx.navigation.NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        val items = listOf(
            Triple(BottomNavItem.HOME, Icons.Filled.Home, "Bosh sahifa"),
            Triple(BottomNavItem.MENU, Icons.Filled.RestaurantMenu, "Menu"),
            Triple(BottomNavItem.ABOUT, Icons.Filled.Info, "Biz haqimizda")
        )
        items.forEach { (item, icon, label) ->
            val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label) }
            )
        }
    }
}
