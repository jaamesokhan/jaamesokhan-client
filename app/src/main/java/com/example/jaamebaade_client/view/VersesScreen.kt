package com.example.jaamebaade_client.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.jaamebaade_client.model.Status
import com.example.jaamebaade_client.view.components.VerseItem
import com.example.jaamebaade_client.view.components.VersePageHeader
import com.example.jaamebaade_client.viewmodel.VersesViewModel
import kotlinx.coroutines.delay

@Composable
fun VerseScreen(
    navController: NavController,
    poemId: Int,
    poetId: Int,
    focusedVerseId: Int?,
    modifier: Modifier
) {

    var poetName by remember(poetId) {
        mutableStateOf("")
    }

    var poemTitle by remember(poemId) {
        mutableStateOf("")
    }

    val versesViewModel =
        hiltViewModel<VersesViewModel, VersesViewModel.VerseViewModelFactory> { factory ->
            factory.create(
                poemId, poetId
            )
        }
    var minId by remember { mutableIntStateOf(0) }
    var maxId by remember { mutableIntStateOf(0) }

    var shouldFocusForSearch by remember { mutableStateOf(false) }
    var shouldFocusForRecitation by remember { mutableStateOf(false) }


    var showVerseNumbers by remember { mutableStateOf(false) }

    val lazyListState = rememberLazyListState()

    val mediaPlayer = versesViewModel.mediaPlayer
    val playStatus = versesViewModel.playStatus
    val syncInfoFetchStatus = versesViewModel.syncInfoFetchStatus
    val audioSyncData = versesViewModel.audioSyncInfo.collectAsState().value
    var recitedVerseIndex by remember { mutableIntStateOf(0) }

    val versesWithHighlights by versesViewModel.verses.collectAsState()

    val focusedVerse = versesWithHighlights.find { it.verse.id == focusedVerseId }

    LaunchedEffect(poetId) {
        poetName = versesViewModel.getPoetName(poetId)
        val categoryId = versesViewModel.getCategoryIdByPoemId(poemId)
        val minMaxPair = versesViewModel.getFirstAndLastWithCategoryId(categoryId)
        minId = minMaxPair.first
        maxId = minMaxPair.second
    }

    LaunchedEffect(poemId) {
        poemTitle = versesViewModel.getPoemTitle(poemId)
    }

    if (focusedVerse != null) {
        LaunchedEffect(key1 = focusedVerse) {
            lazyListState.animateScrollToItem(index = versesWithHighlights.indexOf(focusedVerse))
            shouldFocusForSearch = true
            delay(2000)
            shouldFocusForSearch = false
        }
    }

    LaunchedEffect(playStatus, syncInfoFetchStatus) {
        if (syncInfoFetchStatus == Status.SUCCESS && audioSyncData != null) {
            while (mediaPlayer.isPlaying) {
                val currentPosition =
                    mediaPlayer.currentPosition + (audioSyncData.poemAudio?.oneSecondBugFix ?: 0)
                val syncInfo = audioSyncData.poemAudio?.syncArray?.syncInfo?.findLast {
                    currentPosition >= it.audioMiliseconds!!
                }

                if (syncInfo?.verseOrder != null && syncInfo.verseOrder!! >= 0) {
                    recitedVerseIndex = syncInfo.verseOrder!!
                    shouldFocusForRecitation = true
                    delay(25)
                } else {
                    shouldFocusForSearch = false
                }
            }
        }
        if (playStatus == Status.FINISHED) {
            shouldFocusForSearch = false
        }
    }

    LaunchedEffect(recitedVerseIndex) {
        lazyListState.animateScrollToItem(index = recitedVerseIndex)
    }

    Column(
        modifier = modifier
    ) {
        VersePageHeader(
            navController,
            poetId,
            poemId,
            minId,
            maxId,
            versesViewModel = versesViewModel,
            showVerseNumbers = showVerseNumbers,
            onToggleVerseNumbers = { showVerseNumbers = !showVerseNumbers }
        )

        Spacer(modifier = Modifier.width(200.dp))

        Spacer(modifier = Modifier.width(200.dp))

        LazyColumn(state = lazyListState) {
            itemsIndexed(versesWithHighlights) { index, verseWithHighlights ->
                val itemModifier =
                    if (shouldFocusForSearch && index == versesWithHighlights.indexOf(focusedVerse)) {
                        Modifier.background(color = MaterialTheme.colorScheme.inverseOnSurface)
                    } else if (shouldFocusForRecitation && index == recitedVerseIndex) {
                        Modifier.background(color = MaterialTheme.colorScheme.inverseOnSurface) // TODO change color maybe?
                    } else {
                        Modifier
                    }

                VerseItem(
                    modifier = itemModifier,
                    verse = verseWithHighlights.verse,
                    highlights = verseWithHighlights.highlights,
                    index = index,
                    showVerseNumber = showVerseNumbers
                ) { startIndex, endIndex ->
                    versesViewModel.highlight(verseWithHighlights.verse.id, startIndex, endIndex)
                }
            }
        }
    }
}