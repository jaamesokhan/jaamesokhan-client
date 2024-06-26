package com.example.jaamebaade_client.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.jaamebaade_client.model.VersePoemCategoryPoet
import com.example.jaamebaade_client.view.components.SearchBar
import com.example.jaamebaade_client.view.components.SearchResultItem
import com.example.jaamebaade_client.viewmodel.SearchViewModel

@Composable
fun SearchScreen(
    modifier: Modifier,
    navController: NavController,
    searchViewModel: SearchViewModel = hiltViewModel(),
) {
    var hasSearched by remember { mutableStateOf(false) }

    val results = searchViewModel.results
    val poets = searchViewModel.allPoets
    Column(modifier = modifier) {
        SearchBar(modifier = Modifier,
            poets = poets.collectAsState().value,
            onSearchFilterChanged = { searchViewModel.poetFilter = it },
            onSearchQueryChanged = { searchViewModel.query = it }) {
            searchViewModel.search()
            hasSearched = true
        }
        SearchResults(
            results = results,
            hasSearched = hasSearched,
            navController = navController
        )

    }
}

@Composable
fun SearchResults(
    results: List<VersePoemCategoryPoet>,
    hasSearched: Boolean,
    navController: NavController
) {
    LazyColumn {
        if (results.isEmpty() && hasSearched) {
            item {
                Text(text = "جست‌وجوی شما نتیجه‌ای در بر نداشت!")
            }
        } else {
            items(results) { result ->
                SearchResultItem(
                    modifier = Modifier, result = result, navController = navController
                )
            }
        }
    }
}