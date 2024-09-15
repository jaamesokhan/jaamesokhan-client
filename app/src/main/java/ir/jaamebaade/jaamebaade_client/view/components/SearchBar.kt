package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.model.Poet
import ir.jaamebaade.jaamebaade_client.model.SearchHistoryRecord

@Composable
fun SearchBar(
    modifier: Modifier,
    poets: List<Poet>,
    searchHistoryRecords: List<SearchHistoryRecord> = emptyList(),
    onSearchFilterChanged: (Poet?) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onSearchHistoryRecordClick: ((SearchHistoryRecord) -> Unit)? = null,
    onSearchHistoryRecordDeleteClick: ((SearchHistoryRecord) -> Unit)? = null,
    onSearchClearClick: (String) -> Unit,
    onSearchQueryIconClicked: (String) -> Unit,
) {
    var query by rememberSaveable { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedPoetIndex by rememberSaveable { mutableStateOf<Int?>(null) }
    var isSearchIconClicked by remember { mutableStateOf(false) }

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
                .background(Color.Transparent),
            trailingIcon = {
                if (!isSearchIconClicked) {
                    IconButton(onClick = {
                        onSearchQueryIconClicked(query)
                        keyboardController?.hide()
                        isSearchIconClicked = true
                    }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(R.string.SEARCH)
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
            label = {
                Text(
                    stringResource(R.string.SEARCH),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            textStyle = MaterialTheme.typography.headlineSmall,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                onSearchQueryIconClicked(query)
                isSearchIconClicked = true
                keyboardController?.hide()
            }),
        )

        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("در: ", style = MaterialTheme.typography.labelSmall)
            Spacer(modifier = Modifier.width(2.dp))
            Row(
                modifier = Modifier
                    .clickable { expanded = true }
                    .padding(2.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (selectedPoetIndex == null) {
                    Text(
                        text = "همه",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    )
                } else {
                    Text(
                        text = poets[selectedPoetIndex!!].name,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))

                Icon(imageVector = Icons.Outlined.ArrowDropDown, contentDescription = "more")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .height(200.dp)
                    .padding(8.dp)
            ) {
                DropdownMenuItem(
                    text = { Text("همه", style = MaterialTheme.typography.labelMedium) },
                    onClick = {
                        onSearchFilterChanged(null)
                        selectedPoetIndex = null
                        onSearchQueryIconClicked(query)
                        expanded = false
                    })
                poets.forEachIndexed { index, poet ->
                    HorizontalDivider()
                    DropdownMenuItem(
                        text = {
                            Text(
                                poet.name,
                                style = MaterialTheme.typography.labelMedium
                            )
                        },
                        onClick = {
                            onSearchFilterChanged(poet)
                            selectedPoetIndex = index
                            onSearchQueryIconClicked(query)
                            expanded = false
                        })
                }
            }
        }

        AnimatedVisibility(
            visible = !isSearchIconClicked,
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

@Composable
fun SearchHistoryList(
    modifier: Modifier = Modifier,
    searchHistoryRecords: List<SearchHistoryRecord>,
    onSearchHistoryRecordClick: (SearchHistoryRecord) -> Unit,
    onSearchHistoryRecordDelete: (SearchHistoryRecord) -> Unit
) {

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
    ) {
        item {
            Text(
                text = stringResource(R.string.RECENT_SEARCHES),
                style = MaterialTheme.typography.titleMedium
            )
        }

        items(searchHistoryRecords) { historyItem ->
            SearchHistoryRecordItem(
                historyItem = historyItem,
                onSearchHistoryRecordClick = onSearchHistoryRecordClick,
                onSearchHistoryRecordDelete = onSearchHistoryRecordDelete
            )
        }
    }

}

@Composable
private fun SearchHistoryRecordItem(
    historyItem: SearchHistoryRecord,
    onSearchHistoryRecordClick: (SearchHistoryRecord) -> Unit,
    onSearchHistoryRecordDelete: (SearchHistoryRecord) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSearchHistoryRecordClick(historyItem) }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(historyItem.query, style = MaterialTheme.typography.labelMedium)

        IconButton(onClick = {
            onSearchHistoryRecordDelete(historyItem)
        }) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = stringResource(R.string.DELETE),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}