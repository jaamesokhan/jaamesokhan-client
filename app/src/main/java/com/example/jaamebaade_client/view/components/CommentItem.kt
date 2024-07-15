package com.example.jaamebaade_client.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.jaamebaade_client.model.Comment
import com.example.jaamebaade_client.utility.convertToJalali
import com.example.jaamebaade_client.utility.toLocalFormat
import java.util.Date

@Composable
fun CommentItem(comment: Comment) {
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
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
            Text(text = comment.text)
        }
    }
}