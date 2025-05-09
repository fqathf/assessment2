package com.faiqathifnurrahimhadiko607062330082.assessment2.database

import kotlinx.coroutines.flow.Flow

class ShoppingRepository(private val shoppingItemDao: ShoppingItemDao) {
    // Get all active shopping items
    val allShoppingItems: Flow<List<ShoppingItem>> = shoppingItemDao.getAllShoppingItems()

    // Get all archived items
    val archivedItems: Flow<List<ShoppingItem>> = shoppingItemDao.getArchivedItems()

    // Get items by priority
    fun getItemsByPriority(priority: Priority): Flow<List<ShoppingItem>> {
        return shoppingItemDao.getItemsByPriority(priority)
    }

    // Get items by category
    fun getItemsByCategory(category: String): Flow<List<ShoppingItem>> {
        return shoppingItemDao.getItemsByCategory(category)
    }

    // Search items
    fun searchItems(query: String): Flow<List<ShoppingItem>> {
        return shoppingItemDao.searchItems("%$query%")
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