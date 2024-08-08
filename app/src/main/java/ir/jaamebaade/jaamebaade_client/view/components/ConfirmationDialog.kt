package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
                modifier = Modifier.padding(8.dp, 0.dp),
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
                onClick = {
                    onDismiss()
                }
            ) {
                Text("نه، بعدا!")
            }
        }
    )
}
