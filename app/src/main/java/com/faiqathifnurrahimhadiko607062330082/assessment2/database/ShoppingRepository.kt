package com.faiqathifnurrahimhadiko607062330082.assessment2.database

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ShoppingRepository(private val shoppingItemDao: ShoppingItemDao) {
    // Get all active shopping items
    val allShoppingItems: Flow<List<ShoppingItem>> = shoppingItemDao.getAllShoppingItems()

    // Get all archived items
    val archivedItems: Flow<List<ShoppingItem>> = shoppingItemDao.getArchivedItems()

    // Get all categories
    val allCategories: Flow<List<String>> = shoppingItemDao.getAllCategories()

    // Get filtered items based on search query and filters
    fun getFilteredItems(
        searchQuery: String,
        priority: Priority?,
        category: String?
    ): Flow<List<ShoppingItem>> {
        return when {
            // If there's a search query, use search
            searchQuery.isNotBlank() -> shoppingItemDao.searchItems(searchQuery)
            // If there's a priority filter, use priority filter
            priority != null -> shoppingItemDao.getItemsByPriority(priority)
            // If there's a category filter, use category filter
            category != null -> shoppingItemDao.getItemsByCategory(category)
            // Otherwise, return all items
            else -> allShoppingItems
        }
    }

    // Insert a new shopping item
    suspend fun insert(item: ShoppingItem) {
        shoppingItemDao.insert(item)
    }

    // Update an existing shopping item
    suspend fun update(item: ShoppingItem) {
        shoppingItemDao.update(item)
    }

    // Delete a shopping item
    suspend fun delete(item: ShoppingItem) {
        shoppingItemDao.delete(item)
    }

    // Archive an item
    suspend fun archiveItem(item: ShoppingItem) {
        shoppingItemDao.update(item.copy(isArchived = true))
    }

    // Restore an archived item
    suspend fun restoreItem(item: ShoppingItem) {
        shoppingItemDao.update(item.copy(isArchived = false))
    }

    // Delete all archived items
    suspend fun deleteAllArchived() {
        shoppingItemDao.deleteAllArchived()
    }
} 