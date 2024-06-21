package com.example.jaamebaade_client.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.jaamebaade_client.model.Poet

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(
    modifier: Modifier,
    poets: List<Poet>,
    onSearchFilterChanged: (Poet?) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onSearchQueryIconClicked: (String) -> Unit,
) {
    var query by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedPoet by remember { mutableStateOf<Poet?>(null) }

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
                .height(75.dp),
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
                Text("جست‌وجو", style = MaterialTheme.typography.bodyLarge)
            },
            textStyle = MaterialTheme.typography.headlineSmall,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                onSearchQueryIconClicked(query)
                keyboardController?.hide()
            })
        )

        Row {
            Text("جست‌وجو در: ")
            if (selectedPoet == null) {
                Text(text = "همه", modifier = Modifier.clickable { expanded = true })
            } else {
                Text(text = selectedPoet!!.name, modifier = Modifier.clickable { expanded = true })
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                DropdownMenuItem(
                    text = { Text("همه") },
                    onClick = {
                        onSearchFilterChanged(null)
                        selectedPoet = null
                        expanded = false
                    })
                poets.forEach { poet ->
                    DropdownMenuItem(
                        text = { Text(poet.name) },
                        onClick = {
                            onSearchFilterChanged(poet)
                            selectedPoet = poet
                            expanded = false
                        })
                }
            }
        }
    }
}
