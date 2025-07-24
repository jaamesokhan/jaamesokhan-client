package ir.jaamebaade.jaamebaade_client.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.constants.AppRoutes
import ir.jaamebaade.jaamebaade_client.model.BookmarkPoemCategoriesPoetFirstVerse
import ir.jaamebaade.jaamebaade_client.view.components.NewCardItem
import ir.jaamebaade.jaamebaade_client.view.components.bookmark.BottomSheetListItem
import ir.jaamebaade.jaamebaade_client.view.components.toast.ToastType
import ir.jaamebaade.jaamebaade_client.viewmodel.MyBookmarkViewModel
import ir.jaamebaade.jaamebaade_client.viewmodel.ToastManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBookmarkScreen(
    modifier: Modifier,
    navController: NavController,
    viewModel: MyBookmarkViewModel = hiltViewModel()
) {
    val bookmarks = viewModel.bookmarks
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedBookmark by remember { mutableStateOf<BookmarkPoemCategoriesPoetFirstVerse?>(null) }
    val context = LocalContext.current
    if (bookmarks.isEmpty()) {

        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                tint = MaterialTheme.colorScheme.outlineVariant,
                contentDescription = "",
            )
            Text(
                text = stringResource(R.string.NO_BOOKMARK),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }
    } else {
        LazyColumn(modifier = modifier.fillMaxSize()) {
            itemsIndexed(
                items = bookmarks,
                key = { _, item -> item.bookmark.id }) { index, bookmark ->
                NewCardItem(
                    modifier = Modifier.animateItem(),
                    headerText = AnnotatedString(bookmark.poem.title),
                    bodyText = bookmark.firstVerse.text,
                    imageUrl = bookmark.poet.imageUrl,
                    onClick = { navController.navigate("${AppRoutes.POEM}/${bookmark.poet.id}/${bookmark.poem.id}/-1") },
                    icon = Icons.Filled.MoreVert,
                    onIconClick = {
                        selectedBookmark = bookmark
                        showBottomSheet = true
                    }
                )
                if (index != bookmarks.size - 1)
                    HorizontalDivider(
                        modifier = Modifier.padding(
                            start = 90.dp,
                            end = 0.dp,
                            top = 5.dp,
                            bottom = 5.dp
                        ),
                        color = MaterialTheme.colorScheme.outline
                    )

            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
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
                        viewModel.share(selectedBookmark!!, context)
                        showBottomSheet = false
                    }
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(start = 20.dp, end = 5.dp)
                    )
                    BottomSheetListItem(
                        icon = ImageVector.vectorResource(R.drawable.delete),
                        text = stringResource(R.string.DELETE),
                        contentColor = MaterialTheme.colorScheme.error,
                    ) {
                        viewModel.deleteBookmark(selectedBookmark!!)
                        showBottomSheet = false
                        ToastManager.showToast(R.string.UNBOOKMARK_SUCCESS, ToastType.SUCCESS)
                    }

                }
            }
        }
    }
}