package ir.jaamebaade.jaamebaade_client.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.jaamebaade.jaamebaade_client.repository.FontRepository
import ir.jaamebaade.jaamebaade_client.utility.restartApp

@Composable
fun FontSizeTileMenu(fontRepository: FontRepository) {
    // State to track the selected option
    var selectedOption by remember { mutableStateOf<Int?>(fontRepository.fontSizeIndex.value) }
    var showDialog by remember { mutableStateOf(false) }

    val options = listOf("ریز", "متوسط", "درشت")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        options.forEachIndexed { index, option ->
            FontSizeTile(
                option = option,
                fontSize = fontRepository.fontFamily.value.getFontSizes()[index],
                isSelected = selectedOption == index,
                onClick = {
                    selectedOption = index
                    fontRepository.setFontSize(index)
                    showDialog = true
                }
            )
        }
    }
    if (showDialog) {
        val context = LocalContext.current
        ConfirmationDialog(
            message = "برای تغییر اندازه فونت برنامه الان مجددا آغاز شود؟",
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

@Composable
fun FontSizeTile(
    option: String,
    fontSize: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(5.dp)
            .size(90.dp)
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.secondary else Color.Gray, // TODO colors
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = option,

            color = Color.White,
            fontSize = fontSize.sp,
            textAlign = TextAlign.Center
        )
    }
}
