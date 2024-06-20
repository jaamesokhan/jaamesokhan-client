package com.example.jaamebaade_client.view

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.jaamebaade_client.view.components.CategoryItem
import com.example.jaamebaade_client.viewmodel.PoetCategoryViewModel

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
    val categories = poetCategoryViewModel.categories
    // TODO maybe add poet or parent Id to top of the page
    LazyColumn(modifier = modifier.padding(8.dp)) {
        items(categories) { category ->
            CategoryItem(category = category) {
                navController.navigate("poetCategoryScreen/${poetId}/${category.id}")
            }
        }
    }

}