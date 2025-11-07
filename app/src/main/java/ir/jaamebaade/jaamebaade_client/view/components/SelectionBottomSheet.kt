package ir.jaamebaade.jaamebaade_client.view.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.ContentCopy
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.constants.HighlightColors
import ir.jaamebaade.jaamebaade_client.model.Status
import ir.jaamebaade.jaamebaade_client.model.Verse
import ir.jaamebaade.jaamebaade_client.ui.theme.secondaryS50
import ir.jaamebaade.jaamebaade_client.view.components.base.CustomBottomSheet
import ir.jaamebaade.jaamebaade_client.view.components.poem.HighlightActionButton
import ir.jaamebaade.jaamebaade_client.viewmodel.SelectionOptionViewModel

@Composable
fun SelectionBottomSheet(
    viewModel: SelectionOptionViewModel,
    verse: Verse,
    startIndex: Int,
    endIndex: Int,
    changeMeaningFetchStatus: (status: Status) -> Unit,
    currentMeaningFetchStatus: Status,
    highlightCallBack: (startIndex: Int, endIndex: Int, color: String) -> Unit,
    context: Context,
    meaning: String,
    onDismiss: () -> Unit,
) {
    var selectedColor by remember { mutableStateOf(HighlightColors.DEFAULT) }
    
    CustomBottomSheet(
        onDismissRequest = onDismiss,
    ) {
        viewModel.getWordMeaning(
            word = verse.text.substring(startIndex, endIndex),
            successCallBack = {
                changeMeaningFetchStatus(Status.SUCCESS)
            },
            failureCallBack = {
                changeMeaningFetchStatus(Status.FAILED)
            })
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp)) {
            Row(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.Left,
            ) {
                HighlightActionButton(
                    painter = painterResource(R.drawable.highlight),
                    text = stringResource(R.string.HIGHLIGHT),
                ) {
                    highlightCallBack(
                        startIndex,
                        endIndex,
                        HighlightColors.colorToHex(selectedColor)
                    )
                    onDismiss()
                }

                Spacer(modifier = Modifier.width(12.dp))

                HighlightActionButton(
                    painter = rememberVectorPainter(image = Icons.Default.ContentCopy),
                    text = stringResource(R.string.COPY),
                    outlined = true,
                ) {
                    val clipboard =
                        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText(
                        "جام سخن",
                        verse.text.substring(startIndex, endIndex)
                    )
                    clipboard.setPrimaryClip(clip)
                    onDismiss()
                }
            }
            
            // Color picker
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.SELECT_HIGHLIGHT_COLOR),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                HighlightColors.ALL_COLORS.forEach { color ->
                    ColorCircle(
                        color = color,
                        isSelected = color == selectedColor,
                        onClick = { selectedColor = color }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.Circle,
                    tint = MaterialTheme.colorScheme.secondaryS50,
                    contentDescription = null,
                    modifier = Modifier.size(8.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = verse.text.substring(startIndex, endIndex),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.headlineLarge,
                )
            }
            when (currentMeaningFetchStatus) {
                Status.LOADING, Status.NOT_STARTED -> {
                    Box(modifier = Modifier.height(40.dp)) {
                        LoadingIndicator()
                    }
                }

                Status.FAILED -> {
                    Text("خطا در دریافت معنی")
                }

                Status.SUCCESS -> {
                    Text(
                        text = meaning,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        modifier = Modifier
                            .padding(
                                bottom = 32.dp
                            )
                            .verticalScroll(rememberScrollState()),
                    )
                }

                else -> {

                }
            }
        }
    }
}

@Composable
private fun ColorCircle(
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(color)
            .border(
                width = if (isSelected) 3.dp else 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                shape = CircleShape
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
}