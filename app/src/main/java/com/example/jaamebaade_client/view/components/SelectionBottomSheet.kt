package com.example.jaamebaade_client.view.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jaamebaade_client.model.Status
import com.example.jaamebaade_client.model.Verse
import com.example.jaamebaade_client.viewmodel.SelectionOptionViewModel

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SelectionBottomSheet(
    sheetState: SheetState,
    viewModel: SelectionOptionViewModel,
    verse: Verse,
    startIndex: Int,
    endIndex: Int,
    changeMeaningFetchStatus: (status: Status) -> Unit,
    currentMeaningFetchStatus: Status,
    highlightCallBack: (startIndex: Int, endIndex: Int) -> Unit,
    context: Context,
    meaning: String,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        viewModel.getWordMeaning(
            word = verse.text.substring(startIndex, endIndex),
            successCallBack = {
                changeMeaningFetchStatus(Status.SUCCESS)
            },
            failureCallBack = {
                changeMeaningFetchStatus(Status.FAILED)
            })
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.SpaceEvenly,
            ) {
                Button(onClick = {
                    highlightCallBack(
                        startIndex,
                        endIndex
                    )
                    onDismiss()

                }) {
                    Text("هایلایت", style = MaterialTheme.typography.bodySmall)
                }

                Button(onClick = {
                    val clipboard =
                        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText(
                        "جام سخن",
                        verse.text.substring(startIndex, endIndex)
                    )
                    clipboard.setPrimaryClip(clip)
                    onDismiss()
                }) {
                    Text("کپی", style = MaterialTheme.typography.bodySmall)
                }
            }
            Row(modifier = Modifier.padding(bottom = 8.dp)) {
                Text(
                    text = " معنی:"
                )
                Text(
                    text = verse.text.substring(startIndex, endIndex),
                    fontWeight = FontWeight.Bold
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
                        modifier = Modifier.padding(
                            bottom = 10.dp
                        )
                    )
                }

                else -> {

                }
            }
        }
    }
}