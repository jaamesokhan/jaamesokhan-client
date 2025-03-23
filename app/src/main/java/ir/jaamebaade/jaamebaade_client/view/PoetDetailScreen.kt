package ir.jaamebaade.jaamebaade_client.view

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.constants.AppRoutes
import ir.jaamebaade.jaamebaade_client.utility.toNavArgs
import ir.jaamebaade.jaamebaade_client.view.components.poet.ListItem
import ir.jaamebaade.jaamebaade_client.viewmodel.PoetCategoryPoemViewModel

@Composable
fun PoetDetailScreen(
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

    LazyColumn(modifier = modifier.padding(top = 8.dp)) {
        itemsIndexed(categories) { index, category ->
            ListItem(
                title = category.text,
                showDivider = index != categories.size - 1,
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.my_poets),
                        contentDescription = stringResource(R.string.CATEGORY),
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(24.dp)
                    )
                },
                onClick = {
                    navController.navigate(
                        "${AppRoutes.POET_CATEGORY_SCREEN}/$poetId/${
                            parentIds.plus(
                                category.id
                            ).toNavArgs()
                        }"
                    )
                }
            )
        }
        if (categories.isNotEmpty() && poems.itemCount > 0) {
            item {
                HorizontalDivider(thickness = 2.dp)
            }
        }
        items(poems.itemCount) { index ->
            poems[index]?.let { poemWithVerse ->
                ListItem(
                    title = poemWithVerse.poem.title,
                    subtitle = poemWithVerse.firstVerse.text,
                    showDivider = index != poems.itemCount - 1,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.poem),
                            contentDescription = stringResource(R.string.POEM),
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    onClick = {
                        navController.navigate("${AppRoutes.POEM}/${poetId}/${poemWithVerse.poem.id}/-1")
                    }
                )
            }
        }
    }
}