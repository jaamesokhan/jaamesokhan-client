package ir.jaamebaade.jaamebaade_client.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
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
import ir.jaamebaade.jaamebaade_client.utility.toPersianNumber
import ir.jaamebaade.jaamebaade_client.view.components.base.SquareButton
import ir.jaamebaade.jaamebaade_client.view.components.poet.ListItem
import ir.jaamebaade.jaamebaade_client.viewmodel.PoetDetailViewModel
import kotlinx.coroutines.launch

@Composable
fun PoetDetailScreen(
    modifier: Modifier = Modifier,
    poetId: Int,
    parentIds: IntArray = intArrayOf(),
    navController: NavController
) {
    val viewModel =
        hiltViewModel<PoetDetailViewModel, PoetDetailViewModel.PoetDetailViewModelFactory> { factory ->
            factory.create(poetId, parentIds)
        }

    val categories = viewModel.categories
    val poems = viewModel.poemsPageData.collectAsLazyPagingItems()

    val coroutineScope = rememberCoroutineScope()


    Box {
        LazyColumn(modifier = modifier.padding(top = 8.dp)) {
            itemsIndexed(categories) { index, categoryWithCount ->
                ListItem(
                    title = categoryWithCount.category.text,
                    subtitle = categoryWithCount.poemCount.toPersianNumber() + " " + stringResource(
                        R.string.POEM
                    ),
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
                                    categoryWithCount.category.id
                                ).toNavArgs()
                            }"
                        )
                    }
                )
            }
            if (categories.isNotEmpty() && poems.itemCount > 0) {
                item {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline)
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
        SquareButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 12.dp, bottom = 58.dp),
            icon = Icons.Default.Shuffle,
            backgroundColor = MaterialTheme.colorScheme.primary,
            tint = MaterialTheme.colorScheme.onPrimary,
            size = 80,
            iconSize = 32,
            roundedCornerShapeSize = 15,
            contentDescription = stringResource(R.string.RANDOM_POEM),
            text = null,
        ) {
            coroutineScope.launch {
                val poemWithPoet = viewModel.findShuffledPoem()
                if (poemWithPoet != null) {
                    navController.navigate("${AppRoutes.POEM}/${poemWithPoet.poet.id}/${poemWithPoet.poem.id}/-1")
                } else {
                    // TODO should this even be handled?
                }
            }
        }
    }
}