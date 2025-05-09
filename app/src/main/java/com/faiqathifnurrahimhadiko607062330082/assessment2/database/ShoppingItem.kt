package com.faiqathifnurrahimhadiko607062330082.assessment2.database

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Priority {
    HIGH, MEDIUM, LOW
}

@Entity(tableName = "shopping_items")
data class ShoppingItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val priority: Priority,
    val isChecked: Boolean = false,
    val isArchived: Boolean = false
) 