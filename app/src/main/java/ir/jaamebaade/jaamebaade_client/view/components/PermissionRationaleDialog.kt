package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.R

@Composable
fun PermissionRationaleDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(id = R.string.NOTIFICATION_ACCESS_TITLE),
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = { Text(text = stringResource(id = R.string.NOTIFICATION_ACCESS_BODY)) },
        confirmButton = {
            Button(modifier = Modifier.padding(4.dp), onClick = onConfirm) {
                Text(
                    text = stringResource(id = R.string.YES),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        dismissButton = {
            Button(modifier = Modifier.padding(4.dp), onClick = onDismiss) {
                Text(
                    text = stringResource(id = R.string.NOT_NOW),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    )
}