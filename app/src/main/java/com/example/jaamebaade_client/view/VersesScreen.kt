package com.example.jaamebaade_client.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.navigation.navOptions
import com.example.jaamebaade_client.view.components.VerseItem
import com.example.jaamebaade_client.view.components.VersePageHeader
import com.example.jaamebaade_client.viewmodel.VersesViewModel

@Composable
fun VerseScreen(navController: NavController, poemId: Int, poetId: Int, modifier: Modifier) {

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

    var showVerseNumbers by remember { mutableStateOf(false) }
    var categoryId: Int = 0
    LaunchedEffect(poetId) {
        poetName = versesViewModel.getPoetName(poetId)
        categoryId = versesViewModel.getCategoryIdByPoemId(poemId)
        val minMaxPair = versesViewModel.getFirstAndLastWithCategoryId(categoryId)
        minId = minMaxPair.first
        maxId = minMaxPair.second
    }

    LaunchedEffect(poemId) {
        poemTitle = versesViewModel.getPoemTitle(poemId)
    }
    val versesWithHighlights by versesViewModel.verses.collectAsState()
    Column(
        modifier = modifier
    ) {
        VersePageHeader(
            navController,
            poetId,
            poemId,
            minId,
            maxId,
            poetName = poetName,
            poemTitle = poemTitle,
            versesViewModel = versesViewModel,
            showVerseNumbers = showVerseNumbers,
            onToggleVerseNumbers = { showVerseNumbers = !showVerseNumbers }
        )

        Spacer(modifier = Modifier.width(200.dp))

        Spacer(modifier = Modifier.width(200.dp))

        LazyColumn {
            itemsIndexed(versesWithHighlights) { index, verseWithHighlights ->
                VerseItem(
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