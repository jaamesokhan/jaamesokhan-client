package ir.jaamebaade.jaamebaade_client.view.components

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import ir.jaamebaade.jaamebaade_client.model.Poet
import ir.jaamebaade.jaamebaade_client.model.SearchHistoryRecord

@Composable
fun SearchBar(
    modifier: Modifier,
    poets: List<Poet>,
    searchHistory: List<SearchHistoryRecord>? = null,
    onSearchFilterChanged: (Poet?) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onSearchFocusChanged: (Boolean) -> Unit = {},
    onHistoryItemClick: ((SearchHistoryRecord) -> Unit)? = null,
    onHistoryItemDeleteClick: ((SearchHistoryRecord) -> Unit)? = null,
    onSearchQueryIconClicked: (String) -> Unit,
) {
    var query by rememberSaveable { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedPoetIndex by rememberSaveable { mutableStateOf<Int?>(null) }
    var showSearchHistory by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    // Handle back press when search history is visible
    BackHandler(enabled = showSearchHistory) {
        showSearchHistory = false // Hide search history on back press
    }

    Column {
        TextField(
            value = query,
            onValueChange = {
                query = it
                onSearchQueryChanged(it)
                showSearchHistory = searchHistory != null
            },
            modifier = modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    Log.d("focks", "${focusState.isFocused}")
                    showSearchHistory = focusState.isFocused
                    onSearchFocusChanged(focusState.isFocused)
                },
            trailingIcon = {
                IconButton(onClick = {
                    onSearchQueryIconClicked(query)
                    keyboardController?.hide()
                    focusRequester.freeFocus()
                    focusManager.clearFocus()
                }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                }
            },
            label = {
                Text(
                    "جست‌وجو",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            textStyle = MaterialTheme.typography.headlineSmall,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                onSearchQueryIconClicked(query)
                showSearchHistory = false
                keyboardController?.hide()
            }),
        )
        Box {
            if (searchHistory != null && showSearchHistory) {
                SearchHistoryColumn(
                    searchHistory = searchHistory,
                    onHistoryItemClick = { historyItem ->
                        onHistoryItemClick?.invoke(historyItem)
                        query = historyItem.query
                        showSearchHistory = false
                    },
                    onHistoryItemDelete = { historyItem ->
                        onHistoryItemDeleteClick?.invoke(historyItem)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .zIndex(1f) // Ensure it overlays other components
                        .background(Color.White)

                )

            }

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
        }


    }
}

@Composable
fun SearchHistoryColumn(
    modifier: Modifier = Modifier,
    searchHistory: List<SearchHistoryRecord>,
    onHistoryItemClick: (SearchHistoryRecord) -> Unit,
    onHistoryItemDelete: (SearchHistoryRecord) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .verticalScroll(rememberScrollState())

    ) {
        searchHistory.forEach { historyItem ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onHistoryItemClick(historyItem) }
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(historyItem.query, style = MaterialTheme.typography.labelMedium)

                IconButton(onClick = {
                    onHistoryItemDelete(historyItem) // Trigger deletion
                }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Delete history item",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}