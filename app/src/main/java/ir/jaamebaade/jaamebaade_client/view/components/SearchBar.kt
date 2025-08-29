package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.model.Poet
import ir.jaamebaade.jaamebaade_client.model.SearchHistoryRecord
import ir.jaamebaade.jaamebaade_client.view.components.base.NotFoundBox
import ir.jaamebaade.jaamebaade_client.view.components.search.DropDownToggleOption
import ir.jaamebaade.jaamebaade_client.view.components.search.OptionDropDown

@Composable
fun SearchBar(
    modifier: Modifier,
    poets: List<Poet>,
    searchHistoryRecords: List<SearchHistoryRecord> = emptyList(),
    onSearchFilterChanged: (List<Poet>) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onSearchHistoryRecordClick: ((SearchHistoryRecord) -> Unit)? = null,
    onSearchHistoryRecordDeleteClick: ((SearchHistoryRecord) -> Unit)? = null,
    onSearchClearClick: (String) -> Unit,
    onSearchQueryIconClicked: (String, () -> Unit) -> Unit,
) {
    var query by rememberSaveable { mutableStateOf("") }
    var poetSelectionExpanded by remember { mutableStateOf(false) }
    val allPoetsOptionString = stringResource(R.string.SEARCH_ALL_POETS)
    val poetOptions = remember {
        mutableStateListOf(
            DropDownToggleOption(
                text = allPoetsOptionString,
                key = null,
            ),
            *poets.map { DropDownToggleOption(text = it.name, key = it.id) }.toTypedArray()
        )
    }
    var isSearchIconClicked by rememberSaveable { mutableStateOf(false) }


    val keyboardController = LocalSoftwareKeyboardController.current


    Column {
        TextField(
            value = query,
            onValueChange = {
                query = it
                onSearchQueryChanged(it)
            },
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.Transparent),
            shape = RoundedCornerShape(15.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
            leadingIcon = {
                if (!isSearchIconClicked) {
                    IconButton(onClick = {
                        onSearchQueryIconClicked(query) {
                            isSearchIconClicked = true
                        }
                        keyboardController?.hide()
                    }) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(R.string.SEARCH_BAR_HINT)
                        )
                    }
                } else {
                    IconButton(onClick = {
                        onSearchClearClick(query)
                        query = ""
                        isSearchIconClicked = false
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close, contentDescription = stringResource(
                                R.string.CLOSE
                            )
                        )
                    }
                }
            },
            placeholder = {
                Text(
                    stringResource(R.string.SEARCH_BAR_HINT),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            textStyle = MaterialTheme.typography.bodyLarge,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                onSearchQueryIconClicked(query) {
                    isSearchIconClicked = true
                }
                keyboardController?.hide()
            }),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Absolute.Left
        ) {
            OptionDropDown(
                selectedText = if (poetOptions.first().selected.value) {
                    poetOptions.first().text
                } else {
                    if (poetOptions.none { it.selected.value }) {
                        stringResource(R.string.EMPTY_SEARCH_FILTER)
                    } else {
                        poetOptions.filter { it.selected.value }.joinToString("/") { it.text }
                    }
                },
                opened = poetSelectionExpanded,
                onOpen = { poetSelectionExpanded = true },
                onClose = {
                    onSearchQueryIconClicked(query) {
                        isSearchIconClicked = true
                    }
                    poetSelectionExpanded = false
                    val filters = getSelectedPoets(poetOptions, poets)
                    onSearchFilterChanged(filters)
                },
                options = poetOptions,
                allOptionsKey = null,
            )
        }

        AnimatedVisibility(
            visible = !isSearchIconClicked && query.isEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            SearchHistoryList(
                searchHistoryRecords = searchHistoryRecords,
                onSearchHistoryRecordClick = { historyItem ->
                    onSearchHistoryRecordClick?.invoke(historyItem)
                    query = historyItem.query
                    isSearchIconClicked = true
                },
                onSearchHistoryRecordDelete = { historyItem ->
                    onSearchHistoryRecordDeleteClick?.invoke(historyItem)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(8.dp)

            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchHistoryList(
    modifier: Modifier = Modifier,
    searchHistoryRecords: List<SearchHistoryRecord>,
    onSearchHistoryRecordClick: (SearchHistoryRecord) -> Unit,
    onSearchHistoryRecordDelete: (SearchHistoryRecord) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.RECENT_SEARCHES),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(modifier = Modifier.width(12.dp))
            HorizontalDivider()
        }
        if (searchHistoryRecords.isEmpty()) {
            NotFoundBox()
        } else {
            LazyColumn(
                modifier = modifier
                    .fillMaxWidth()
            ) {
                items(items = searchHistoryRecords, key = { it.id }) { historyItem ->
                    SearchHistoryRecordItem(
                        modifier = Modifier.animateItem(),
                        historyItem = historyItem,
                        onSearchHistoryRecordClick = onSearchHistoryRecordClick,
                        onSearchHistoryRecordDelete = onSearchHistoryRecordDelete
                    )
                }
            }

        }
    }
}

@Composable
private fun SearchHistoryRecordItem(
    modifier: Modifier,
    historyItem: SearchHistoryRecord,
    onSearchHistoryRecordClick: (SearchHistoryRecord) -> Unit,
    onSearchHistoryRecordDelete: (SearchHistoryRecord) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSearchHistoryRecordClick(historyItem) },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = {
            onSearchHistoryRecordDelete(historyItem)
        }) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = stringResource(R.string.DELETE),
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = historyItem.query,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

fun getSelectedPoets(
    poetOptions: List<DropDownToggleOption>,
    poets: List<Poet>
): List<Poet> {
    return poetOptions.filter { it.selected.value && it.key != null }
        .map { poetOption -> poets.find { it.id == poetOption.key!! }!! }
}