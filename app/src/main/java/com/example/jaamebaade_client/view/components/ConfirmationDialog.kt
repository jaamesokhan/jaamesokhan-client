package com.example.jaamebaade_client.view.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ConfirmationDialog(
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("تایید") },
        text = { Text(message) },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm()
                }
            ) {
                Text("بله")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onDismiss()
                }
            ) {
                Text("نه، بعدا!")
            }
        }
    )
}
