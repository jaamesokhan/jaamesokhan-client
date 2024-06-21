package com.example.jaamebaade_client.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
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

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Yellow) // TODO change header color
    ) {
        Text(
            text = poetName,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = "-",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = poemTitle,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(end = 16.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            onClick = { versesViewModel.onBookmarkClicked() },
            modifier = Modifier
                .padding(end = 10.dp)
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
                .clickable { } // TODO implement share
        )
    }
}
