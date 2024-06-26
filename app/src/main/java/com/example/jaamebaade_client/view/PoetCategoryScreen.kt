package com.example.jaamebaade_client.view

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.jaamebaade_client.model.Category
import com.example.jaamebaade_client.view.components.CategoryItem
import com.example.jaamebaade_client.viewmodel.PoetCategoryViewModel
import kotlinx.coroutines.launch

@Composable
fun PoetCategoryScreen(
    modifier: Modifier = Modifier,
    poetId: Int,
    parentId: Int = 0,
    navController: NavController
) {
    val poetCategoryViewModel =
        hiltViewModel<PoetCategoryViewModel, PoetCategoryViewModel.PoetCategoryViewModelFactory> { factory ->
            factory.create(poetId, parentId)
        }
    // Observe categories LiveData
    val categories = poetCategoryViewModel.categories
    // TODO maybe add poet or parent Id to top of the page
    val coroutineScope = rememberCoroutineScope()
    LazyColumn(modifier = modifier.padding(8.dp)) {
        items(categories) { category ->
            CategoryItem(category = category) {
                coroutineScope.launch {
                    val childCategories =
                        poetCategoryViewModel.getPoetCategoriesFromRepository(poetId, category.id)
                    if (childCategories.isEmpty())
                        navController.navigate("poemsListScreen/${category.poetId}/${category.id}")
                    else
                        navController.navigate("poetCategoryScreen/${poetId}/${category.id}")

                }
            }
        }
    }

}