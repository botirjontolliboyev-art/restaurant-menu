package com.example.restaurantmenu.ui.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.restaurantmenu.data.model.MenuItem
import com.example.restaurantmenu.ui.components.FoodCard
import com.example.restaurantmenu.viewmodel.UiState

/**
 * Shows the list of foods (as cards) for a single category.
 */
@Composable
fun CategoryDetailScreen(
    categoryId: String,
    itemsState: UiState<List<MenuItem>>?,
    onFirstLoad: (String) -> Unit
) {
    LaunchedEffect(categoryId) {
        onFirstLoad(categoryId)
    }

    when (itemsState) {
        null, is UiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is UiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Xatolik: ${itemsState.message}")
            }
        }
        is UiState.Success -> {
            if (itemsState.data.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Bu kategoriyada hozircha taom yo'q")
                }
                return
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(itemsState.data, key = { it.id }) { item ->
                    FoodCard(item = item)
                }
            }
        }
    }
}
