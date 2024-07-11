package com.example.jaamebaade_client.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.jaamebaade_client.api.response.AudioData

@Composable
fun AudioListScreen(
    expanded: Boolean,
    audioDataList: List<AudioData>,
    onDismiss: () -> Unit,
    onClick: (AudioData) -> Unit
) {

    DropdownMenu(
        modifier = Modifier
            .width(250.dp)
            .height(250.dp),
        expanded = expanded,
        onDismissRequest = onDismiss,
    ) {
        if (audioDataList.isEmpty()) {
            Text(
                text = "هیچ خوانشی یافت نشد",
                modifier = Modifier.padding(8.dp)
            )
        }
        audioDataList.forEachIndexed { i, audioData ->
            DropdownMenuItem(
                modifier = Modifier
                    .clickable {
                        onClick(audioData)
                    },
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.MusicNote,
                            contentDescription = "audio for ${audioData.artist}"
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = audioData.artist)
                        // TODO add more data
                    }
                }, onClick = {
                })
            if (i != audioDataList.size - 1) {
                Divider()
            }
        }
    }
}

