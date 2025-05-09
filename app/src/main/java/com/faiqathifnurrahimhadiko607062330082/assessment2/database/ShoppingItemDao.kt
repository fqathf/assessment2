package com.faiqathifnurrahimhadiko607062330082.assessment2.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingItemDao {
    @Query("SELECT * FROM shopping_items WHERE isArchived = 0 ORDER BY priority DESC, name ASC")
    fun getAllActiveItems(): Flow<List<ShoppingItem>>

    @Query("SELECT * FROM shopping_items WHERE isArchived = 1 ORDER BY priority DESC, name ASC")
    fun getAllArchivedItems(): Flow<List<ShoppingItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ShoppingItem)

    @Update
    suspend fun updateItem(item: ShoppingItem)

    @Delete
    suspend fun deleteItem(item: ShoppingItem)

    @Query("UPDATE shopping_items SET isChecked = :isChecked WHERE id = :itemId")
    suspend fun updateItemCheckedStatus(itemId: Int, isChecked: Boolean)

    @Query("UPDATE shopping_items SET isArchived = :isArchived WHERE id = :itemId")
    suspend fun updateItemArchiveStatus(itemId: Int, isArchived: Boolean)
} 