package com.example.jaamebaade_client.view.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.jaamebaade_client.repository.FontRepository
import com.example.jaamebaade_client.ui.theme.getFontByFontFamilyName
import com.example.jaamebaade_client.utility.restartApp

@Composable
fun FontFamilyMenu(fontRepository: FontRepository) {
    var selectedFontFamily by remember { mutableStateOf(fontRepository.fontFamily.value) }
    val fontFamiliesList = fontRepository.fonts // Example font families
    var expanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    Text(
        text = selectedFontFamily.takeIf { it.isNotBlank() } ?: "VazirMatn",
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
        fontFamiliesList.forEachIndexed { index, fontFamily ->
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
            if (index != fontFamiliesList.lastIndex)
                HorizontalDivider()
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