package ir.jaamebaade.jaamebaade_client.view.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
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
    var showSearchHistory by remember { mutableStateOf(false) } // State to control history visibility

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

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
                    onSearchFocusChanged(focusState.isFocused) // Notify focus change
                },
            trailingIcon = {
                IconButton(onClick = {
                    onSearchQueryIconClicked(query)
                    keyboardController?.hide()
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
                keyboardController?.hide()
            }),
        )
        if (searchHistory != null) {
            SearchHistoryDropdown(
                expanded = showSearchHistory,
                searchHistory = searchHistory,
                onHistoryItemClick = { historyItem ->
                    onHistoryItemClick?.invoke(historyItem)
                    query = historyItem.query // Set query when history item is clicked
                    showSearchHistory = false // Close history dropdown
                },
                onHistoryItemDelete = { historyItem ->
                    onHistoryItemDeleteClick?.invoke(historyItem) // Trigger deletion
                },
                onDismissRequest = { showSearchHistory = false }
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
                        text = { Text(poet.name, style = MaterialTheme.typography.labelMedium) },
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

@Composable
fun SearchHistoryDropdown(
    expanded: Boolean,
    searchHistory: List<SearchHistoryRecord>,
    onHistoryItemClick: (SearchHistoryRecord) -> Unit,
    onHistoryItemDelete: (SearchHistoryRecord) -> Unit,
    onDismissRequest: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = Modifier.fillMaxWidth(),
        properties = PopupProperties(focusable = false),
    ) {
        searchHistory.forEach { historyItem ->
            DropdownMenuItem(
                text = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // History item text
                        Text(historyItem.query)

                        // Delete icon
                        IconButton(onClick = {
                            onHistoryItemDelete(historyItem) // Trigger deletion
                        }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete history item"
                            )
                        }
                    }
                }, onClick = {
                    onHistoryItemClick(historyItem)
                }
            )
        }
    }
}

