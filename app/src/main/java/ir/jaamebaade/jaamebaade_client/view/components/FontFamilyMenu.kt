package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.jaamebaade.jaamebaade_client.repository.FontRepository
import ir.jaamebaade.jaamebaade_client.ui.theme.CustomFonts
import ir.jaamebaade.jaamebaade_client.utility.restartApp

@Composable
fun FontFamilyMenu(fontRepository: FontRepository) {
    var selectedFontFamily by remember { mutableStateOf(fontRepository.fontFamily.value) }
    var expanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        Row(
            modifier = Modifier.clickable { expanded = true },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedFontFamily.displayName,
                style = TextStyle(
                    fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize
                )
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "more")
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                CustomFonts.getAllFonts().forEachIndexed { index, customFont ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = customFont.displayName,
                                style = TextStyle(
                                    fontFamily = customFont.fontFamily,
                                    fontSize = 20.sp
                                )
                            )
                        },
                        onClick = {
                            selectedFontFamily = customFont
                            fontRepository.setFontFamily(customFont)
                            showDialog = true
                            expanded = false
                        })
                    if (index != CustomFonts.getAllFonts().lastIndex)
                        HorizontalDivider()
                }
            }
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