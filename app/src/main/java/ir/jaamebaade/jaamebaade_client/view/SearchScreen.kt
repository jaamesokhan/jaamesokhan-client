package ir.jaamebaade.jaamebaade_client.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ir.jaamebaade.jaamebaade_client.constants.AppRoutes
import ir.jaamebaade.jaamebaade_client.model.Status
import ir.jaamebaade.jaamebaade_client.model.VersePoemCategoriesPoet
import ir.jaamebaade.jaamebaade_client.model.toPathHeaderText
import ir.jaamebaade.jaamebaade_client.view.components.CardItem
import ir.jaamebaade.jaamebaade_client.view.components.LoadingIndicator
import ir.jaamebaade.jaamebaade_client.view.components.SearchBar
import ir.jaamebaade.jaamebaade_client.viewmodel.SearchViewModel

@Composable
fun SearchScreen(
    modifier: Modifier,
    navController: NavController,
    searchViewModel: SearchViewModel = hiltViewModel(),
) {
    var searchStatus by remember { mutableStateOf(Status.NOT_STARTED) }

    val results = searchViewModel.results
    val poets = searchViewModel.allPoets
    Column(modifier = modifier) {
        SearchBar(modifier = Modifier.fillMaxWidth(),
            poets = poets.collectAsState().value,
            onSearchFilterChanged = { searchViewModel.poetFilter = it },
            onSearchQueryChanged = { searchViewModel.query = it }) {
            if (it.length > 2) {
                searchStatus = Status.LOADING
                searchViewModel.search(callBack = { searchStatus = Status.SUCCESS })
            }
        }
        SearchResults(
            results = results,
            searchQuery = searchViewModel.query,
            searchStatus = searchStatus,
            navController = navController
        )
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
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "جست‌وجوی شما نتیجه‌ای در بر نداشت!")
        }
    } else if (searchStatus == Status.LOADING) {
        LoadingIndicator()
    } else {
        LazyColumn {
            items(results) { result ->
                val bodyText = createSearchResultBody(
                    item = result,
                    searchQuery = searchQuery,
                    color = MaterialTheme.colorScheme.onSurface
                )
                CardItem(
                    headerText = result.toPathHeaderText(),
                    bodyText = bodyText,
                    onClick = {
                        navController.navigate("${AppRoutes.POEM}/${result.poet.id}/${result.verse!!.poemId}/${result.verse.id}")
                    },
                )
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