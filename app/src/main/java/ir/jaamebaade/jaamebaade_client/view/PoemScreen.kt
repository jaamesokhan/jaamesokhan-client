package ir.jaamebaade.jaamebaade_client.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ir.jaamebaade.jaamebaade_client.model.Status
import ir.jaamebaade.jaamebaade_client.model.VersePoemCategoriesPoet
import ir.jaamebaade.jaamebaade_client.model.VerseWithHighlights
import ir.jaamebaade.jaamebaade_client.view.components.PoemScreenActionHeader
import ir.jaamebaade.jaamebaade_client.view.components.PoemScreenBottomToolBar
import ir.jaamebaade.jaamebaade_client.view.components.PoemScreenPathHeader
import ir.jaamebaade.jaamebaade_client.view.components.VerseItem
import ir.jaamebaade.jaamebaade_client.viewmodel.AudioViewModel
import ir.jaamebaade.jaamebaade_client.viewmodel.PoemViewModel
import kotlinx.coroutines.delay

@Composable
fun PoemScreen(
    navController: NavController,
    poemId: Int,
    poetId: Int,
    focusedVerseId: Long?,
    modifier: Modifier,
    audioViewModel: AudioViewModel = hiltViewModel()
) {

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


    var showVerseNumbers by remember { mutableStateOf(false) }
    var selectMode by remember { mutableStateOf(false) }

    val lazyListState = rememberLazyListState()

    val mediaPlayer = audioViewModel.mediaPlayer
    val playStatus = audioViewModel.playStatus
    val syncInfoFetchStatus = poemViewModel.syncInfoFetchStatus
    val audioSyncData = poemViewModel.audioSyncInfo.collectAsState().value
    var recitedVerseIndex by remember { mutableIntStateOf(0) }

    val versesWithHighlights by poemViewModel.verses.collectAsState()

    val focusedVerse = versesWithHighlights.find { it.verse.id == focusedVerseId }

    val selectedVerses = remember { mutableStateListOf<VerseWithHighlights>() }


    fun onClick(boolean: Boolean, item: VerseWithHighlights) {
        if (boolean) selectedVerses.remove(item)
        else selectedVerses.add(item)

        if (selectedVerses.isEmpty()) selectMode = false
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

    Column(
        modifier = modifier
    ) {
        Surface(
            shadowElevation = 4.dp
        ) {
            Column {
                path?.let {
                    PoemScreenPathHeader(
                        navController = navController,
                        minId = minId,
                        maxId = maxId,
                        poemPath = it,
                    )
                }

                PoemScreenActionHeader(
                    navController = navController,
                    poetId = poetId,
                    poemId = poemId,
                    poetName = path?.poet?.name,
                    poemViewModel = poemViewModel,
                    showVerseNumbers = showVerseNumbers,
                    selectMode = selectMode,
                    audioViewModel = audioViewModel,
                    onToggleVerseNumbers = { showVerseNumbers = !showVerseNumbers },
                    onToggleSelectMode = {
                        selectMode = !selectMode
                        if (!selectMode) selectedVerses.clear()
                    },
                )
            }
        }

        LazyColumn(modifier = Modifier.padding(10.dp), state = lazyListState) {
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
                    highlights = verseWithHighlights.highlights,
                    index = index,
                    showVerseNumber = showVerseNumbers,
                    selectMode = selectMode,
                    isSelected = isSelected,
                    onClick = {
                        if (selectMode) onClick(isSelected, verseWithHighlights)
                    },
                ) { startIndex, endIndex ->
                    poemViewModel.highlight(
                        verseWithHighlights.verse.id,
                        startIndex,
                        endIndex
                    )
                }

            }
        }
    }

    PoemScreenBottomToolBar(selectMode, modifier, selectedVerses) {
        selectMode = false
        selectedVerses.clear()
    }
}
