package com.example.jaamebaade_client.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jaamebaade_client.model.Poem
import com.example.jaamebaade_client.model.Poet
import com.example.jaamebaade_client.model.VerseWithHighlights

@Composable
fun HighlightItem(
    modifier: Modifier = Modifier,
    verseWithHighlights: VerseWithHighlights

) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
                VerseItem(verse = verseWithHighlights.verse, highlights = verseWithHighlights.highlights) {a, b ->}
        }
    }
}