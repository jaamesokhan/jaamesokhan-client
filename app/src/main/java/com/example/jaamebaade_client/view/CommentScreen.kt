package com.example.jaamebaade_client.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jaamebaade_client.viewmodel.CommentViewModel

@Composable
fun CommentScreen(poemId: Int, modifier: Modifier) {
    val viewModel =
        hiltViewModel<CommentViewModel, CommentViewModel.CommentViewModelFactory> { factory ->
            factory.create(
                poemId
            )
        }

    var commentText by remember { mutableStateOf("") }
    val comments by viewModel.comments.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.Bottom,
        ) {
            items(comments) {
                Text(text = it.text)
            }
        }

        OutlinedTextField(
            value = commentText,
            onValueChange = { commentText = it },
            label = { Text("یادداشت") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = {
                    viewModel.addComment(poemId, commentText)
                    commentText = ""
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Send,
                        contentDescription = "ثبت",
                        modifier = Modifier.rotate(180f)
                    )
                }
            }
        )
    }
}

