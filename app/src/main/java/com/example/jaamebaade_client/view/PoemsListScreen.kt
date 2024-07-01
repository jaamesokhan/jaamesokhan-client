package com.example.jaamebaade_client.view

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.example.jaamebaade_client.view.components.PoemsListItem
import com.example.jaamebaade_client.viewmodel.PoemsListViewModel


@Composable
fun PoemListScreen(categoryId: Int, poetId: Int, modifier: Modifier, navController: NavController) {

    val poemsListViewModel: PoemsListViewModel =
        hiltViewModel<PoemsListViewModel, PoemsListViewModel.PoemsListViewModelFactory> { factory ->
            factory.create(categoryId)
        }
    val poems = poemsListViewModel.poemsPageData.collectAsLazyPagingItems()

    LazyColumn(modifier = modifier) {
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




