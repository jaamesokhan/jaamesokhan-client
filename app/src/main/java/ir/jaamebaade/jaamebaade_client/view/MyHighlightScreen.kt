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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.constants.AppRoutes
import ir.jaamebaade.jaamebaade_client.model.Category
import ir.jaamebaade.jaamebaade_client.model.HighlightVersePoemCategoriesPoet
import ir.jaamebaade.jaamebaade_client.model.MergedHighlight
import ir.jaamebaade.jaamebaade_client.model.Poem
import ir.jaamebaade.jaamebaade_client.model.toMergedHighlight
import ir.jaamebaade.jaamebaade_client.view.components.HighlightCardItem
import ir.jaamebaade.jaamebaade_client.view.components.bookmark.BottomSheetListItem
import ir.jaamebaade.jaamebaade_client.view.components.toast.ToastType
import ir.jaamebaade.jaamebaade_client.viewmodel.MyHighlightViewModel
import ir.jaamebaade.jaamebaade_client.viewmodel.ToastManager


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyHighlightScreen(
    modifier: Modifier,
    navController: NavController,
    viewModel: MyHighlightViewModel = hiltViewModel()
) {
    val highlights = viewModel.highlights
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedHighlight by remember { mutableStateOf<MergedHighlight?>(null) }
    val context = LocalContext.current

    if (highlights.isEmpty()) {
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
                text = stringResource(R.string.NO_HIGHLIGHTS_FOUND),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }

    } else {
        val highlightsSortedByVerseId = highlights.sortedBy { it.versePath.verse!!.id }
        var newHighlights = mutableListOf(highlightsSortedByVerseId[0].toMergedHighlight())

        // mergeHighlights
        for (i in 1 until highlightsSortedByVerseId.size) {
            if (highlightsSortedByVerseId[i].versePath.verse!!.id == newHighlights.last().highlights.last().verseId + 1L
                && highlightsSortedByVerseId[i].versePath.poem.id == newHighlights.last().poem.id
            ) {
                newHighlights.last().highlights.add(highlightsSortedByVerseId[i].highlight)
                newHighlights.last().verses.add(highlightsSortedByVerseId[i].versePath.verse!!)
            } else {
                newHighlights.add(highlightsSortedByVerseId[i].toMergedHighlight())
            }
        }
        newHighlights =
            newHighlights.sortedBy { it.highlights.first().createdAt }.toMutableList()



        LazyColumn(modifier = modifier.fillMaxSize()) {
            itemsIndexed(
                items = newHighlights,
                key = { _, it -> it.highlights.first().id }) { index, mergedHighlight ->
                val headerText = createMergedHighlightItemHeader(
                    mergedHighlight,
                    MaterialTheme.colorScheme.tertiary
                )
                val pathText =
                    createHighlightPath(mergedHighlight.categories, mergedHighlight.poem)
                HighlightCardItem(
                    modifier = Modifier.animateItem(),
                    headerText = headerText,
                    bodyText = pathText,
                    imageUrl = mergedHighlight.poet.imageUrl,
                    icon = Icons.Filled.MoreVert,
                    onClick = { navController.navigate("${AppRoutes.POEM}/${mergedHighlight.poet.id}/${mergedHighlight.poem.id}/-1") },
                    onIconClick = {
                        selectedHighlight = mergedHighlight
                        showBottomSheet = true
                    }
                )
                if (index != highlights.size - 1)
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
                    viewModel.share(selectedHighlight!!, context)
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
                    selectedHighlight!!.highlights.forEach {
                        viewModel.deleteHighlight(
                            it
                        )
                    }
                    showBottomSheet = false
                    ToastManager.showToast(R.string.UNBOOKMARK_SUCCESS, ToastType.SUCCESS)
                }

            }
        }
    }
}


private fun createHighlightPath(categories: List<Category>, poem: Poem) =
    "${categories.joinToString(" > ") { it.text }} > ${poem.title}"

private fun createNormalHighlightItemBody(
    highlightInfo: HighlightVersePoemCategoriesPoet,
    color: Color
): AnnotatedString {
    val bodyText = buildAnnotatedString {
        append(highlightInfo.versePath.verse!!.text)
        addStyle(
            style = SpanStyle(background = color),
            start = highlightInfo.highlight.startIndex,
            end = highlightInfo.highlight.endIndex,
        )
    }
    return bodyText
}

private fun createMergedHighlightItemHeader(
    mergedHighlight: MergedHighlight,
    highlightedTextColor: Color
): AnnotatedString {
    var res = AnnotatedString("")
    mergedHighlight.highlights.zip(mergedHighlight.verses)
        .forEach { (highlight, verse) ->
            val annotatedString = buildAnnotatedString {
                if (highlight == mergedHighlight.highlights.last()) {
                    append(verse.text)
                } else {
                    append(verse.text.plus("\n"))
                }
                addStyle(
                    style = SpanStyle(background = highlightedTextColor),
                    start = highlight.startIndex,
                    end = highlight.endIndex
                )
            }
            res = res.plus(annotatedString)
        }

    return res
}
