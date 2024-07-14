package com.example.jaamebaade_client.view

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.example.jaamebaade_client.constants.AppRoutes
import com.example.jaamebaade_client.ui.theme.toNavArgs
import com.example.jaamebaade_client.view.components.CategoryItem
import com.example.jaamebaade_client.view.components.PoemsListItem
import com.example.jaamebaade_client.viewmodel.PoetCategoryPoemViewModel

@Composable
fun PoetCategoryPoemScreen(
    modifier: Modifier = Modifier,
    poetId: Int,
    parentIds: IntArray = intArrayOf(),
    navController: NavController
) {
    val poetCategoryPoemViewModel =
        hiltViewModel<PoetCategoryPoemViewModel, PoetCategoryPoemViewModel.PoetCategoryPoemViewModelFactory> { factory ->
            factory.create(poetId, parentIds)
        }

    val categories = poetCategoryPoemViewModel.categories
    val poems = poetCategoryPoemViewModel.poemsPageData.collectAsLazyPagingItems()

    LazyColumn(modifier = modifier.padding(8.dp)) {
        itemsIndexed(categories) { index, category ->
            CategoryItem(category = category) {
                navController.navigate(
                    "${AppRoutes.POET_CATEGORY_SCREEN}/$poetId/${
                        parentIds.plus(
                            category.id
                        ).toNavArgs()
                    }"
                )
            }
            if (index != categories.size - 1) {
                Divider()
            }
        }
        if (categories.isNotEmpty() && poems.itemCount > 0) {
            item {
                Divider(thickness = 2.dp)
            }
        }
        itemsIndexed(poems) { index, poem ->
            poem?.let {
                PoemsListItem(poem) {
                    navController.navigate("poem/${poetId}/${poem.id}")
                }
            }
            if (index != poems.itemCount - 1) {
                Divider()
            }
        }
    }

}