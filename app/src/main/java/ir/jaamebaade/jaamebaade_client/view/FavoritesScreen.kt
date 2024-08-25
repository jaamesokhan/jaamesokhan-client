package ir.jaamebaade.jaamebaade_client.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ir.jaamebaade.jaamebaade_client.view.components.BookmarkList
import ir.jaamebaade.jaamebaade_client.view.components.CommentsList
import ir.jaamebaade.jaamebaade_client.view.components.HighlightList
import ir.jaamebaade.jaamebaade_client.viewmodel.FavoritesViewModel

@Composable
fun FavoritesScreen(
    modifier: Modifier,
    navController: NavController,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("علاقه‌مندی‌ها", "هایلایت‌ها", "یادداشت‌ها")
    Column(modifier) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(text = title, style = MaterialTheme.typography.headlineMedium) },
                    selectedContentColor = MaterialTheme.colorScheme.onSurface,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        when (selectedTabIndex) {
            0 -> BookmarkList(viewModel, navController)
            1 -> HighlightList(viewModel, navController)
            2 -> CommentsList(viewModel, navController)
        }
    }
}



