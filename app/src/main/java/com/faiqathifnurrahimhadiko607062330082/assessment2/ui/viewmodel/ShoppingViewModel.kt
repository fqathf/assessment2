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
        val categories: List<String> = emptyList(),
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
                _uiState.update { it.copy(isLoading = true, error = null) }
                
                // Collect filtered items
                repository.getFilteredItems(
                    searchQuery = _uiState.value.searchQuery,
                    priority = _uiState.value.selectedPriority,
                    category = _uiState.value.selectedCategory
                ).collect { items ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            items = items,
                            isLoading = false,
                            error = null
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load items"
                    )
                }
            }
        }

        // Collect archived items
        viewModelScope.launch {
            try {
                repository.archivedItems.collect { items ->
                    _uiState.update { it.copy(archivedItems = items) }
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(error = e.message ?: "Failed to load archived items")
                }
            }
        }

        // Collect categories
        viewModelScope.launch {
            try {
                repository.allCategories.collect { categories ->
                    _uiState.update { it.copy(categories = categories) }
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(error = e.message ?: "Failed to load categories")
                }
            }
        }
    }

    fun addItem(item: ShoppingItem) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(error = null) }
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
                _uiState.update { it.copy(error = null) }
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
                _uiState.update { it.copy(error = null) }
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
                _uiState.update { it.copy(error = null) }
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
        loadData() // Reload data with new search query
    }

    fun setSelectedPriority(priority: Priority?) {
        _uiState.update { it.copy(selectedPriority = priority) }
        loadData() // Reload data with new priority filter
    }

    fun setSelectedCategory(category: String?) {
        _uiState.update { it.copy(selectedCategory = category) }
        loadData() // Reload data with new category filter
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