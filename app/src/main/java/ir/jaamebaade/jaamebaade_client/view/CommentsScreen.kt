package ir.jaamebaade.jaamebaade_client.view

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ir.jaamebaade.jaamebaade_client.view.components.CommentItem
import ir.jaamebaade.jaamebaade_client.viewmodel.CommentViewModel

@Composable
fun CommentsScreen(poemId: Int, modifier: Modifier) {
    val viewModel =
        hiltViewModel<CommentViewModel, CommentViewModel.CommentViewModelFactory> { factory ->
            factory.create(
                poemId
            )
        }

    var commentText by remember { mutableStateOf("") }
    val comments = viewModel.comments

    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.Bottom,
        ) {
            items(items = comments, key = { it.id }) { comment ->
                var lapVisible by remember { mutableStateOf(false) }
                val animatedLapAlpha by animateFloatAsState(
                    targetValue = if (lapVisible) 1f else 0f,
                    label = "Lap alpha",
                    animationSpec = tween(
                        durationMillis = 250,
                        easing = LinearEasing,
                    )
                )
                CommentItem(
                    modifier = Modifier.graphicsLayer {
                        lapVisible = true
                        alpha = animatedLapAlpha
                    },
                    comment = comment,
                    onDeleteClicked = { viewModel.deleteComment(comment) }
                )
            }
        }

        TextField(
            value = commentText,
            onValueChange = { commentText = it },
            label = { Text("یادداشت", style = MaterialTheme.typography.labelMedium) },
            modifier = Modifier
                .fillMaxWidth(),
            trailingIcon = {
                IconButton(
                    onClick = {
                        viewModel.addComment(poemId, commentText)
                        commentText = ""
                    },
                    enabled = commentText.isNotEmpty()
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.AutoMirrored.Outlined.Send,
                        contentDescription = "ثبت",
                    )
                }
            }
        )
    }
}

