package com.example.jaamebaade_client.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.jaamebaade_client.model.Comment
import com.example.jaamebaade_client.utility.convertToJalali
import com.example.jaamebaade_client.utility.toLocalFormat
import java.util.Date

@Composable
fun CommentItem(comment: Comment, onDeleteClicked: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "تاریخ" + ": " +
                        Date(comment.createdAt).convertToJalali().toLocalFormat(),
                style = MaterialTheme.typography.labelMedium
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(modifier = Modifier.weight(0.9f)) {
                    Text(text = comment.text)
                }
                Box(modifier = Modifier.padding(8.dp).weight(0.1f)) {
                    IconButton(
                        onClick = {
                            onDeleteClicked()
                        }
                    ) {
                        Icon(imageVector = Icons.Outlined.Delete, contentDescription = "حذف")
                    }
                }
            }
        }
    }
}