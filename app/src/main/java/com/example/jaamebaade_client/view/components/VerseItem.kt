package com.example.jaamebaade_client.view.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.jaamebaade_client.model.Verse

@Composable
fun VerseItem(verse: Verse, modifier: Modifier = Modifier){
    Row(modifier = modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp, horizontal = 4.dp)) {
        Text(text = verse.text, style = MaterialTheme.typography.headlineSmall)
    }
}
