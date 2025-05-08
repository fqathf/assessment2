package com.faiqathifnurrahimhadiko607062330082.assessment2.ui.screens

@Composable
fun ShoppingListScreen(
    viewModel: ShoppingViewModel,
    onNavigateToAdd: () -> Unit
) {
    val items by viewModel.allItems.collectAsState(initial = emptyList())

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAdd) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(items) { item ->
                ShoppingItemCard(
                    item = item,
                    onCheckChange = { viewModel.toggleCheckStatus(item) }
                )
            }
        }
    }
}