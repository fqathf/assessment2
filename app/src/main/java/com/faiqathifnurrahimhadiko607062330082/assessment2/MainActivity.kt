package com.faiqathifnurrahimhadiko607062330082.assessment2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.faiqathifnurrahimhadiko607062330082.assessment2.database.AppDatabase
import com.faiqathifnurrahimhadiko607062330082.assessment2.database.ShoppingRepository
import com.faiqathifnurrahimhadiko607062330082.assessment2.ui.screens.ArchivedItemsScreen
import com.faiqathifnurrahimhadiko607062330082.assessment2.ui.screens.ShoppingListScreen
import com.faiqathifnurrahimhadiko607062330082.assessment2.ui.theme.ShoppingListTheme
import com.faiqathifnurrahimhadiko607062330082.assessment2.ui.viewmodel.ShoppingViewModel
import com.faiqathifnurrahimhadiko607062330082.assessment2.ui.viewmodel.ShoppingViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize database and repository
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = ShoppingRepository(database.shoppingItemDao())

        setContent {
            ShoppingListTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ShoppingListApp(repository)
                }
            }
        }
    }
}

@Composable
fun ShoppingListApp(repository: ShoppingRepository) {
    val navController = rememberNavController()
    val viewModel: ShoppingViewModel = viewModel(
        factory = ShoppingViewModelFactory(repository)
    )

    NavHost(
        navController = navController,
        startDestination = "shopping_list"
    ) {
        composable("shopping_list") {
            ShoppingListScreen(
                viewModel = viewModel,
                onNavigateToArchived = {
                    navController.navigate("archived_items")
                },
                onAddItem = {
                    // Add item is handled within the screen
                }
            )
        }
        composable("archived_items") {
            ArchivedItemsScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}