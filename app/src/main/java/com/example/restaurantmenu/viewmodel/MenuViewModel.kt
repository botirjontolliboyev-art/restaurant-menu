package com.example.restaurantmenu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurantmenu.data.model.Category
import com.example.restaurantmenu.data.model.MenuItem
import com.example.restaurantmenu.data.repository.MenuRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}

class MenuViewModel(
    private val repository: MenuRepository = MenuRepository()
) : ViewModel() {

    private val _categories = MutableStateFlow<UiState<List<Category>>>(UiState.Loading)
    val categories: StateFlow<UiState<List<Category>>> = _categories.asStateFlow()

    // categoryId -> items state, cached so switching tabs doesn't reload
    private val _menuItemsByCategory =
        MutableStateFlow<Map<String, UiState<List<MenuItem>>>>(emptyMap())
    val menuItemsByCategory: StateFlow<Map<String, UiState<List<MenuItem>>>> =
        _menuItemsByCategory.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        repository.getCategories()
            .onEach { list -> _categories.value = UiState.Success(list) }
            .catch { e -> _categories.value = UiState.Error(e.message ?: "Xatolik yuz berdi") }
            .launchIn(viewModelScope)
    }

    /** Loads menu items for a category the first time it's opened. */
    fun loadMenuItems(categoryId: String) {
        if (_menuItemsByCategory.value.containsKey(categoryId)) return

        _menuItemsByCategory.update { it + (categoryId to UiState.Loading) }

        repository.getMenuItemsByCategory(categoryId)
            .onEach { items ->
                _menuItemsByCategory.update { it + (categoryId to UiState.Success(items)) }
            }
            .catch { e ->
                _menuItemsByCategory.update {
                    it + (categoryId to UiState.Error(e.message ?: "Xatolik yuz berdi"))
                }
            }
            .launchIn(viewModelScope)
    }
}
