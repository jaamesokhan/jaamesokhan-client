package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.ui.theme.ButtonShape
import ir.jaamebaade.jaamebaade_client.ui.theme.CardShape

@Composable
fun ConfirmationDialog(
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        shape = CardShape,
        onDismissRequest = onDismiss,
        title = { Text("تایید") },
        text = { Text(message) },
        confirmButton = {
            Button(
                modifier = Modifier.padding(8.dp, 0.dp),
                shape = ButtonShape,
                onClick = {
                    onConfirm()
                }
            ) {
                Text("بله")
            }
        },
        dismissButton = {
            Button(
                modifier = Modifier.padding(8.dp, 0.dp),
                shape = ButtonShape,
                onClick = {
                    onDismiss()
                }
            ) {
                Text("نه، بعدا!")
            }
        }
    )
}
