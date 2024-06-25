package com.example.jaamebaade_client.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.jaamebaade_client.viewmodel.VersesViewModel

@Composable
fun VersePageHeader(
    poetName: String,
    poemTitle: String,
    modifier: Modifier = Modifier,
    versesViewModel: VersesViewModel
) {
    val isBookmarked by versesViewModel.isBookmarked.collectAsState()

    val bookmarkIconColor = if (isBookmarked) Color.Red else Color.Gray
    val context = LocalContext.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Yellow) // TODO change header color
    ) {
        Text(
            text = "$poetName - $poemTitle",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(end = 8.dp).weight(0.5f),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )

        Spacer(modifier = Modifier.weight(0.1f))

        IconButton(
            onClick = { versesViewModel.onBookmarkClicked() },
            modifier = Modifier
                .padding(end = 10.dp)
                .weight(0.1F)
        ) {
            Icon(
                modifier = Modifier.size(30.dp),
                imageVector = Icons.Default.Favorite,
                contentDescription = "Bookmark",
                tint = bookmarkIconColor // Toggle color based on bookmark state

            )
        }
        Icon(
            imageVector = Icons.Default.Share,
            contentDescription = "Share",
            modifier = Modifier
                .size(30.dp)
                .weight(0.1F)
                .clickable {
                    versesViewModel.share(versesViewModel.verses.value, context)
                }
        )
    }
}

