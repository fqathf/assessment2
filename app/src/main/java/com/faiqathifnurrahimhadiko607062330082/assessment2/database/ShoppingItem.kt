package com.faiqathifnurrahimhadiko607062330082.assessment2.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_items")
data class ShoppingItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val quantity: Int,
    val priority: String, // "high", "medium", "low"
    val isChecked: Boolean = false
)
