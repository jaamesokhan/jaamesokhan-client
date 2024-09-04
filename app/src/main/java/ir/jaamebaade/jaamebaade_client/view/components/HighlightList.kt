package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.constants.AppRoutes
import ir.jaamebaade.jaamebaade_client.model.HighlightVersePoemCategoriesPoet
import ir.jaamebaade.jaamebaade_client.model.MergedHighlight
import ir.jaamebaade.jaamebaade_client.model.toMergedHighlight
import ir.jaamebaade.jaamebaade_client.viewmodel.FavoritesViewModel

@Composable
fun HighlightList(viewModel: FavoritesViewModel, navController: NavController) {
    val highlights = viewModel.highlights
    var mergeHighlights by rememberSaveable { mutableStateOf(false) }

    val newHighlights = mutableListOf(highlights[0].toMergedHighlight())
    if (mergeHighlights) {
        for (i in 1 until highlights.size) {
            if (highlights[i].versePath.verse!!.id == newHighlights.last().highlights.last().verseId + 1
                && highlights[i].versePath.poem.id == newHighlights.last().poem.id
            ) {
                newHighlights.last().highlights.add(highlights[i].highlight)
                newHighlights.last().verses.add(highlights[i].versePath.verse!!)
            } else {
                newHighlights.add(highlights[i].toMergedHighlight())
            }
        }
    }
    if (highlights.isEmpty()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(),
        ) {
            Text(
                text = "متنی را هایلایت نکرده‌اید!",
                style = MaterialTheme.typography.headlineMedium,
            )
        }
    } else {
        Column {
            Row {
                if (mergeHighlights) {
                    Button(onClick = { mergeHighlights = !mergeHighlights }) {
                        Text(text = stringResource(R.string.MERGE_HIGHLIGHTS))
                    }
                } else {
                    OutlinedButton(onClick = { mergeHighlights = !mergeHighlights }) {
                        Text(text = stringResource(R.string.MERGE_HIGHLIGHTS))
                    }
                }
            }
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                if (mergeHighlights) {
                    items(newHighlights) { mergedHighlight ->
                        val bodyText = createMergedHighlightItemBody(
                            mergedHighlight,
                            MaterialTheme.colorScheme.tertiary
                        )
                        CardItem(
                            headerText = "${mergedHighlight.categories.joinToString(" > ") { it.text }} > ${mergedHighlight.poem.title}",
                            bodyText = bodyText,
                            icon = Icons.Outlined.Delete,
                            onClick = {
                                navController.navigate("${AppRoutes.POEM}/${mergedHighlight.poet.id}/${mergedHighlight.poem.id}/${mergedHighlight.highlights.first().verseId}")
                            },
                            onIconClick = {
                                mergedHighlight.highlights.forEach {
                                    viewModel.deleteHighlight(
                                        it
                                    )
                                }
                            },
                        )
                    }
                } else {
                    items(highlights) { highlight ->
                        val bodyText = createNormalHighlightItemBody(
                            highlight,
                            MaterialTheme.colorScheme.tertiary
                        )
                        CardItem(
                            headerText = "${highlight.versePath.categories.joinToString(" > ") { it.text }} > ${highlight.versePath.poem.title}",
                            bodyText = bodyText,
                            icon = Icons.Outlined.Delete,
                            onClick = {
                                navController.navigate("${AppRoutes.POEM}/${highlight.versePath.poet.id}/${highlight.versePath.poem.id}/${highlight.highlight.verseId}")
                            },
                            onIconClick = { viewModel.deleteHighlight(highlight) },
                        )
                    }
                }
            }
        }
    }
}

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

private fun createMergedHighlightItemBody(
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
