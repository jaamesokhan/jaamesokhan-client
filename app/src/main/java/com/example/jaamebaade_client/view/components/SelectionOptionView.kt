package com.example.jaamebaade_client.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jaamebaade_client.viewmodel.SelectionOptionViewModel

@Composable
fun SelectionOptionView(
    verseId: Int,
    startIndex: Int,
    endIndex: Int,
) {
    val viewModel =
        hiltViewModel<SelectionOptionViewModel, SelectionOptionViewModel.SelectionOptionViewModelFactory> { factory ->
            factory.create(verseId)
        }
    val verse = viewModel.verse
    Column(modifier = Modifier.background(color = Color.White)) {
        Row {
            Button(onClick = {
                viewModel.highlight(verseId, startIndex, endIndex)
                // close dialog

            }) {
                Text("هایلایت")
            }
        }
        Text(text = "معنی: ${verse?.verse?.text?.substring(startIndex, endIndex)}")
        Text(text = "verse id ${verseId} start index ${startIndex} end index ${endIndex}")
    }
}