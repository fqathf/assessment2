package com.faiqathifnurrahimhadiko607062330082.assessment2.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.faiqathifnurrahimhadiko607062330082.assessment2.database.AppDatabase
import com.faiqathifnurrahimhadiko607062330082.assessment2.database.ShoppingItem
import kotlinx.coroutines.launch

class ShoppingViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).shoppingDao()

    val allItems = dao.getAllItems().flowIn(viewModelScope)

    fun addItem(name: String, quantity: Int, priority: String) {
        if (name.isBlank() || quantity <= 0) {
            // Sanity check: Input tidak valid
            return
        }
        viewModelScope.launch {
            dao.insertItem(ShoppingItem(name = name, quantity = quantity, priority = priority))
        }
    }

    fun toggleCheckStatus(item: ShoppingItem) {
        viewModelScope.launch {
            dao.updateItem(item.copy(isChecked = !item.isChecked))
        }
    }
}