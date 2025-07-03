package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.ui.theme.neutralN50
import ir.jaamebaade.jaamebaade_client.ui.theme.secondaryS40
import ir.jaamebaade.jaamebaade_client.view.components.base.CustomBottomSheet
import ir.jaamebaade.jaamebaade_client.viewmodel.CommentViewModel

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

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp)
                    .background(color = MaterialTheme.colorScheme.surface),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = onDismissRequest
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = stringResource(R.string.CLOSE),
                        tint = MaterialTheme.colorScheme.neutralN50
                    )
                }
                Text(
                    text = stringResource(R.string.NOTES),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.neutralN50,
                )
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outline)
            Column(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.background)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Bottom
            ) {
                Column(
                    verticalArrangement = Arrangement.Bottom,
                ) {
                    comments.forEach { comment ->
                        var lapVisible by remember { mutableStateOf(false) }
                        val animatedLapAlpha by animateFloatAsState(
                            targetValue = if (lapVisible) 1f else 0f,
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
                            onShareClicked = { viewModel.shareComment(comment, context) },
                            onDeleteClicked = { viewModel.deleteComment(comment) }
                        )

                        if (comment != comments.last()) {
                            HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                        }
                    }
                }

                TextField(
                    value = commentText,
                    onValueChange = { commentText = it },
                    shape = RoundedCornerShape(15.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.neutralN50,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.onBackground,
                        focusedLeadingIconColor = MaterialTheme.colorScheme.secondaryS40,
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
                                imageVector = Icons.Default.Send,
                                contentDescription = stringResource(R.string.SUBMIT),
                            )
                        }
                    }
                )
            }
        }
    }
}