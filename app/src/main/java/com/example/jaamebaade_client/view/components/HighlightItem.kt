package com.example.jaamebaade_client.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jaamebaade_client.model.HighlightVersePoemPoet
import com.example.jaamebaade_client.model.Poem
import com.example.jaamebaade_client.model.Poet
import com.example.jaamebaade_client.model.VerseWithHighlights

@Composable
fun HighlightItem(
    modifier: Modifier = Modifier,
    highlightVersePoemPoet: HighlightVersePoemPoet,
    navController: NavController

) {
    val startIndex = highlightVersePoemPoet.highlight.startIndex
    val endIndex = highlightVersePoemPoet.highlight.endIndex
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { navController.navigate("poem/${highlightVersePoemPoet.poet.id}/${highlightVersePoemPoet.poem.id}") }

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Text(
                text = "${highlightVersePoemPoet.poet.name}>${highlightVersePoemPoet.poem.title}",
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                highlightVersePoemPoet.verse.text.substring(startIndex, endIndex),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Red,
            )

        }
    }
}