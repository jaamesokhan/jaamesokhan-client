package ir.jaamebaade.jaamebaade_client.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.constants.AppRoutes
import ir.jaamebaade.jaamebaade_client.model.Category
import ir.jaamebaade.jaamebaade_client.model.HistoryRecordPathFirstVerse
import ir.jaamebaade.jaamebaade_client.model.Poem
import ir.jaamebaade.jaamebaade_client.ui.theme.SheetTopShape
import ir.jaamebaade.jaamebaade_client.ui.theme.secondaryS50
import ir.jaamebaade.jaamebaade_client.utility.convertToJalali
import ir.jaamebaade.jaamebaade_client.utility.toLocalFormatWithHour
import ir.jaamebaade.jaamebaade_client.view.components.CollectionCardItem
import ir.jaamebaade.jaamebaade_client.view.components.collectionCardBodyStyle
import ir.jaamebaade.jaamebaade_client.viewmodel.HistoryViewModel
import java.util.Date
import ir.jaamebaade.jaamebaade_client.ui.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: HistoryViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
    sheetState: SheetState,
) {
    val poemHistory = viewModel.poemHistory.collectAsState()
    val isSheetOpen = sheetState.isVisible
    val lazyListState = rememberLazyListState()
    // LaunchedEffect to reload history when the bottom sheet opens
    LaunchedEffect(isSheetOpen) {
        if (isSheetOpen) {
            // Reload the history every time the bottom sheet is opened
            viewModel.loadPoemHistory()
        }
    }

    LaunchedEffect(poemHistory.value) {
        // Scroll to the top only if the history data is loaded and not empty
        if (poemHistory.value.isNotEmpty()) {
            lazyListState.animateScrollToItem(0)
        }
    }
    ModalBottomSheet(
        shape = SheetTopShape,
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.space8, vertical = Dimens.space12)
                .background(color = MaterialTheme.colorScheme.surface),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = onDismiss
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = stringResource(R.string.CLOSE),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Text(
                text = stringResource(R.string.READ_HISTORY),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )
            if (poemHistory.value.isNotEmpty()) {
                TextButton(onClick = { viewModel.clearHistory() }) {
                    Text(
                        text = stringResource(R.string.DELETE_ALL_HISTORY),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }
        }

        if (poemHistory.value.isEmpty()) {
            Row(
                modifier = Modifier
                    .padding(Dimens.space10)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    tint = MaterialTheme.colorScheme.outlineVariant,
                    contentDescription = "",
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.NO_HISTORY),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
        } else {
            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.spacedBy(Dimens.space8),
                contentPadding = PaddingValues(top = Dimens.space4, bottom = Dimens.space16),
            ) {
                items(
                    items = poemHistory.value, key = { item ->
                        item.id
                    }) { historyRecord ->
                    MyHistoryCardItem(
                        modifier = Modifier.animateItem(),
                        historyRecord = historyRecord,
                        onClick = {
                            navController.navigate("${AppRoutes.POEM}/${historyRecord.path.poet.id}/${historyRecord.path.poem.id}/-1")
                            onDismiss()
                        },
                        onDelete = { viewModel.deleteHistoryRecord(historyRecord.id) }
                    )
                }
            }
        }


    }
}



@Composable
fun MyHistoryCardItem(
    modifier: Modifier = Modifier,
    historyRecord: HistoryRecordPathFirstVerse,
    onClick: () -> Unit = {},
    onDelete: (() -> Unit)? = null,
) {
    CollectionCardItem(
        modifier = modifier,
        pathText = createPoemPath(historyRecord.path.categories, historyRecord.path.poem),
        imageUrl = historyRecord.path.poet.imageUrl,
        onClick = onClick,
        onMoreClick = onDelete,
        moreIcon = Icons.Outlined.Close,
        moreIconDescription = stringResource(R.string.DELETE),
    ) {
        Text(
            text = historyRecord.firstVerse.text,
            style = collectionCardBodyStyle(),
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
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
                text = Date(historyRecord.timestamp).convertToJalali()
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
