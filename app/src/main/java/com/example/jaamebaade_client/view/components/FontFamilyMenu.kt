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
import com.example.jaamebaade_client.ui.theme.CustomFont
import com.example.jaamebaade_client.utility.restartApp

@Composable
fun FontFamilyMenu(fontRepository: FontRepository) {
    var selectedFontFamily by remember { mutableStateOf(fontRepository.fontFamily.value) }
    var expanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    Text(
        text = selectedFontFamily.displayName,
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
        CustomFont.entries.forEachIndexed { index, customFont ->
            DropdownMenuItem(text = {
                Text(
                    text = customFont.displayName,
                    style = TextStyle(
                        fontFamily = customFont.getFontFamily(),
                        fontSize = 20.sp
                    )
                )
            }, onClick = {
                selectedFontFamily = customFont
                fontRepository.setFontFamily(customFont)
                showDialog = true
                expanded = false
            })
            if (index != CustomFont.entries.lastIndex)
                HorizontalDivider()
        }
    }
    if (showDialog) {
        val context = LocalContext.current
        ConfirmationDialog(
            message = "برای تغییر فونت برنامه الان مجددا آغاز شود؟",
            onConfirm = {
                showDialog = false
                restartApp(context)
            },
            onDismiss = {
                showDialog = false
            }
        )
    }
}