package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.view.components.base.CustomBottomSheet
import ir.jaamebaade.jaamebaade_client.viewmodel.CommentViewModel
import ir.jaamebaade.jaamebaade_client.ui.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesBottomSheet(
    onDismissRequest: () -> Unit,
    poemId: Int,
) {
    val viewModel =
        hiltViewModel<CommentViewModel, CommentViewModel.CommentViewModelFactory> { factory ->
            factory.create(
                poemId
            )
        }
    val context = LocalContext.current
    CustomBottomSheet(onDismissRequest = onDismissRequest) {
        var commentText by remember { mutableStateOf("") }
        val comments = viewModel.comments
        val listState = rememberLazyListState()
        var previousSize by remember { mutableIntStateOf(comments.size) }

        LaunchedEffect(comments.size) {
            if (comments.size > previousSize || (previousSize == 0 && comments.isNotEmpty())) {
                listState.animateScrollToItem(comments.lastIndex)
            }
            previousSize = comments.size
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.space8, vertical = Dimens.space12)
                    .background(color = MaterialTheme.colorScheme.surface),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = onDismissRequest
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = stringResource(R.string.CLOSE),
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                }
                Text(
                    text = stringResource(R.string.NOTES),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outline)

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f, fill = false)
                    .background(color = MaterialTheme.colorScheme.background)
                    .fillMaxWidth(),
            ) {
                itemsIndexed(items = comments, key = { _, comment -> comment.id }) { index, comment ->
                    Column(modifier = Modifier.animateItem()) {
                        CommentItem(
                            comment = comment,
                            onShareClicked = { viewModel.shareComment(comment, context) },
                            onDeleteClicked = { viewModel.deleteComment(comment) }
                        )
                        if (index != comments.lastIndex) {
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.outline,
                                modifier = Modifier.padding(horizontal = Dimens.space34)
                            )
                        }
                    }
                }
            }

            TextField(
                value = commentText,
                onValueChange = { commentText = it },
                shape = RoundedCornerShape(15.dp),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onBackground,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                placeholder = {
                    Text(
                        stringResource(R.string.NOTE_PLACE_HOLDER),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                },
                maxLines = 4,
                modifier = Modifier
                    .fillMaxWidth(),
                leadingIcon = {
                    IconButton(
                        onClick = {
                            viewModel.addComment(poemId, commentText)
                            commentText = ""
                        },
                        enabled = commentText.trim().isNotEmpty()
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = stringResource(R.string.SUBMIT),
                        )
                    }
                }
            )
        }
    }
}
