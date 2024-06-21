package com.example.jaamebaade_client.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.jaamebaade_client.model.Verse
import com.example.jaamebaade_client.view.components.SearchBar
import com.example.jaamebaade_client.view.components.SearchResultItem
import com.example.jaamebaade_client.viewmodel.SearchViewModel

@Composable
fun SearchScreen(
    modifier: Modifier,
    navController: NavController,
    searchViewModel: SearchViewModel = hiltViewModel(),
) {
    val results = searchViewModel.results
    val poets = searchViewModel.allPoets
    Column(modifier = modifier) {
        SearchBar(
            modifier = Modifier,
            poets = poets.collectAsState().value,
            onSearchFilterChanged = { searchViewModel.poetFilter = it },
            onSearchQueryChanged = { searchViewModel.query = it }) {
            searchViewModel.search()
        }
        SearchResults(results = results, navController = navController)

    }
}

@Composable
fun SearchResults(results: List<Verse>, navController: NavController) {
    LazyColumn {
        items(results) { result ->
            SearchResultItem(modifier = Modifier, result = result, navController = navController)
        }
    }
}