package com.example.restaurantmenu.data.repository

import com.example.restaurantmenu.data.model.Category
import com.example.restaurantmenu.data.model.MenuItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Single source of truth for all Firestore reads.
 * Kept separate from ViewModels so the data source (Firestore) can be
 * swapped later (e.g. REST API, Room cache) without touching UI code.
 *
 * Firestore structure expected:
 *  categories (collection)
 *      {categoryId} (doc) -> Category
 *  menuItems (collection)
 *      {itemId} (doc) -> MenuItem
 */
class MenuRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    companion object {
        private const val COLLECTION_CATEGORIES = "categories"
        private const val COLLECTION_MENU_ITEMS = "menuItems"
    }

    /** Real-time stream of categories, ordered by "order" field. */
    fun getCategories(): Flow<List<Category>> = callbackFlow {
        val listener = db.collection(COLLECTION_CATEGORIES)
            .orderBy("order")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val categories = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Category::class.java)?.copy(id = doc.id)
                } ?: emptyList()
                trySend(categories)
            }
        awaitClose { listener.remove() }
    }

    /** Real-time stream of menu items belonging to a given category. */
    fun getMenuItemsByCategory(categoryId: String): Flow<List<MenuItem>> = callbackFlow {
        val listener = db.collection(COLLECTION_MENU_ITEMS)
            .whereEqualTo("categoryId", categoryId)
            .whereEqualTo("available", true)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val items = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(MenuItem::class.java)?.copy(id = doc.id)
                } ?: emptyList()
                trySend(items)
            }
        awaitClose { listener.remove() }
    }
}
