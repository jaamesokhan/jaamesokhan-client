package ir.jaamebaade.jaamebaade_client.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.constants.AppRoutes
import ir.jaamebaade.jaamebaade_client.model.Category
import ir.jaamebaade.jaamebaade_client.model.CommentPoemCategoriesPoet
import ir.jaamebaade.jaamebaade_client.model.Poem
import ir.jaamebaade.jaamebaade_client.ui.theme.SheetTopShape
import ir.jaamebaade.jaamebaade_client.ui.theme.secondaryS50
import ir.jaamebaade.jaamebaade_client.utility.convertToJalali
import ir.jaamebaade.jaamebaade_client.utility.toLocalFormatWithHour
import ir.jaamebaade.jaamebaade_client.view.components.CollectionCardItem
import ir.jaamebaade.jaamebaade_client.view.components.collectionCardBodyStyle
import ir.jaamebaade.jaamebaade_client.view.components.bookmark.BottomSheetListItem
import ir.jaamebaade.jaamebaade_client.view.components.toast.ToastType
import ir.jaamebaade.jaamebaade_client.viewmodel.MyNoteViewModel
import ir.jaamebaade.jaamebaade_client.viewmodel.ToastManager
import java.util.Date
import ir.jaamebaade.jaamebaade_client.ui.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyNotesScreen(
    modifier: Modifier,
    navController: NavController,
    viewModel: MyNoteViewModel = hiltViewModel()
) {
    val notes = viewModel.notes
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedNote by remember { mutableStateOf<CommentPoemCategoriesPoet?>(null) }
    val context = LocalContext.current
    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { backStackEntry ->
            if (backStackEntry.destination.route == AppRoutes.NOTES_SCREEN.toString()) {
                // Screen is visible again, refresh
                viewModel.refreshBookmarks()
            }
        }
    }
    if (notes.isEmpty()) {

        Row(
            modifier = Modifier
                .padding(Dimens.space10)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                tint = MaterialTheme.colorScheme.outlineVariant,
                contentDescription = "",
            )
            Spacer(modifier = Modifier.width(Dimens.space8))
            Text(
                text = stringResource(R.string.NO_NOTE),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(Dimens.space12),
            contentPadding = PaddingValues(top = Dimens.space14, bottom = Dimens.space16),
        ) {
            items(
                items = notes,
                key = { item -> item.comment.id }) { note ->
                MyNoteCardItem(
                    modifier = Modifier.animateItem(),
                    note = note,
                    onClick = {
                        navController.navigate("${AppRoutes.POEM}/${note.path.poet.id}/${note.path.poem.id}/-1")
                    },
                    onIconClick = {
                        selectedNote = note
                        showBottomSheet = true
                    },
                )

            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                shape = SheetTopShape,
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.surface,
            ) {
                Column {
                    BottomSheetListItem(
                        icon = Icons.Outlined.Share,
                        text = stringResource(R.string.SHARE),
                    ) {
                        viewModel.share(selectedNote!!, context)
                        showBottomSheet = false
                    }
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(start = Dimens.space20, end = Dimens.space4)
                    )
                    BottomSheetListItem(
                        icon = ImageVector.vectorResource(R.drawable.delete),
                        text = stringResource(R.string.DELETE),
                        contentColor = MaterialTheme.colorScheme.error,
                    ) {
                        viewModel.deleteComment(selectedNote!!)
                        showBottomSheet = false
                        ToastManager.showToast(R.string.UNBOOKMARK_SUCCESS, ToastType.SUCCESS)
                    }

                }
            }
        }
    }
}

@Composable
fun MyNoteCardItem(
    modifier: Modifier = Modifier,
    note: CommentPoemCategoriesPoet,
    onClick: () -> Unit = {},
    onIconClick: () -> Unit = {},
) {
    CollectionCardItem(
        modifier = modifier,
        pathText = createPoemPath(note.path.categories, note.path.poem),
        imageUrl = note.path.poet.imageUrl,
        onClick = onClick,
        onMoreClick = onIconClick,
    ) {
        Text(
            text = note.comment.text.trim(),
            style = collectionCardBodyStyle(),
            color = MaterialTheme.colorScheme.onBackground,
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Circle,
                tint = MaterialTheme.colorScheme.secondaryS50,
                contentDescription = null,
                modifier = Modifier.size(8.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = Date(note.comment.createdAt).convertToJalali()
                    .toLocalFormatWithHour(),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.outlineVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

private fun createPoemPath(categories: List<Category>, poem: Poem) =
    "${categories.joinToString(" > ") { it.text }} > ${poem.title}"
