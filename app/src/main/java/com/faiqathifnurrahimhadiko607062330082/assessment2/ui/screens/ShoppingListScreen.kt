package com.faiqathifnurrahimhadiko607062330082.assessment2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.faiqathifnurrahimhadiko607062330082.assessment2.database.Priority
import com.faiqathifnurrahimhadiko607062330082.assessment2.database.ShoppingItem
import com.faiqathifnurrahimhadiko607062330082.assessment2.ui.viewmodel.ShoppingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(
    viewModel: ShoppingViewModel,
    onNavigateToArchived: () -> Unit,
    onAddItem: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showFilterDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Shopping List") },
                actions = {
                    // Search icon
                    IconButton(onClick = { /* TODO: Implement search */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                    // Filter icon
                    IconButton(onClick = { showFilterDialog = true }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filter")
                    }
                    // Archive icon
                    IconButton(onClick = onNavigateToArchived) {
                        Icon(Icons.Default.Archive, contentDescription = "Archived Items")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddItem) {
                Icon(Icons.Default.Add, contentDescription = "Add Item")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (uiState.items.isEmpty()) {
                EmptyListMessage()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.items) { item ->
                        ShoppingItemCard(
                            item = item,
                            onItemClick = { /* TODO: Implement edit */ },
                            onArchiveClick = { viewModel.archiveItem(item) },
                            onDeleteClick = { viewModel.deleteItem(item) }
                        )
                    }
                }
            }

            // Error message
            uiState.error?.let { error ->
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                ) {
                    Text(error)
                }
            }
        }
    }

    // Filter dialog
    if (showFilterDialog) {
        FilterDialog(
            selectedPriority = uiState.selectedPriority,
            selectedCategory = uiState.selectedCategory,
            onPrioritySelected = { viewModel.setSelectedPriority(it) },
            onCategorySelected = { viewModel.setSelectedCategory(it) },
            onDismiss = { showFilterDialog = false }
        )
    }
}

@Composable
fun ShoppingItemCard(
    item: ShoppingItem,
    onItemClick: () -> Unit,
    onArchiveClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onItemClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (item.description.isNotBlank()) {
                    Text(
                        text = item.description,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PriorityChip(priority = item.priority)
                    if (item.category.isNotBlank()) {
                        CategoryChip(category = item.category)
                    }
                }
            }
            Row {
                IconButton(onClick = onArchiveClick) {
                    Icon(Icons.Default.Archive, contentDescription = "Archive")
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}

@Composable
fun PriorityChip(priority: Priority) {
    val (color, text) = when (priority) {
        Priority.HIGH -> MaterialTheme.colorScheme.error to "High"
        Priority.MEDIUM -> MaterialTheme.colorScheme.tertiary to "Medium"
        Priority.LOW -> MaterialTheme.colorScheme.primary to "Low"
    }
    
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color
        )
    }
}

@Composable
fun CategoryChip(category: String) {
    Surface(
        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = category,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
fun EmptyListMessage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Your shopping list is empty",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Tap the + button to add items",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun FilterDialog(
    selectedPriority: Priority?,
    selectedCategory: String?,
    onPrioritySelected: (Priority?) -> Unit,
    onCategorySelected: (String?) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Filter Items") },
        text = {
            Column {
                Text("Priority", style = MaterialTheme.typography.titleSmall)
                Row(
                    modifier = Modifier.padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Priority.values().forEach { priority ->
                        FilterChip(
                            selected = priority == selectedPriority,
                            onClick = { onPrioritySelected(if (priority == selectedPriority) null else priority) },
                            label = { Text(priority.name.lowercase().capitalize()) }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text("Category", style = MaterialTheme.typography.titleSmall)
                // TODO: Add category filter chips
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Done")
            }
        }
    )
} 