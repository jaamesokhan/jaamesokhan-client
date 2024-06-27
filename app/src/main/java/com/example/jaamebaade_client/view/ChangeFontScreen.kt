package com.example.jaamebaade_client.view

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.ui.text.TextStyle
import com.example.jaamebaade_client.repository.FontRepository

@Composable
fun ChangeFontScreen(modifier: Modifier, fontRepository: FontRepository) {

    var fontSize by remember { mutableFloatStateOf(fontRepository.fontSize.value) }
//   var fontFamily by remember { mutableStateOf("DefaultFontFamily") }
    var selectedFontFamily by remember { mutableStateOf(fontRepository.fontFamily.value) }
    val fontFamiliesList = fontRepository.fonts // Example font families
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text("اندازه فونت")
        Slider(
            value = fontSize,
            onValueChange = {
                fontSize = it
                fontRepository.setFontSize(it)
            },
            valueRange = 12f..32f
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
                    Text(fontFamily)
                }, onClick = {
                    selectedFontFamily = fontFamily
                    fontRepository.setFontFamily(fontFamily)
                    expanded = false
                })
            }
        }
    }
}
