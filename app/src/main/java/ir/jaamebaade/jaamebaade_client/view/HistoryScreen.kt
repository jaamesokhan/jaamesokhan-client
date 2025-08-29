package ir.jaamebaade.jaamebaade_client.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
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
import ir.jaamebaade.jaamebaade_client.ui.theme.secondaryS50
import ir.jaamebaade.jaamebaade_client.utility.convertToJalali
import ir.jaamebaade.jaamebaade_client.utility.toLocalFormatWithHour
import ir.jaamebaade.jaamebaade_client.view.components.ComposableCardItem
import ir.jaamebaade.jaamebaade_client.viewmodel.HistoryViewModel
import java.util.Date

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
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp)
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
            )
        }

        if (poemHistory.value.isEmpty()) {
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxSize().background(MaterialTheme.colorScheme.background),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    tint = MaterialTheme.colorScheme.outlineVariant,
                    contentDescription = "",
                )
                Text(
                    text = stringResource(R.string.NO_HISTORY),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
        } else {
            LazyColumn(state = lazyListState, modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
                itemsIndexed(
                    items = poemHistory.value, key = { _, item ->
                        item.id
                    }) { index, historyRecord ->
                    MyHistoryCardItem(
                        historyRecord = historyRecord
                    ) {
                        navController.navigate("${AppRoutes.POEM}/${historyRecord.path.poet.id}/${historyRecord.path.poem.id}/-1")
                        onDismiss()
                    }

                    if (index != poemHistory.value.size - 1)
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
        }


    }
}



@Composable
fun MyHistoryCardItem(
    modifier: Modifier = Modifier,
    historyRecord: HistoryRecordPathFirstVerse,
    onClick: () -> Unit = {},
) {
    ComposableCardItem(
        modifier = modifier,
        imageUrl = historyRecord.path.poet.imageUrl,
        header = {
            Text(
                text = createPoemPath(historyRecord.path.categories, historyRecord.path.poem),
                style = MaterialTheme.typography.headlineMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
        },
        body = {
            Text(
                text = historyRecord.firstVerse.text,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.outlineVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        },
        footer =
            {
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

            },
        icon = null,
        iconDescription = null,
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.background
    )
}

private fun createPoemPath(categories: List<Category>, poem: Poem) =
    "${categories.joinToString(" > ") { it.text }} > ${poem.title}"
