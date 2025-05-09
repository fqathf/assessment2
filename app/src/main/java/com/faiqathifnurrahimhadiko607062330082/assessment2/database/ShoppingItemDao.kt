package com.faiqathifnurrahimhadiko607062330082.assessment2.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingItemDao {
    @Query("SELECT * FROM shopping_items WHERE isArchived = 0 ORDER BY priority DESC, name ASC")
    fun getAllShoppingItems(): Flow<List<ShoppingItem>>

    @Query("SELECT * FROM shopping_items WHERE isArchived = 1 ORDER BY priority DESC, name ASC")
    fun getArchivedItems(): Flow<List<ShoppingItem>>

    @Query("SELECT * FROM shopping_items WHERE priority = :priority AND isArchived = 0 ORDER BY name ASC")
    fun getItemsByPriority(priority: Priority): Flow<List<ShoppingItem>>

    @Query("SELECT * FROM shopping_items WHERE category = :category AND isArchived = 0 ORDER BY priority DESC, name ASC")
    fun getItemsByCategory(category: String): Flow<List<ShoppingItem>>

    @Query("SELECT * FROM shopping_items WHERE (name LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%') AND isArchived = 0 ORDER BY priority DESC, name ASC")
    fun searchItems(query: String): Flow<List<ShoppingItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ShoppingItem)

    @Update
    suspend fun update(item: ShoppingItem)

    @Delete
    suspend fun delete(item: ShoppingItem)

    @Query("DELETE FROM shopping_items WHERE isArchived = 1")
    suspend fun deleteAllArchived()

    @Query("SELECT DISTINCT category FROM shopping_items WHERE category IS NOT NULL AND category != '' AND isArchived = 0 ORDER BY category ASC")
    fun getAllCategories(): Flow<List<String>>
} 