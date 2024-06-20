package com.example.jaamebaade_client.view

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.foundation.lazy.items
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jaamebaade_client.viewmodel.PoemsListViewModel
//import androidx.compose.runtime.getValue
import com.example.jaamebaade_client.view.components.PoemsListItem


@Composable
fun PoemListScreen(categoryId: Int) {

    val poemsListViewModel: PoemsListViewModel =
        hiltViewModel<PoemsListViewModel, PoemsListViewModel.PoemsListViewModelFactory> { factory ->
            factory.create(categoryId)
        }

    val poems = poemsListViewModel.poems
    val isLoading = poemsListViewModel.isLoading
    Log.d("Enter Poems", "$categoryId")
    if(!isLoading)
        LazyColumn {
            items(poems) { poem ->
                poem?.let {
                    PoemsListItem(poem) {}
                }
            }
    }
}


