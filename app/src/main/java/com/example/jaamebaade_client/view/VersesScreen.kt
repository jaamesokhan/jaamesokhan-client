package com.example.jaamebaade_client.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jaamebaade_client.view.components.VerseItem
import com.example.jaamebaade_client.view.components.VersePageHeader
import com.example.jaamebaade_client.viewmodel.VersesViewModel

@Composable
fun VerseScreen(poemId: Int, poetId: Int, modifier: Modifier) {
    val versesViewModel =
        hiltViewModel<VersesViewModel, VersesViewModel.VerseViewModelFactory> { factory ->
            factory.create(
                poemId, poetId
            )
        }
    var poetName by remember(poetId) {
        mutableStateOf("")
    }

    var poemTitle by remember(poemId) {
        mutableStateOf("")
    }

    LaunchedEffect(poetId) {
        poetName = versesViewModel.getPoetName(poetId)
    }

    LaunchedEffect(poemId) {
        poemTitle = versesViewModel.getPoemTitle(poemId)
    }
    val verses by versesViewModel.verses.collectAsState()
    Column(modifier = modifier) {
        VersePageHeader(
            poetName = poetName,
            poemTitle = poemTitle,
            versesViewModel = versesViewModel
        )
        Spacer(modifier = Modifier.width(200.dp)) // Add space

        LazyColumn() {
            itemsIndexed(verses) { _, verse ->
                VerseItem(verse = verse)
            }
        }
    }
}