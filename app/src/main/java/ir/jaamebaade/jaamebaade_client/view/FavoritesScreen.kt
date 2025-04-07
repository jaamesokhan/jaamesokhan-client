package ir.jaamebaade.jaamebaade_client.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.view.components.BookmarkList
import ir.jaamebaade.jaamebaade_client.view.components.CommentsList
import ir.jaamebaade.jaamebaade_client.view.components.HighlightList
import ir.jaamebaade.jaamebaade_client.view.components.TabItem
import ir.jaamebaade.jaamebaade_client.viewmodel.FavoritesViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavoritesScreen(
    modifier: Modifier,
    navController: NavController,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val tabs = listOf(
        TabItem(
            text = stringResource(R.string.BOOKMARKS),
            screen = { BookmarkList(viewModel, navController) }
        ),
        TabItem(
            text = stringResource(R.string.HIGHLIGHTS),
            screen = { HighlightList(viewModel, navController) }
        ),
        TabItem(
            text = stringResource(R.string.NOTES),
            screen = { CommentsList(viewModel, navController) }
        ),
    )
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    Column(modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
        ) {
            tabs.forEachIndexed { index, item ->
                Tab(
                    text = {
                        Text(
                            text = item.text,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    },
                    selected = pagerState.currentPage == index,
                    selectedContentColor = MaterialTheme.colorScheme.onSurface,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } }
                )
            }
        }
        HorizontalPager(
            state = pagerState,
        ) {
            tabs[pagerState.currentPage].screen()
        }
    }
}