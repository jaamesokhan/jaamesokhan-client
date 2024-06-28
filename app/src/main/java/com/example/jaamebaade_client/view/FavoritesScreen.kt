package com.example.jaamebaade_client.view

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
import com.example.jaamebaade_client.view.components.BookmarkList
import com.example.jaamebaade_client.view.components.HighlightList
import com.example.jaamebaade_client.viewmodel.FavoritesViewModel

@Composable
fun FavoritesScreen(
    modifier: Modifier,
    navController: NavController,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("علاقه‌مندی‌ها", "هایلایت‌ها")
    Column(modifier) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
//            containerColor = MaterialTheme.colorScheme.inversePrimary, TODO colors!
//            contentColor = contentColorFor(backgroundColor = MaterialTheme.colorScheme.primary)
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(text = title, style = MaterialTheme.typography.headlineLarge) }
                )
            }
        }
        when (selectedTabIndex) {
            0 -> BookmarkList(viewModel, navController)
            1 -> HighlightList(viewModel, navController)
        }
    }

}



