package ir.jaamebaade.jaamebaade_client.view

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import ir.jaamebaade.jaamebaade_client.constants.AppRoutes
import ir.jaamebaade.jaamebaade_client.utility.toNavArgs
import ir.jaamebaade.jaamebaade_client.view.components.CategoryItem
import ir.jaamebaade.jaamebaade_client.view.components.PoemsListItem
import ir.jaamebaade.jaamebaade_client.viewmodel.PoetCategoryPoemViewModel

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
                HorizontalDivider()
            }
        }
        if (categories.isNotEmpty() && poems.itemCount > 0) {
            item {
                HorizontalDivider(thickness = 2.dp)
            }
        }
        items(poems.itemCount) { index ->
            poems[index]?.let {
                PoemsListItem(poems[index]!!) {
                    navController.navigate("${AppRoutes.POEM}/${poetId}/${poems[index]!!.id}/-1")
                }
            }
            if (index != poems.itemCount - 1) {
                HorizontalDivider()
            }
        }
    }

}