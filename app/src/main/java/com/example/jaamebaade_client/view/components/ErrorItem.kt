package com.example.jaamebaade_client.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ErrorItem(error: Throwable, onRetry: () -> Unit) {
    Column {
        Text(text = error.message ?: "Unknown Error")
        Button(onClick = onRetry) {
            Text(text = "Retry")
        }
    }
}