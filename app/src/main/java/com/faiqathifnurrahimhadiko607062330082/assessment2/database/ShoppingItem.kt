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
    val description: String = "",
    val category: String = "",
    val priority: Priority = Priority.MEDIUM,
    val quantity: Int = 1,
    val isArchived: Boolean = false,
    val isChecked: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
) 