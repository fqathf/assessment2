package com.faiqathifnurrahimhadiko607062330082.assessment2.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.faiqathifnurrahimhadiko607062330082.assessment2.database.ShoppingItem
import com.faiqathifnurrahimhadiko607062330082.assessment2.database.ShoppingRepository
import com.faiqathifnurrahimhadiko607062330082.assessment2.database.Priority
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ShoppingViewModel(private val repository: ShoppingRepository) : ViewModel() {

    // UI State
    data class ShoppingUiState(
        val items: List<ShoppingItem> = emptyList(),
        val archivedItems: List<ShoppingItem> = emptyList(),
        val searchQuery: String = "",
        val selectedPriority: Priority? = null,
        val selectedCategory: String? = null,
        val isLoading: Boolean = false,
        val error: String? = null
    )

    private val _uiState = MutableStateFlow(ShoppingUiState())
    val uiState: StateFlow<ShoppingUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                
                // Collect active items
                repository.allShoppingItems.collect { items ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            items = items,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "An error occurred"
                    )
                }
            }
        }

        // Collect archived items
        viewModelScope.launch {
            repository.archivedItems.collect { items ->
                _uiState.update { it.copy(archivedItems = items) }
            }
        }
    }

    fun addItem(item: ShoppingItem) {
        viewModelScope.launch {
            try {
                repository.insert(item)
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(error = e.message ?: "Failed to add item")
                }
            }
        }
    }

    fun updateItem(item: ShoppingItem) {
        viewModelScope.launch {
            try {
                repository.update(item)
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(error = e.message ?: "Failed to update item")
                }
            }
        }
    }

    fun deleteItem(item: ShoppingItem) {
        viewModelScope.launch {
            try {
                repository.delete(item)
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(error = e.message ?: "Failed to delete item")
                }
            }
        }
    }

    fun archiveItem(item: ShoppingItem) {
        viewModelScope.launch {
            try {
                repository.archiveItem(item)
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(error = e.message ?: "Failed to archive item")
                }
            }
        }
    }

    fun restoreItem(item: ShoppingItem) {
        viewModelScope.launch {
            try {
                repository.restoreItem(item)
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(error = e.message ?: "Failed to restore item")
                }
            }
        }
    }

    fun deleteAllArchived() {
        viewModelScope.launch {
            try {
                repository.deleteAllArchived()
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(error = e.message ?: "Failed to delete archived items")
                }
            }
        }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun setSelectedPriority(priority: Priority?) {
        _uiState.update { it.copy(selectedPriority = priority) }
    }

    fun setSelectedCategory(category: String?) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}

class ShoppingViewModelFactory(private val repository: ShoppingRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShoppingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ShoppingViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 