package com.example.jaamebaade_client.view

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.example.jaamebaade_client.view.components.ErrorItem
import com.example.jaamebaade_client.viewmodel.PoemsListViewModel
import com.example.jaamebaade_client.view.components.PoemsListItem


@Composable
fun PoemListScreen(categoryId: Int, poetId: Int, modifier: Modifier, navController: NavController) {

    val poemsListViewModel: PoemsListViewModel =
        hiltViewModel<PoemsListViewModel, PoemsListViewModel.PoemsListViewModelFactory> { factory ->
            factory.create(categoryId)
        }
    val poems = poemsListViewModel.poemsPageData.collectAsLazyPagingItems()

    LazyColumn(modifier = modifier) {
        itemsIndexed(poems) { _, poem ->
            poem?.let {
                PoemsListItem(poem) {
                    navController.navigate("poem/${poetId}/${poem.id}")
                }
            }
        }

        // Not sure if these code does anything!!!
        poems.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item { CircularProgressIndicator() }
                }

                loadState.append is LoadState.Loading -> {
                    item { CircularProgressIndicator() }
                }

                loadState.refresh is LoadState.Error -> {
                    val e = poems.loadState.refresh as LoadState.Error
                    item { ErrorItem(e.error) { poems.retry() } }
                }

                loadState.append is LoadState.Error -> {
                    val e = poems.loadState.append as LoadState.Error
                    item { ErrorItem(e.error) { poems.retry() } }
                }
            }
        }
    }
}



