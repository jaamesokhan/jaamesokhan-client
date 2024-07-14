package com.example.jaamebaade_client.view

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.jaamebaade_client.constants.AppRoutes
import com.example.jaamebaade_client.ui.theme.toNavArgs
import com.example.jaamebaade_client.view.components.CategoryItem
import com.example.jaamebaade_client.viewmodel.PoetCategoryViewModel
import kotlinx.coroutines.launch

@Composable
fun PoetCategoryScreen(
    modifier: Modifier = Modifier,
    poetId: Int,
    parentIds: IntArray = intArrayOf(),
    navController: NavController
) {
    val poetCategoryViewModel =
        hiltViewModel<PoetCategoryViewModel, PoetCategoryViewModel.PoetCategoryViewModelFactory> { factory ->
            factory.create(poetId, parentIds)
        }
    // Observe categories LiveData
    val categories = poetCategoryViewModel.categories
    val coroutineScope = rememberCoroutineScope()
    LazyColumn(modifier = modifier.padding(8.dp)) {
        itemsIndexed(categories) { index, category ->
            CategoryItem(category = category) {
                coroutineScope.launch {
                    val childCategories =
                        poetCategoryViewModel.getPoetCategoriesFromRepository(poetId, category.id)
                    if (childCategories.isEmpty()) {
                        navController.navigate(
                            "${AppRoutes.POEMS_LIST_SCREEN}/${category.poetId}/${
                                parentIds.plus(
                                    category.id
                                ).toNavArgs()
                            }"
                        )
                    } else {
                        navController.navigate(
                            "${AppRoutes.POET_CATEGORY_SCREEN}/$poetId/${
                                parentIds.plus(
                                    category.id
                                ).toNavArgs()
                            }"
                        )
                    }
                }
            }
            if (index != categories.size - 1) {
                Divider()
            }
        }
    }

}