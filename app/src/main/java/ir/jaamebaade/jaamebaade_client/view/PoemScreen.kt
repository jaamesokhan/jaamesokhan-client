package ir.jaamebaade.jaamebaade_client.view

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material.icons.outlined.VolumeUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.model.CharSpan
import ir.jaamebaade.jaamebaade_client.model.Status
import ir.jaamebaade.jaamebaade_client.model.VersePoemCategoriesPoet
import ir.jaamebaade.jaamebaade_client.model.VerseWithHighlights
import ir.jaamebaade.jaamebaade_client.model.toPathHeaderText
import ir.jaamebaade.jaamebaade_client.repository.FontRepository
import ir.jaamebaade.jaamebaade_client.ui.theme.primary20
import ir.jaamebaade.jaamebaade_client.ui.theme.primary90
import ir.jaamebaade.jaamebaade_client.view.components.AudioListItems
import ir.jaamebaade.jaamebaade_client.view.components.NotesBottomSheet
import ir.jaamebaade.jaamebaade_client.view.components.SelectionBottomSheet
import ir.jaamebaade.jaamebaade_client.view.components.SelectionEndpoints
import ir.jaamebaade.jaamebaade_client.view.components.SelectionToolbar
import ir.jaamebaade.jaamebaade_client.view.components.VerseItem
import ir.jaamebaade.jaamebaade_client.view.components.VerseSelectionController
import ir.jaamebaade.jaamebaade_client.view.components.poem.PoemMoreOptionsList
import ir.jaamebaade.jaamebaade_client.view.components.poem.PoemOptionItem
import ir.jaamebaade.jaamebaade_client.view.components.poem.PoemScreenActionHeader
import ir.jaamebaade.jaamebaade_client.view.components.poem.PoemScreenBottomToolBar
import ir.jaamebaade.jaamebaade_client.view.components.poem.PoemScreenTitle
import ir.jaamebaade.jaamebaade_client.view.components.poem.ToggleButtonItem
import ir.jaamebaade.jaamebaade_client.viewmodel.AppNavHostViewModel
import ir.jaamebaade.jaamebaade_client.viewmodel.PoemViewModel
import ir.jaamebaade.jaamebaade_client.viewmodel.SelectionOptionViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PoemScreen(
    navController: NavController,
    poemId: Int,
    poetId: Int,
    focusedVerseId: Long?,
    modifier: Modifier,
    appNavHostViewModel: AppNavHostViewModel = hiltViewModel(),
    fontRepository: FontRepository
) {
    val context = LocalContext.current
    val verseStyle = SpanStyle(
        fontFamily = fontRepository.getPoemFontFamily(),
        fontSize = fontRepository.getPoemFontSize()
    )
    var path by remember(poemId) {
        mutableStateOf<VersePoemCategoriesPoet?>(null)
    }

    var poemTitle by remember(poemId) {
        mutableStateOf("")
    }

    val poemViewModel =
        hiltViewModel<PoemViewModel, PoemViewModel.VerseViewModelFactory> { factory ->
            factory.create(
                poemId, poetId
            )
        }

    LaunchedEffect(poemId) {
        poemViewModel.onPoemVisited(poemId)
    }
    var minId by remember { mutableIntStateOf(0) }
    var maxId by remember { mutableIntStateOf(0) }

    var shouldFocusForSearch by remember { mutableStateOf(false) }
    var shouldFocusForRecitation by remember { mutableStateOf(false) }


    var showNotes by remember { mutableStateOf(false) }

    var showHighlights by remember { mutableStateOf(true) }
    var showVerseNumbers by remember { mutableStateOf(false) }
    var selectMode by remember { mutableStateOf(false) }
    var poemHeaderRevealFraction by remember { mutableStateOf(1f) }
    val isBookmarked by poemViewModel.isBookmarked.collectAsState()

    val lazyListState = rememberLazyListState()

    val mediaPlayer = appNavHostViewModel.mediaPlayer
    val playStatus = appNavHostViewModel.playStatus
    val syncInfoFetchStatus = poemViewModel.syncInfoFetchStatus
    val audioSyncData = poemViewModel.audioSyncInfo.collectAsState().value
    var recitedVerseIndex by remember { mutableIntStateOf(0) }

    val versesWithHighlights by poemViewModel.verses.collectAsState()
    val showHintForHighlight = poemViewModel.showHintForHighlight
    val focusedVerse = versesWithHighlights.find { it.verse.id == focusedVerseId }

    val selectedVerses = remember { mutableStateListOf<VerseWithHighlights>() }
    val collapseRangePx = with(LocalDensity.current) { 150.dp.toPx() }
    var collapsedPoemHeaderOffsetPx by remember(poemId) { mutableFloatStateOf(0f) }

    val selectionController = remember { VerseSelectionController() }
    var lazyColumnCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var isDraggingSelection by remember { mutableStateOf(false) }
    var currentDragWindowPosition by remember { mutableStateOf(Offset.Zero) }
    var pendingSelection by remember { mutableStateOf<Map<Long, CharSpan>?>(null) }
    var selectionToolbarPosition by remember { mutableStateOf(Offset.Zero) }
    var showMeaningSheet by remember { mutableStateOf(false) }
    val selectionOptionViewModel: SelectionOptionViewModel = hiltViewModel()
    val selectionMeaning by selectionOptionViewModel.apiResult
    var selectionMeaningFetchStatus by remember { mutableStateOf(Status.NOT_STARTED) }
    val selectionEdgeScrollPx = with(LocalDensity.current) { 56.dp.toPx() }

    fun dismissSelection() {
        pendingSelection = null
        showMeaningSheet = false
        selectionMeaningFetchStatus = Status.NOT_STARTED
    }

    LaunchedEffect(isDraggingSelection) {
        if (!isDraggingSelection) return@LaunchedEffect
        while (isActive && isDraggingSelection) {
            val coordinates = lazyColumnCoordinates
            if (coordinates != null) {
                val localY = coordinates.windowToLocal(currentDragWindowPosition).y
                val height = coordinates.size.height
                val scrollAmount = when {
                    localY < selectionEdgeScrollPx ->
                        -(selectionEdgeScrollPx - localY).coerceAtLeast(0f) * 0.5f

                    localY > height - selectionEdgeScrollPx ->
                        (localY - (height - selectionEdgeScrollPx)).coerceAtLeast(0f) * 0.5f

                    else -> 0f
                }
                if (scrollAmount != 0f) {
                    lazyListState.scrollBy(scrollAmount)
                    selectionController.extendTo(currentDragWindowPosition)
                }
            }
            delay(16)
        }
    }

    fun updatePoemHeaderReveal(collapsedOffsetPx: Float) {
        collapsedPoemHeaderOffsetPx = collapsedOffsetPx.coerceIn(0f, collapseRangePx)
        poemHeaderRevealFraction = 1f - collapsedPoemHeaderOffsetPx / collapseRangePx
    }

    val poemHeaderScrollConnection = remember(collapseRangePx) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val deltaY = available.y
                if (deltaY == 0f) return Offset.Zero

                val scrollingUp = deltaY < 0f
                val scrollingDown = deltaY > 0f

                return when {
                    scrollingUp && collapsedPoemHeaderOffsetPx < collapseRangePx -> {
                        val previousOffset = collapsedPoemHeaderOffsetPx
                        updatePoemHeaderReveal(collapsedPoemHeaderOffsetPx - deltaY)
                        Offset(x = 0f, y = -(collapsedPoemHeaderOffsetPx - previousOffset))
                    }

                    scrollingDown && collapsedPoemHeaderOffsetPx > 0f -> {
                        val previousOffset = collapsedPoemHeaderOffsetPx
                        updatePoemHeaderReveal(collapsedPoemHeaderOffsetPx - deltaY)
                        Offset(x = 0f, y = previousOffset - collapsedPoemHeaderOffsetPx)
                    }

                    else -> Offset.Zero
                }
            }
        }
    }
    val animatedPoemHeaderRevealFraction by animateFloatAsState(
        targetValue = poemHeaderRevealFraction,
        label = "poemHeaderRevealFraction"
    )

    LaunchedEffect(poemId) {
        collapsedPoemHeaderOffsetPx = 0f
        poemHeaderRevealFraction = 1f
    }

    fun onClick(boolean: Boolean, item: VerseWithHighlights) {
        if (boolean) selectedVerses.remove(item)
        else selectedVerses.add(item)
    }

    LaunchedEffect(poetId) {
        path = poemViewModel.getPoemPath(poemId)
        val categoryId = poemViewModel.getCategoryIdByPoemId(poemId)
        val minMaxPair = poemViewModel.getFirstAndLastWithCategoryId(categoryId)
        minId = minMaxPair.first
        maxId = minMaxPair.second
    }

    LaunchedEffect(poemId) {
        poemTitle = poemViewModel.getPoemTitle(poemId)
    }

    if (focusedVerse != null) {
        LaunchedEffect(key1 = focusedVerse) {
            lazyListState.animateScrollToItem(index = versesWithHighlights.indexOf(focusedVerse))
            shouldFocusForSearch = true
            delay(2000)
            shouldFocusForSearch = false
        }
    }

    LaunchedEffect(playStatus) {
        if (playStatus == Status.FINISHED || playStatus == Status.NOT_STARTED) {
            shouldFocusForRecitation = false
        } else if (syncInfoFetchStatus == Status.SUCCESS && audioSyncData != null) {
            while (mediaPlayer.isPlaying) {
                val currentPosition =
                    mediaPlayer.currentPosition + (audioSyncData.poemAudio?.oneSecondBugFix ?: 0)
                val syncInfo = audioSyncData.poemAudio?.syncArray?.syncInfo?.findLast {
                    currentPosition >= it.audioMiliseconds!!
                }

                if (syncInfo?.verseOrder != null && syncInfo.verseOrder!! >= 0) {
                    recitedVerseIndex = syncInfo.verseOrder!!
                    shouldFocusForRecitation = true
                } else {
                    shouldFocusForRecitation = false
                }
                delay(50)
            }
        }
    }

    LaunchedEffect(recitedVerseIndex) {
        lazyListState.animateScrollToItem(index = recitedVerseIndex)
    }

    var audioOptionChecked by remember { mutableStateOf(false) }
    var moreOptionsChecked by remember { mutableStateOf(false) }


    val toggleButtonItems = listOf(
        ToggleButtonItem(
            checkedImageVector = Icons.Filled.VolumeUp,
            uncheckedImageVector = Icons.Outlined.VolumeUp,
            contentDescription = stringResource(R.string.RECITE),
            checked = audioOptionChecked,
            onClick = { audioOptionChecked = it }
        ),
        ToggleButtonItem(
            checkedIconId = R.drawable.bookmark_selected,
            uncheckedIconId = R.drawable.bookmark,
            contentDescription = stringResource(R.string.BOOKMARK),
            checked = isBookmarked,
            onClick = { poemViewModel.onBookmarkClicked() }
        ),
        ToggleButtonItem(
            checkedIconId = R.drawable.note,
            uncheckedIconId = R.drawable.note,
            contentDescription = stringResource(R.string.COMMENT),
            checked = showNotes,
            onClick = { showNotes = !showNotes }
        ),
        ToggleButtonItem(
            checkedImageVector = Icons.Default.MoreVert,
            uncheckedImageVector = Icons.Default.MoreVert,
            contentDescription = stringResource(R.string.OPTIONS),
            checked = moreOptionsChecked,
            onClick = { moreOptionsChecked = it }
        )
    )

    val moreOptionsList = listOf(
        PoemOptionItem(
            deactivatedIcon = {
                Icon(
                    imageVector = Icons.Outlined.Visibility,
                    contentDescription = stringResource(R.string.SHOW_HIGHLIGHT)
                )
            },
            activatedIcon = {
                Icon(
                    imageVector = Icons.Outlined.VisibilityOff,
                    contentDescription = stringResource(R.string.DONT_SHOW_HIGHLIGHT)
                )
            },
            deactivatedText = stringResource(R.string.SHOW_HIGHLIGHT),
            activatedText = stringResource(R.string.DONT_SHOW_HIGHLIGHT),
            isActive = showHighlights,
            onClick = { showHighlights = !showHighlights; moreOptionsChecked = false }
        ),
        PoemOptionItem(
            deactivatedIcon = {
                Icon(
                    imageVector = Icons.Default.FormatListNumbered,
                    contentDescription = stringResource(R.string.VERSE_NUMBER)
                )
            },
            activatedIcon = {
                Icon(
                    imageVector = Icons.Default.FormatListNumbered,
                    contentDescription = stringResource(R.string.REMOVE_VERSE_NUMBER)
                )
            },
            deactivatedText = stringResource(R.string.VERSE_NUMBER),
            activatedText = stringResource(R.string.REMOVE_VERSE_NUMBER),
            isActive = showVerseNumbers,
            onClick = { showVerseNumbers = !showVerseNumbers; moreOptionsChecked = false }
        ),
        PoemOptionItem(
            deactivatedIcon = {
                Icon(
                    painter = painterResource(R.drawable.selection),
                    contentDescription = stringResource(R.string.SELECT_VERSES),
                )
            },
            deactivatedText = stringResource(R.string.SELECT_VERSES),
            isActive = selectMode,
            onClick = { selectMode = !selectMode; moreOptionsChecked = false }
        ),
        PoemOptionItem(
            deactivatedIcon = {
                Icon(
                    imageVector = Icons.Outlined.Share,
                    contentDescription = stringResource(R.string.SHARE),
                )
            },
            deactivatedText = stringResource(R.string.SHARE),
            onClick = {
                moreOptionsChecked = !moreOptionsChecked; poemViewModel.share(
                versesWithHighlights,
                path?.toPathHeaderText() ?: poemTitle,
                context
            )
            }
        )
    )

    val sheetState = rememberModalBottomSheetState()

    if (audioOptionChecked || moreOptionsChecked) {
        ModalBottomSheet(
            onDismissRequest = {
                audioOptionChecked = false
                moreOptionsChecked = false
            },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.background,
        ) {
            if (audioOptionChecked) {
                AudioListItems(
                    viewModel = poemViewModel,
                    appNavHostViewModel = appNavHostViewModel,
                    onDismiss = { audioOptionChecked = false })
            } else if (moreOptionsChecked) {
                PoemMoreOptionsList(optionsList = moreOptionsList)
            }
        }
    }

    if (showNotes) {
        NotesBottomSheet(
            onDismissRequest = { showNotes = false },
            poemId = poemId,
        )
    }

    Column(
        modifier = modifier
    ) {
        Surface(
            shadowElevation = 4.dp
        ) {
            Column {
                path?.let {
                    PoemScreenTitle(
                        modifier = Modifier
                            .verticalReveal(animatedPoemHeaderRevealFraction)
                            .alpha(animatedPoemHeaderRevealFraction),
                        navController = navController,
                        minId = minId,
                        maxId = maxId,
                        poemPath = it,
                    )
                }

                PoemScreenActionHeader(
                    toggleButtonItems = toggleButtonItems
                )
                AnimatedVisibility(
                    visible = showHintForHighlight
                ) {
                    HighlightHintMessage(poemViewModel)
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .nestedScroll(poemHeaderScrollConnection)
                .onGloballyPositioned { lazyColumnCoordinates = it }
                .pointerInput(selectMode) {
                    if (selectMode) return@pointerInput
                    detectDragGesturesAfterLongPress(
                        onDragStart = { offset ->
                            val coordinates = lazyColumnCoordinates ?: return@detectDragGesturesAfterLongPress
                            val windowPosition = coordinates.localToWindow(offset)
                            currentDragWindowPosition = windowPosition
                            isDraggingSelection = selectionController.start(windowPosition)
                        },
                        onDrag = { change, _ ->
                            change.consume()
                            val coordinates = lazyColumnCoordinates ?: return@detectDragGesturesAfterLongPress
                            val windowPosition = coordinates.localToWindow(change.position)
                            currentDragWindowPosition = windowPosition
                            selectionController.extendTo(windowPosition)
                        },
                        onDragEnd = {
                            isDraggingSelection = false
                            val endpoints = selectionController.end()
                            val result = endpoints?.let { resolveSelection(versesWithHighlights, it) }
                            val totalSelectedChars =
                                result?.values?.sumOf { (it.end - it.start).coerceAtLeast(0) } ?: 0
                            if (result != null && totalSelectedChars > 0) {
                                selectionToolbarPosition = currentDragWindowPosition
                                pendingSelection = result
                            }
                        },
                        onDragCancel = {
                            isDraggingSelection = false
                            selectionController.cancel()
                        }
                    )
                }
                .padding(10.dp),
            state = lazyListState
        ) {
            itemsIndexed(versesWithHighlights) { index, verseWithHighlights ->
                val isSelected = selectedVerses.contains(verseWithHighlights)
                val itemModifier =
                    if (shouldFocusForSearch && index == versesWithHighlights.indexOf(focusedVerse)) {
                        Modifier.background(color = MaterialTheme.colorScheme.inverseOnSurface)
                    } else if (shouldFocusForRecitation && index == recitedVerseIndex) {
                        Modifier.background(color = MaterialTheme.colorScheme.inverseOnSurface) // TODO change color maybe?
                    } else if (isSelected) {
                        Modifier.background(color = MaterialTheme.colorScheme.surfaceVariant)
                    } else {
                        Modifier
                    }

                VerseItem(
                    modifier = itemModifier,
                    verse = verseWithHighlights.verse,
                    highlights = if (showHighlights) verseWithHighlights.highlights else listOf(),
                    index = index,
                    showVerseNumber = showVerseNumbers,
                    selectMode = selectMode,
                    isSelected = isSelected,
                    onClick = {
                        if (selectMode) onClick(isSelected, verseWithHighlights)
                    },
                    verseStyle = verseStyle,
                    selectionController = selectionController,
                    pendingSelectionSpan = pendingSelection?.get(verseWithHighlights.verse.id),
                )

            }
        }
    }

    pendingSelection?.let { selection ->
        val orderedSelectedVerses = versesWithHighlights
            .filter { selection.containsKey(it.verse.id) }
            .sortedBy { it.verse.id }
        val selectedText = orderedSelectedVerses.joinToString("\n") { verseWithHighlights ->
            val span = selection.getValue(verseWithHighlights.verse.id)
            val text = verseWithHighlights.verse.text
            text.substring(span.start.coerceIn(0, text.length), span.end.coerceIn(0, text.length))
        }

        if (showMeaningSheet) {
            SelectionBottomSheet(
                viewModel = selectionOptionViewModel,
                selectedText = selectedText,
                changeMeaningFetchStatus = { selectionMeaningFetchStatus = it },
                currentMeaningFetchStatus = selectionMeaningFetchStatus,
                meaning = selectionMeaning,
                onDismiss = { dismissSelection() }
            )
        } else {
            val positionProvider = remember(selectionToolbarPosition) {
                SelectionToolbarPositionProvider(selectionToolbarPosition)
            }
            Popup(
                popupPositionProvider = positionProvider,
                onDismissRequest = { dismissSelection() },
            ) {
                SelectionToolbar(
                    onHighlight = { color ->
                        poemViewModel.highlight(selection, color)
                        dismissSelection()
                    },
                    onCopy = {
                        val clipboard =
                            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("جام سخن", selectedText)
                        clipboard.setPrimaryClip(clip)
                        dismissSelection()
                    },
                    onMeaning = { showMeaningSheet = true },
                )
            }
        }
    }

    PoemScreenBottomToolBar(selectMode, modifier, selectedVerses) {
        selectMode = false
        selectedVerses.clear()
    }
}

/** Anchors [SelectionToolbar] just above wherever the selection drag ended, clamped to stay
 * fully on screen. [windowPosition] is already in window coordinates, which is what
 * [PopupPositionProvider] expects. */
private class SelectionToolbarPositionProvider(
    private val windowPosition: Offset,
) : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize,
    ): IntOffset {
        val gapPx = 24
        val x = (windowPosition.x - popupContentSize.width / 2)
            .toInt()
            .coerceIn(0, (windowSize.width - popupContentSize.width).coerceAtLeast(0))
        val y = (windowPosition.y - popupContentSize.height - gapPx)
            .toInt()
            .coerceIn(0, (windowSize.height - popupContentSize.height).coerceAtLeast(0))
        return IntOffset(x, y)
    }
}

/**
 * Resolves drag endpoints into per-verse spans using the full verse list, not just whichever
 * verses happen to be composed right now — a long drag can scroll the anchor (or any verse in
 * between) out of the LazyColumn's composition window well before the drag ends.
 */
private fun resolveSelection(
    verses: List<VerseWithHighlights>,
    endpoints: SelectionEndpoints,
): Map<Long, CharSpan>? {
    val anchorIdx = verses.indexOfFirst { it.verse.id == endpoints.anchorVerseId }
    val currentIdx = verses.indexOfFirst { it.verse.id == endpoints.currentVerseId }
    if (anchorIdx == -1 || currentIdx == -1) return null

    if (anchorIdx == currentIdx) {
        val lo = minOf(endpoints.anchorWordStart, endpoints.currentWordStart)
        val hi = maxOf(endpoints.anchorWordEnd, endpoints.currentWordEnd)
        return mapOf(endpoints.anchorVerseId to CharSpan(lo, hi))
    }

    val forward = anchorIdx < currentIdx
    val lowIdx = if (forward) anchorIdx else currentIdx
    val highIdx = if (forward) currentIdx else anchorIdx
    val lowStart = if (forward) endpoints.anchorWordStart else endpoints.currentWordStart
    val highEnd = if (forward) endpoints.currentWordEnd else endpoints.anchorWordEnd

    val result = mutableMapOf<Long, CharSpan>()
    for (i in lowIdx..highIdx) {
        val verse = verses[i].verse
        result[verse.id] = when (i) {
            lowIdx -> CharSpan(lowStart, verse.text.length)
            highIdx -> CharSpan(0, highEnd)
            else -> CharSpan(0, verse.text.length)
        }
    }
    return result
}

private fun Modifier.verticalReveal(fraction: Float): Modifier = this
    .clipToBounds()
    .layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        val revealedHeight = (placeable.height * fraction.coerceIn(0f, 1f)).toInt()

        layout(placeable.width, revealedHeight) {
            placeable.place(0, 0)
        }
    }

@Composable
private fun HighlightHintMessage(viewModel: PoemViewModel) {
    Row(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.primary90)
            .padding(vertical = 8.dp)
            .fillMaxWidth(),

        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(2.0f),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Text(
                text = stringResource(R.string.HIGHLIGHT_HINT_MESSAGE),
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 2,
                overflow = TextOverflow.Clip,
                color = MaterialTheme.colorScheme.primary20
            )
        }
        IconButton(
            onClick = {
                viewModel.onHighlightHintDismissed()
            }) {
            Icon(
                imageVector = Icons.Default.Close, contentDescription = "close",
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary20
            )
        }
    }
}
