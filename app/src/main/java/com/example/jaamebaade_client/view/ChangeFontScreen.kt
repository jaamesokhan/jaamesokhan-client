package com.example.jaamebaade_client.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jaamebaade_client.repository.FontRepository
import com.example.jaamebaade_client.ui.theme.Nastaliq
import com.example.jaamebaade_client.ui.theme.VazirMatn
import com.example.jaamebaade_client.ui.theme.getFontByFontFamilyName

@Composable
fun ChangeFontScreen(modifier: Modifier, fontRepository: FontRepository) {

    var fontSize by remember { mutableFloatStateOf(fontRepository.fontSize.value) }
//   var fontFamily by remember { mutableStateOf("DefaultFontFamily") }
    var selectedFontFamily by remember { mutableStateOf(fontRepository.fontFamily.value) }
    val fontFamiliesList = fontRepository.fonts // Example font families
    var expanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text("اندازه فونت")
        Slider(
            value = fontSize,
            onValueChange = {
                fontSize = it
                fontRepository.setFontSize(it)
            },
            valueRange = 12f..50f
        )

        Text("فونت برنامه")

        // Clickable text to show dropdown menu
        Text(
            text = selectedFontFamily.takeIf { it.isNotBlank() } ?: "Default",
            modifier = Modifier.clickable(onClick = { expanded = true }),
            style = TextStyle(
                fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize
            )
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            fontFamiliesList.forEach { fontFamily ->
                DropdownMenuItem(text = {
                    Text(
                        fontFamily, style = TextStyle(
                            fontFamily = getFontByFontFamilyName(fontFamily),
                            fontSize = 20.sp
                        )
                    )
                }, onClick = {
                    selectedFontFamily = fontFamily
                    fontRepository.setFontFamily(fontFamily)
                    showDialog = true
                    expanded = false
                })
            }
        }
        if (showDialog) {
            val context = LocalContext.current
            ConfirmationDialog(
                message = "برای تغییر فونت برنامه الان مجددا آغاز شود؟",
                onConfirm = {
                    // Handle confirmation action
                    showDialog = false
                    restartApp(context)
                    // Place your logic here for the action after confirmation
                    // e.g., navigate to a new screen, perform an action, etc.
                },
                onDismiss = {
                    // Handle dismiss action (cancel)
                    showDialog = false
                    // Optionally handle cancel action
                }
            )
        }
    }
}

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

fun restartApp(context: Context) {
    val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
    intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
    if (context is Activity) {
        context.finish()
    }
}