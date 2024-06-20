package com.example.jaamebaade_client.view

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jaamebaade_client.view.components.VerseItem
import com.example.jaamebaade_client.viewmodel.PoemsListViewModel
import com.example.jaamebaade_client.viewmodel.VersesViewModel

@Composable
fun VerseScreen(poemId: Int, modifier: Modifier) {
    val poemViewModel =
        hiltViewModel<VersesViewModel, VersesViewModel.VerseViewModelFactory> { factory ->
            factory.create(
                poemId
            )
        }
    val verses by poemViewModel.verses.collectAsState()
    LazyColumn (modifier = modifier){
        itemsIndexed(verses){_, verse ->
            VerseItem(verse = verse)
        }
    }
}