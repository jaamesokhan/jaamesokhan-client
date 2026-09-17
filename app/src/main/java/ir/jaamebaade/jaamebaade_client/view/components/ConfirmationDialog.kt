package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.ui.theme.Dimens

@Composable
fun ConfirmationDialog(
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        shape = MaterialTheme.shapes.extraLarge,
        onDismissRequest = onDismiss,
        title = { Text("تایید") },
        text = { Text(message) },
        confirmButton = {
            Button(
                modifier = Modifier.padding(Dimens.space8, 0.dp),
                shape = MaterialTheme.shapes.large,
                onClick = {
                    onConfirm()
                }
            ) {
                Text("بله")
            }
        },
        dismissButton = {
            Button(
                modifier = Modifier.padding(Dimens.space8, 0.dp),
                shape = MaterialTheme.shapes.large,
                onClick = {
                    onDismiss()
                }
            ) {
                Text("نه، بعدا!")
            }
        }
    )
}
