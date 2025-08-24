package ir.jaamebaade.jaamebaade_client.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ir.jaamebaade.jaamebaade_client.constants.AppRoutes
import ir.jaamebaade.jaamebaade_client.model.Status
import ir.jaamebaade.jaamebaade_client.model.VersePoemCategoriesPoet
import ir.jaamebaade.jaamebaade_client.model.toPathHeaderText
import ir.jaamebaade.jaamebaade_client.view.components.LoadingIndicator
import ir.jaamebaade.jaamebaade_client.view.components.CardItem
import ir.jaamebaade.jaamebaade_client.view.components.SearchBar
import ir.jaamebaade.jaamebaade_client.view.components.base.NotFoundBox
import ir.jaamebaade.jaamebaade_client.viewmodel.SearchViewModel

@Composable
fun SearchScreen(
    modifier: Modifier,
    navController: NavController,
    searchViewModel: SearchViewModel = hiltViewModel(),
) {
    var searchStatus by remember { mutableStateOf(Status.NOT_STARTED) }
    var finalSearchQuery by remember { mutableStateOf("") }

    val results = searchViewModel.results

    val poets by searchViewModel.poets
        .collectAsStateWithLifecycle(initialValue = null)

    var fetchStatus by remember { mutableStateOf(Status.LOADING) }

    LaunchedEffect(key1 = poets) {
        if (poets != null) {
            fetchStatus = Status.SUCCESS
        }
    }

    val showingSearchHistory = searchViewModel.showingSearchHistory.collectAsState()
    if (fetchStatus == Status.SUCCESS) {
        Column(modifier = modifier.background(color = MaterialTheme.colorScheme.surface)) {
            SearchBar(
                modifier = Modifier.fillMaxWidth(),
                poets = poets!!,
                searchHistoryRecords = showingSearchHistory.value,
                onSearchFilterChanged = {
                    searchViewModel.poetFilter = it
                },
                onSearchQueryChanged = {
                    searchViewModel.query = it
                    searchViewModel.onQueryChanged()
                },
                onSearchHistoryRecordClick = { historyItem ->
                    searchViewModel.query = historyItem.query
                    searchStatus = Status.LOADING
                    searchViewModel.search {
                        searchStatus = Status.SUCCESS
                    }
                },
                onSearchHistoryRecordDeleteClick = { historyItem ->
                    searchViewModel.deleteHistoryItem(historyItem)
                },
                onSearchClearClick = {
                    searchViewModel.clearSearch()
                    searchStatus = Status.NOT_STARTED
                }

            ) { query, callback ->
                if (query.length > 2) {
                    searchStatus = Status.LOADING
                    finalSearchQuery = query
                    searchViewModel.search(callBack = { searchStatus = Status.SUCCESS })
                    callback()
                }
            }

            SearchResults(
                results = results,
                searchQuery = finalSearchQuery,
                searchStatus = searchStatus,
                navController = navController
            )
        }
    } else {
        LoadingIndicator()
    }
}


@Composable
fun SearchResults(
    results: List<VersePoemCategoriesPoet>,
    searchQuery: String,
    searchStatus: Status,
    navController: NavController
) {
    if (results.isEmpty() && searchStatus == Status.SUCCESS) {
        NotFoundBox()
    } else if (searchStatus == Status.LOADING) {
        LoadingIndicator()
    } else {
        LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
            items(results) { result ->
                val content = createSearchResultBody(
                    item = result,
                    searchQuery = searchQuery,
                    color = MaterialTheme.colorScheme.onSurface
                )
                CardItem(
                    headerText = content,
                    bodyText = result.toPathHeaderText(),
                    imageUrl = result.poet.imageUrl,
                    onClick = {
                        navController.navigate("${AppRoutes.POEM}/${result.poet.id}/${result.verse!!.poemId}/${result.verse.id}")
                    },
                )
                if (result != results.last()) {
                    HorizontalDivider(
                        modifier = Modifier.padding(
                            start = 90.dp,
                            end = 0.dp,
                            top = 5.dp,
                            bottom = 5.dp
                        ),
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}

private fun createSearchResultBody(
    item: VersePoemCategoriesPoet,
    searchQuery: String,
    color: Color
): AnnotatedString {
    val text = item.verse!!.text
    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = color)) {
            append(text)
        }
        text.indexOf(searchQuery, ignoreCase = true).let { index ->
            if (index >= 0) {
                addStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold
                    ),
                    start = index,
                    end = index + searchQuery.length
                )
            }
        }
    }
    return annotatedString
}